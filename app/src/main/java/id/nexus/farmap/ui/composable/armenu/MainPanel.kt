package id.nexus.farmap.ui.composable.armenu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import id.nexus.farmap.helper.ui.ARMenuNavigator

@Composable
fun MainPanel(){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {

                },
                modifier = Modifier.padding(8.dp)
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Add, null)
                    Text("Add", fontSize = 12.sp)
                }
            }

            Button(
                onClick = {

                },
                modifier = Modifier.padding(8.dp)
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.List, null)
                    Text("List", fontSize = 12.sp)
                }
            }

            Button(
                onClick = {

                },
                modifier = Modifier.padding(8.dp)
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Search, null)
                    Text("Route", fontSize = 12.sp)
                }
            }

            Button(
                onClick = {

                },
                modifier = Modifier.padding(8.dp)
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.KeyboardArrowUp, null)
                    Text("Upload", fontSize = 12.sp)
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 24.dp).fillMaxWidth()
        ) {
            Button(
                onClick = {

                },
                modifier = Modifier.padding(8.dp),
                enabled = false
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Refresh, null)
                    Text("Init", fontSize = 12.sp)
                }
            }
        }
    }
}