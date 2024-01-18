package id.nexus.farmap.ui.composable.armenu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.ar.core.Pose
import id.nexus.farmap.helper.ar.ARContent
import id.nexus.farmap.helper.ar.ViewerData
import id.nexus.farmap.helper.ui.ARMenuNavigator
import id.nexus.farmap.ui.MainUI
import io.github.sceneview.math.Rotation
import io.github.sceneview.math.toQuaternion
import kotlinx.coroutines.CoroutineScope

@Composable
fun MainPanel(
    menuNavController: NavController
){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(24.dp)
        ) {
            Button(
                onClick = {
                    menuNavController.navigate(ARMenuNavigator.AddNode.route)
                }
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Add, null)
                    Text("Add", fontSize = 12.sp)
                }
            }

            Button(
                onClick = {
                    menuNavController.navigate(ARMenuNavigator.ARRoute.route)
                }
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Search, null)
                    Text("Route", fontSize = 12.sp)
                }
            }

            Button(
                onClick = {
                    menuNavController.navigate(ARMenuNavigator.MapInit.route)
                }
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Refresh, null)
                    Text("Init", fontSize = 12.sp)
                }
            }

            Button(
                onClick = {

                }
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.KeyboardArrowUp, null)
                    Text("Upload", fontSize = 12.sp)
                }
            }
        }
    }
}