package id.nexus.farmap.ui.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import id.nexus.farmap.ui.MainUI
import id.nexus.farmap.helper.ui.ScreenNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainMenu(
    scope: CoroutineScope,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {

        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                var expanded by remember { mutableStateOf(false) }
                if (MainUI.adminMode){
                    var editor by remember { mutableStateOf(false) }
                    var editName by remember { mutableStateOf(MainUI.map.mapName) }
                    var editAddress by remember { mutableStateOf(MainUI.map.mapAddress) }
                    var editUrl by remember { mutableStateOf(MainUI.map.iconUrl) }
                    var nameError by remember { mutableStateOf(false) }
                    var addressError by remember { mutableStateOf(false) }
                    var urlError by remember { mutableStateOf(false) }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model= MainUI.map.iconUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(0.2f).clickable { expanded = !expanded }
                        )

                        Button(
                            onClick = {
                                if (editor) {
                                    if (!nameError && !addressError && !urlError) {
                                        MainUI.map.uploadMetadata(editName, editAddress, editUrl)
                                        editor = false
                                    }
                                } else{
                                    editor = true
                                }
                            }) {
                            Text(if (editor) "Save" else "Edit")
                        }
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Welcome to")
                        Text(MainUI.map.mapName, fontSize = 24.sp)

                        Text(
                            text = MainUI.map.mapAddress,
                            maxLines = if (expanded) Int.MAX_VALUE else 1
                        )

                        if (editor) {
                            TextField(
                                value = editName,
                                onValueChange = {
                                    nameError = it.isEmpty()
                                    editName = it
                                },
                                label = { Text("Location Name") },
                                singleLine = false,
                                isError = nameError,
                                modifier = Modifier.fillMaxWidth()
                            )

                            TextField(
                                value = editAddress,
                                onValueChange = {
                                    addressError = it.isEmpty()
                                    editAddress = it
                                },
                                label = { Text("Location Address") },
                                singleLine = false,
                                isError = addressError,
                                modifier = Modifier.fillMaxWidth()
                            )

                            TextField(
                                value = editUrl,
                                onValueChange = {
                                    urlError = it.isEmpty()
                                    editUrl = it
                                },
                                label = { Text("Icon URL") },
                                singleLine = false,
                                isError = urlError,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                } else {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = MainUI.map.iconUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth(0.2f).clickable { expanded = !expanded }
                        )
                    }

                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Welcome to")
                        Text(MainUI.map.mapName, fontSize = 24.sp)
                        Text(
                            text = MainUI.map.mapAddress,
                            maxLines = if (expanded) Int.MAX_VALUE else 1
                        )
                    }
                }

            }

            Divider(modifier = Modifier.fillMaxWidth().width(2.dp))

            var query by remember { mutableStateOf("") }
            var active by remember { mutableStateOf(false) }

            SearchBar(
                query = query,
                onQueryChange = { query = it },
                active = active,
                onActiveChange = { active = it},
                placeholder = { Text("Pick Destination") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                onSearch = {
                    active = false
                    if (MainUI.map.mapNodes.containsKey(query)){
                        navController.navigate("${ScreenNavigator.EntryPage.route}/${query}")
                        scope.launch {
                            snackbarHostState.showSnackbar("[INFO] Searching $query.")
                        }
                    }else{
                        scope.launch {
                            snackbarHostState.showSnackbar("[ERROR] Invalid Destination.")
                        }
                    }
                }
            ) {
                LazyColumn {
                    items(MainUI.map.mapNodes.keys.filter {
                        val type = (MainUI.map.mapNodes[it]?.get("type") as Long).toInt()
                        if (query.isNotEmpty())

                            it.lowercase().contains(query.lowercase()) && (type == 1 || type == 2)
                        else false
                    }){ idx ->
                        ListItem(
                            headlineContent = { Text(idx) },
                            supportingContent = { (MainUI.map.mapNodes[idx]?.get("shortDesc") as? String)?.let { Text(it) } },
                            leadingContent = { Icon(Icons.Filled.Place, contentDescription = null) },
                            modifier = Modifier
                                .clickable {
                                    query = idx
                                }
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}