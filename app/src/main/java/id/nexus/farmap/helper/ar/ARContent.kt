package id.nexus.farmap.helper.ar

import android.content.Context
import com.google.ar.core.Frame
import com.google.ar.core.Pose
import com.google.ar.core.Session
import io.github.sceneview.ar.node.AnchorNode

class ARContent(
    val onSuccess: (String) -> Unit = {},
    val onFail: (String) -> Unit = {}
) {
    var renderedNode: MutableMap<String, AnchorNode> = mutableMapOf()
    var transPose: Pose = Pose.IDENTITY

    //fun createPathNode()

    fun createCalNode(frame: Frame, id: String, htPoint: Pair<Float, Float>){
        frame.hitTest(htPoint.first, htPoint.second)
    }
}