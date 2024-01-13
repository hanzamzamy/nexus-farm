package id.nexus.farmap.ui

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.firestore.Source
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import id.nexus.farmap.helper.navigation.Map

@Composable
fun FARMapScreen() {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    NavHost(navController, startDestination = Navigator.MapLoad.route) {
        composable(Navigator.MapLoad.route) { MapLoad(scope, navController, snackbarHostState) }
        composable(Navigator.MainMenu.route) { MainMenu(scope, navController, snackbarHostState) }
        composable(Navigator.ARNav.route) { ARNav(scope, navController, snackbarHostState) }
    }
}

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
                                navController.navigate(Navigator.MainMenu.route)
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

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

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
        var query by remember { mutableStateOf("") }
        var active by remember { mutableStateOf(false) }

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
                AsyncImage(
                    model=MainUI.map.iconUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth(0.2f).clickable { expanded = !expanded }
                )
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

            Divider(modifier = Modifier.fillMaxWidth().width(2.dp))

            SearchBar(
                query = query,
                onQueryChange = { query = it },
                active = active,
                onActiveChange = { active = it},
                placeholder = { Text("Pick Destination") },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                onSearch = {
                    active = false
                    scope.launch {
                        snackbarHostState.showSnackbar("Searching $query")
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

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun ARNav(
    scope: CoroutineScope,
    navController: NavController,
    snackbarHostState: SnackbarHostState
) {

}