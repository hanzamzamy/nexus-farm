package id.nexus.farmap.ui

sealed class Navigator(val route: String) {
    object MapLoad: Navigator("MapLoad")
    object MainMenu: Navigator("MainMenu")
    object EntryPage: Navigator("EntryPage")
    object ARNav: Navigator("ARNav")
}