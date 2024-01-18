package id.nexus.farmap.ui.composable.armenu

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import id.nexus.farmap.helper.ar.Mode
import id.nexus.farmap.helper.ar.ViewerData
import id.nexus.farmap.helper.ui.ARMenuNavigator
import id.nexus.farmap.ui.MainUI

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNode(
    menuNavController: NavController,
    data: ViewerData
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        var selected by remember { mutableStateOf(0) }
        var idError by remember { mutableStateOf(false) }
        var descError by remember { mutableStateOf(false) }
        var urlError by remember { mutableStateOf(false) }
        var scaleError by remember { mutableStateOf(false) }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            FilterChip(
                onClick = {
                    selected = 1
                    data.tempNodeID = "path${MainUI.map.availableId}"
                    data.tempNodePL = hashMapOf(
                        "type" to 0,
                        "orPos" to hashMapOf(
                            "qw" to 0, "qx" to 0, "qy" to 0, "qz" to 0, "x" to 0, "y" to 0,"z" to 0,
                        ),
                        "neighbor" to listOf("")
                    ) },
                label = {
                    Text("Path")
                },
                selected = selected == 1,
                leadingIcon = if (selected == 1) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                },
            )

            FilterChip(
                onClick = {
                    selected = 2
                    data.tempNodePL = hashMapOf(
                        "type" to 1,
                        "orPos" to hashMapOf(
                            "qw" to 0, "qx" to 0, "qy" to 0, "qz" to 0, "x" to 0, "y" to 0,"z" to 0,
                        ),
                        "neighbor" to listOf(""),
                        "imagesUrl" to listOf(""),
                        "longDecs" to "Long Description.",
                        "shortDesc" to "Short Description."
                    )},
                label = {
                    Text("Init")
                },
                selected = selected == 2,
                leadingIcon = if (selected == 2) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                },
            )

            FilterChip(
                onClick = {
                    selected = 3
                    data.tempNodePL = hashMapOf(
                        "type" to 2,
                        "orPos" to hashMapOf(
                            "qw" to 0, "qx" to 0, "qy" to 0, "qz" to 0, "x" to 0, "y" to 0,"z" to 0,
                        ),
                        "neighbor" to listOf(""),
                        "imagesUrl" to listOf(""),
                        "longDecs" to "Long Description.",
                        "shortDesc" to "Short Description.",
                        "modelUrl" to "",
                        "modelScale" to 1f
                    )},
                label = {
                    Text("Entry")
                },
                selected = selected == 3,
                leadingIcon = if (selected == 3) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                },
            )

            FilterChip(
                onClick = {
                    selected = 4
                    data.tempNodePL = hashMapOf(
                        "type" to 3,
                        "orPos" to hashMapOf(
                            "qw" to 0, "qx" to 0, "qy" to 0, "qz" to 0, "x" to 0, "y" to 0,"z" to 0,
                        ),
                        "modelUrl" to "",
                        "modelScale" to 1f
                    )},
                label = {
                    Text("Render")
                },
                selected = selected == 4,
                leadingIcon = if (selected == 4) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "Done icon",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    }
                } else {
                    null
                },
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth()
        ) {
            Text(when (selected) {
                1 -> "Path Node"
                2 -> "Calibration Node"
                3 -> "Entry Node"
                4 -> "Renderable Node"
                else -> "Select node type"
            })

            if(selected != 2 && selected != 0){
                TextField(
                    value = data.tempNodeID,
                    onValueChange = {
                        idError = it.isEmpty() || MainUI.map.mapNodes.containsKey(it)
                        data.tempNodeID = it
                    },
                    label = { Text("Node ID") },
                    singleLine = true,
                    isError = idError,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (selected == 2 || selected == 3){
                TextField(
                    value = data.tempNodePL["shortDesc"] as String,
                    onValueChange = {
                        descError = it.isEmpty()
                        data.tempNodePL["shortDesc"] = it
                    },
                    label = { Text("Short Description") },
                    singleLine = true,
                    isError = descError,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (selected == 3 || selected == 4){
                TextField(
                    value = data.tempNodePL["modelUrl"] as String,
                    onValueChange = {
                        urlError = it.isEmpty()
                        data.tempNodePL["modelUrl"] = it
                    },
                    label = { Text("Model URL") },
                    singleLine = true,
                    isError = urlError,
                    modifier = Modifier.fillMaxWidth()
                )

                TextField(
                    value = (data.tempNodePL["modelScale"] as Float).toString(),
                    onValueChange = {
                        scaleError = it.isEmpty() || !(it.contains(Regex("^[0-9]*\\\\.?[0-9]*\$")))
                        data.tempNodePL["modelScale"] = it.toFloat()
                    },
                    label = { Text("Model Scale") },
                    singleLine = true,
                    isError = scaleError,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }



        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(24.dp).fillMaxWidth()
        ) {
            Button(
                onClick = {
                    menuNavController.navigate(ARMenuNavigator.MainPanel.route)
                }
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.ArrowBack, null)
                    Text("Back", fontSize = 12.sp)
                }
            }

            Button(
                onClick = {
                    data.showBottomSheet = false
                    data.mode = Mode.ADD
                }
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.Add, null)
                    Text("Place", fontSize = 12.sp)
                }
            }
        }
    }
}