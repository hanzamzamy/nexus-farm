package id.nexus.farmap.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import id.nexus.farmap.helper.ui.ScreenNavigator

@Composable
fun FARMapScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val navController = rememberNavController()
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        NavHost(navController, startDestination = ScreenNavigator.ARNav.route) {
            composable(ScreenNavigator.MapLoad.route) { MapLoad(scope, navController, snackbarHostState) }
            composable(ScreenNavigator.MainMenu.route) { MainMenu(scope, navController, snackbarHostState) }
            composable(
                route = "${ScreenNavigator.EntryPage.route}/{label}",
                arguments = listOf(navArgument("label") {
                    type = NavType.StringType
                })
            ) { EntryPage(scope, navController, snackbarHostState, it.arguments?.getString("label") ?: "") }
            composable(ScreenNavigator.ARNav.route) { ARNav(scope, navController, snackbarHostState) }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}