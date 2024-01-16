package id.nexus.farmap.ui.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import id.nexus.farmap.ui.MainUI
import id.nexus.farmap.helper.ui.ScreenNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun EntryPage(
    scope: CoroutineScope,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
    label: String
){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column{
            if (MainUI.adminMode) {
                var editor by remember { mutableStateOf(false) }
                var editDesc by remember { mutableStateOf(MainUI.map.mapNodes[label]?.get("longDesc") as String) }
                var descError by remember { mutableStateOf(false) }
                var editUrl by remember {
                    mutableStateOf(MainUI.map.mapNodes[label]?.get("imagesUrl").let {
                        it as List<String>
                        it.joinToString("\n")
                    })
                }
                var urlError by remember { mutableStateOf(false) }

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    LazyRow {
                        items(MainUI.map.mapNodes[label]?.get("imagesUrl") as List<String>) { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier.fillMaxHeight(0.5f)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        FloatingActionButton(
                            modifier = Modifier.padding(24.dp),
                            onClick = {
                                if (editor) {
                                    if (!descError && !urlError) {
                                        editUrl.split("\n")
                                            .map { it.trim() }
                                            .let { MainUI.map.updateEntry(label, editDesc, it) }

                                        editor = false
                                    }
                                } else {
                                    editor = true
                                }
                            }) {
                            Icon(if (editor) Icons.Filled.Done else Icons.Filled.Edit, null)
                        }

                        FloatingActionButton(
                            modifier = Modifier.padding(24.dp),
                            onClick = {
                                navController.navigate(ScreenNavigator.ARNav.route)
                                scope.launch {
                                    snackbarHostState.showSnackbar("[INFO] Routing to $label")
                                }
                            }
                        ) {
                            Icon(Icons.Filled.Send, null)
                        }
                    }
                }

                LazyColumn(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    item{ Text(label, fontSize = 24.sp) }
                    item { (MainUI.map.mapNodes[label]?.get("longDesc") as? String)?.let { Text(it, textAlign = TextAlign.Justify) } }

                    if (editor) {
                        item {
                            TextField(
                                value = editDesc,
                                onValueChange = {
                                    descError = it.isEmpty()
                                    editDesc = it
                                },
                                label = { Text("Description") },
                                singleLine = false,
                                isError = descError,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        item {
                            TextField(
                                value = editUrl,
                                onValueChange = {
                                    urlError = it.isEmpty()
                                    editUrl = it
                                },
                                label = { Text("Image URLs (newline-separated)") },
                                singleLine = false,
                                isError = urlError,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    LazyRow {
                        items(MainUI.map.mapNodes[label]?.get("imagesUrl") as List<String>) { url ->
                            AsyncImage(
                                model = url,
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier.fillMaxHeight(0.5f)
                            )
                        }
                    }

                    FloatingActionButton(
                        modifier = Modifier.padding(24.dp),
                        onClick = {
                            navController.navigate(ScreenNavigator.ARNav.route)
                            scope.launch {
                                snackbarHostState.showSnackbar("[INFO] Routing to $label")
                            }
                        }
                    ) {
                        Icon(Icons.Filled.Send, null)
                    }
                }

                LazyColumn(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
                ) {
                    item { Text(label, fontSize = 24.sp) }
                    item { (MainUI.map.mapNodes[label]?.get("longDesc") as? String)?.let { Text(it, textAlign = TextAlign.Justify) } }
                }
            }
        }
    }
}