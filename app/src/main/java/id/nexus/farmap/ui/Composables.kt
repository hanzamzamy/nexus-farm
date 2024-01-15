package id.nexus.farmap.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
        composable(
            route ="${Navigator.EntryPage.route}/{label}",
            arguments = listOf(navArgument("label"){
                type = NavType.StringType
            })
        ) { EntryPage(scope, navController, snackbarHostState, it.arguments?.getString("label") ?: "") }
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
                            model=MainUI.map.iconUrl,
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
                        navController.navigate("${Navigator.EntryPage.route}/${query}")
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

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

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
//            if(MainUI.adminMode) {
//                var editor by remember { mutableStateOf(false) }
//                var editDesc by remember { mutableStateOf(MainUI.map.mapNodes[label]?.get("longDesc") as String) }
//                var descError by remember { mutableStateOf(false) }
//
//                Box(
//                    modifier = Modifier.fillMaxWidth(),
//                    contentAlignment = Alignment.BottomEnd
//                ){
//                    LazyRow {
//                        items(5/*MainUI.map.mapNodes[label]?.get("imagesUrl") as List<String>*/){url->
//                            AsyncImage(
//                                model = "https://ipsf.net/wp-content/uploads/2021/12/dummy-image-square.webp",//url,
//                                contentDescription = null,
//                                contentScale = ContentScale.FillHeight,
//                                modifier = Modifier.fillMaxHeight(0.5f)
//                            )
//                        }
//                    }
//
//                    FloatingActionButton(
//                        modifier = Modifier.padding(24.dp),
//                        onClick = {
//                            navController.navigate(Navigator.ARNav.route)
//                            scope.launch {
//                                snackbarHostState.showSnackbar("[INFO] Routing to $label")
//                            }
//                        }
//                    ){
//                        Icon(Icons.Filled.Send, null)
//                    }
//                }
//
//                Column(
//                    horizontalAlignment = Alignment.Start,
//                    verticalArrangement = Arrangement.spacedBy(16.dp),
//                    modifier = Modifier.padding(16.dp)
//                ){
//                    Text("A123" /*label*/, fontSize = 24.sp)
//                    (MainUI.map.mapNodes[label]?.get("longDesc") as? String)?.let { Text(it) }
//
//                    TextField(
//                        value = editDesc,
//                        onValueChange = {
//                            descError = it.isEmpty()
//                            editDesc = it
//                        },
//                        label = { Text("Description") },
//                        singleLine = false,
//                        isError = descError,
//                        modifier = Modifier.fillMaxWidth()
//                    )
//                }
//
//                Column(
//                    horizontalAlignment = Alignment.CenterHorizontally,
//                    verticalArrangement = Arrangement.Bottom
//                ){
//                    Button(
//                        modifier = Modifier.fillMaxWidth(),
//                        onClick = {
//                            if (editor) {
//                                if (!descError) {
//                                    //TODO
//                                    editor = false
//                                }
//                            } else{
//                                editor = true
//                            }
//                        }) {
//                        Text(if (editor) "Save" else "Edit")
//                    }
//                }
//            }else{
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.BottomEnd
                ){
                    LazyRow {
                        items(5/*MainUI.map.mapNodes[label]?.get("imagesUrl") as List<String>*/){url->
                            AsyncImage(
                                model = "https://ipsf.net/wp-content/uploads/2021/12/dummy-image-square.webp",//url,
                                contentDescription = null,
                                contentScale = ContentScale.FillHeight,
                                modifier = Modifier.fillMaxHeight(0.5f)
                            )
                        }
                    }

                    FloatingActionButton(
                        modifier = Modifier.padding(24.dp),
                        onClick = {
                            navController.navigate(Navigator.ARNav.route)
                            scope.launch {
                                snackbarHostState.showSnackbar("[INFO] Routing to $label")
                            }
                        }
                    ){
                        Icon(Icons.Filled.Send, null)
                    }
                }

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(16.dp)
                ){
                    Text("A123" /*label*/, fontSize = 24.sp)
                    (MainUI.map.mapNodes[label]?.get("longDesc") as? String)?.let { Text(it) }
                    }
            }

//        }

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