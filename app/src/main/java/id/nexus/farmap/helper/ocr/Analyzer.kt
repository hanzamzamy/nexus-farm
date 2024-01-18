package id.nexus.farmap.helper.ocr

import android.content.Context.CAMERA_SERVICE
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.SparseIntArray
import android.view.Surface
import com.google.ar.core.Frame
import com.google.ar.core.Session
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import id.nexus.farmap.ui.MainUI

class Analyzer(
    val onSuccess: (String) -> Unit = {},
    val onFail: (String) -> Unit = {}
) {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    val ORIENTATIONS = SparseIntArray()
    var result: String? = null

    init {
        ORIENTATIONS.append(Surface.ROTATION_0, 0)
        ORIENTATIONS.append(Surface.ROTATION_90, 90)
        ORIENTATIONS.append(Surface.ROTATION_180, 180)
        ORIENTATIONS.append(Surface.ROTATION_270, 270)
    }

    private fun getRotationCompensation(cameraId: String): Int {
        // Get the device's current rotation relative to its "native" orientation.
        // Then, from the ORIENTATIONS table, look up the angle the image must be
        // rotated to compensate for the device's rotation.
        val deviceRotation = MainUI.instance.windowManager.defaultDisplay.rotation
        var rotationCompensation = ORIENTATIONS.get(deviceRotation)

        // Get the device's sensor orientation.
        val cameraManager = MainUI.instance.getSystemService(CAMERA_SERVICE) as CameraManager
        val sensorOrientation = cameraManager
            .getCameraCharacteristics(cameraId)
            .get(CameraCharacteristics.SENSOR_ORIENTATION) ?: 0

        rotationCompensation = (sensorOrientation - rotationCompensation + 360) % 360
        return rotationCompensation
    }

    private fun textFilter(text: Text.TextBlock?): Boolean {
        return text?.text?.contains(Regex("^[a-zA-Z0-9]+\$")) ?: false
    }

    fun analyze(frame: Frame, session: Session){
        val mediaImage = frame.acquireCameraImage()
        val rotation = getRotationCompensation(session.cameraConfig.cameraId)
        val image = InputImage.fromMediaImage(mediaImage, rotation)

        recognizer.process(image)
            .addOnSuccessListener {visionText ->
                mediaImage.close()

                if(visionText.textBlocks.isNotEmpty()){
                    val block = visionText.textBlocks.firstOrNull{
                        textFilter(it)
                    }

                    if(block != null){
                        val bBox = block.boundingBox

                        if (bBox != null){
                            val cPoint = Pair(bBox.exactCenterX(), bBox.exactCenterY())
                            result = block.text
                            onSuccess("Found $result at $cPoint.")
                        }else{
                            onFail("No bounding box found.")
                        }
                    }else{
                        onFail("Text doesn't meet format.")
                    }
                }else{
                    onFail("No text found.")
                }


            }
            .addOnFailureListener { e ->
                onFail("Couldn't analyze image: $e.")
            }

    }

}