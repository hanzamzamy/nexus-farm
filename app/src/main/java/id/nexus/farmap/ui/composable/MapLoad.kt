package id.nexus.farmap.ui.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.Source
import id.nexus.farmap.helper.navigation.Map
import id.nexus.farmap.ui.MainUI
import id.nexus.farmap.helper.ui.ScreenNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MapLoad(
    scope: CoroutineScope,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            var mapName by remember { mutableStateOf("") }
            var adminMode by remember { mutableStateOf(false) }
            var forceCache by remember { mutableStateOf(false) }

            TextField(
                value = mapName,
                onValueChange = { mapName = it },
                label = { Text("Map Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(0.8f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    Column {
                        Text("Admin Mode", fontSize = 12.sp)
                        Switch(
                            checked = adminMode,
                            onCheckedChange = { adminMode = it }
                        )
                    }

                    Column {
                        Text("Force Cache", fontSize = 12.sp)
                        Switch(
                            checked = forceCache,
                            onCheckedChange = { forceCache = it }
                        )
                    }
                }

                Button(onClick = {
                    if (mapName.isNotEmpty()) {
                        MainUI.adminMode = adminMode
                        MainUI.sourceDB = if (forceCache) Source.CACHE else Source.DEFAULT
                        MainUI.map = Map(
                            mapName,
                            onSuccess = { msg ->
                                scope.launch {
                                    snackbarHostState.showSnackbar("[INFO] $msg")
                                }
                                navController.navigate(ScreenNavigator.MainMenu.route)
                            },
                            onFail = { msg ->
                                scope.launch {
                                    snackbarHostState.showSnackbar("[ERROR] $msg")
                                }
                            })
                    }
                }) {
                    Text("Load")
                }
            }
        }
    }
}