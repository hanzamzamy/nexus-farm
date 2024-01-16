package id.nexus.farmap.ui.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.ar.core.Config
import com.google.ar.core.Frame
import com.google.ar.core.Plane
import com.google.ar.core.TrackingFailureReason
import id.nexus.farmap.R
import id.nexus.farmap.helper.ui.ARMenuNavigator
import id.nexus.farmap.ui.MainUI
import id.nexus.farmap.ui.composable.armenu.*
import id.nexus.farmap.ui.theme.FARMapTheme
import io.github.sceneview.*
import io.github.sceneview.ar.ARScene
import io.github.sceneview.ar.arcore.createAnchorOrNull
import io.github.sceneview.ar.arcore.firstByTypeOrNull
import io.github.sceneview.ar.arcore.getUpdatedPlanes
import io.github.sceneview.ar.getDescription
import io.github.sceneview.ar.rememberARCameraNode
import io.github.sceneview.model.ModelInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ARNav(
    scope: CoroutineScope,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
//        val engine = rememberEngine()
//        val modelLoader = rememberModelLoader(engine)
//        val materialLoader = rememberMaterialLoader(engine)
//        val cameraNode = rememberARCameraNode(engine)
//        val childNodes = rememberNodes()
//        val view = rememberView(engine)
//        val collisionSystem = rememberCollisionSystem(view)
//
//        val planeRenderer by remember { mutableStateOf(true) }
//
//        val modelInstances = remember { mutableListOf<ModelInstance>() }
//        var trackingFailureReason by remember {
//            mutableStateOf<TrackingFailureReason?>(null)
//        }
//        var frame by remember { mutableStateOf<Frame?>(null) }
//        ARScene(
//            modifier = Modifier.fillMaxSize(),
//            childNodes = childNodes,
//            engine = engine,
//            view = view,
//            modelLoader = modelLoader,
//            collisionSystem = collisionSystem,
//            sessionConfiguration = { session, config ->
//                config.depthMode =
//                    when (session.isDepthModeSupported(Config.DepthMode.AUTOMATIC)) {
//                        true -> Config.DepthMode.AUTOMATIC
//                        else -> Config.DepthMode.DISABLED
//                    }
//                config.imageStabilizationMode =
//                    when (session.isImageStabilizationModeSupported(Config.ImageStabilizationMode.EIS)) {
//                        true -> Config.ImageStabilizationMode.EIS
//                        else -> Config.ImageStabilizationMode.OFF
//                    }
//                config.instantPlacementMode = Config.InstantPlacementMode.LOCAL_Y_UP
//                config.lightEstimationMode =
//                    Config.LightEstimationMode.ENVIRONMENTAL_HDR
//            },
//            cameraNode = cameraNode,
//            planeRenderer = planeRenderer,
//            onTrackingFailureChanged = {
//                trackingFailureReason = it
//            },
//            onSessionUpdated = { session, updatedFrame ->
//                frame = updatedFrame
//
//                updatedFrame.getUpdatedPlanes()
//                    .firstOrNull { it.type == Plane.Type.VERTICAL }
//                    ?.let { it.createAnchorOrNull(it.centerPose) }?.let { anchor ->
//
//                    }
//
//                //frame!!.acquireCameraImage()
//            },
//            onGestureListener = rememberOnGestureListener(
//                onSingleTapConfirmed = { motionEvent, node ->
//                    if (node == null) {
//                        val hitResults = frame?.hitTest(motionEvent.x, motionEvent.y)
//                        hitResults?.firstByTypeOrNull(Plane.Type.entries.toSet())?.createAnchorOrNull()
//                            ?.let { anchor ->
//
//                            }
//                    }
//                })
//        )
        Text(
            modifier = Modifier
                .systemBarsPadding()
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(16.dp),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            color = Color.White,
            text = "Halo! Halo semuanya! Apakah suara saya terdengar? Saya ingin bertanya"
//            text = trackingFailureReason?.getDescription(LocalContext.current) ?: if (childNodes.isEmpty()) {
//                stringResource(R.string.ar_scan_direction)
//            } else {
//                stringResource(R.string.ar_place_direction)
//            }
        )

        if (MainUI.adminMode) {
            var showBottomSheet by remember { mutableStateOf(false) }

            FloatingActionButton(
                modifier = Modifier
                    .padding(48.dp)
                    .align(Alignment.BottomCenter),
                onClick = {
                    showBottomSheet = true
                }
            ){
                Icon(Icons.Filled.Menu, null)
            }

            if(showBottomSheet){
                ModalBottomSheet(
                    onDismissRequest = {
                        scope.launch {
                            showBottomSheet = false
                        }
                    }
                ){
                    val menuNavController = rememberNavController()

                    NavHost(menuNavController, startDestination = ARMenuNavigator.MainPanel.route) {
                        composable(ARMenuNavigator.MainPanel.route) { MainPanel() }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AppPreview(){
    FARMapTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            FARMapScreen()
        }
    }
}