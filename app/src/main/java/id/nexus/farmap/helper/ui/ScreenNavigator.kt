package id.nexus.farmap.helper.ui

sealed class ScreenNavigator(val route: String) {
    object MapLoad: ScreenNavigator("MapLoad")
    object MainMenu: ScreenNavigator("MainMenu")
    object EntryPage: ScreenNavigator("EntryPage")
    object ARNav: ScreenNavigator("ARNav")
}