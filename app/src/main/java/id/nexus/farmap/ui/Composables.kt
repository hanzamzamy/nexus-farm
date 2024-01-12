package id.nexus.farmap.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
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
                        try {
                            MainUI.map = Map(mapName)
                            navController.navigate(Navigator.MainMenu.route)
                        }catch (e: Error){
                            scope.launch {
                                snackbarHostState.showSnackbar("[ERROR] $e")
                            }
                        }catch (e: Exception){
                            scope.launch {
                                snackbarHostState.showSnackbar("[INFO] $e")
                            }
                        }
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

        SearchBar(
            query = query,
            onQueryChange = { query = it },
            active = active,
            onActiveChange = { active = it},
            placeholder = { Text("Pick Destination") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = null) },
            onSearch = {
                active = false
                scope.launch {
                    snackbarHostState.showSnackbar("Searching $query")
                }
            }
        ) {
            repeat(4) { idx ->
                val resultText = "Suggestion $idx"
                ListItem(
                    headlineContent = { Text(resultText) },
                    supportingContent = { Text("Additional info") },
                    leadingContent = { Icon(Icons.Filled.Star, contentDescription = null) },
                    modifier = Modifier
                        .clickable {
                            query = resultText
                            active = false
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Welcome to")
            Text("")
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