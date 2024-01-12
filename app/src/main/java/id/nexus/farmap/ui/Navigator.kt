package id.nexus.farmap.ui

sealed class Navigator(val route: String) {
    object MapLoad: Navigator("MapLoad")
    object MainMenu: Navigator("MainMenu")
    object ARNav: Navigator("ARNav")
}