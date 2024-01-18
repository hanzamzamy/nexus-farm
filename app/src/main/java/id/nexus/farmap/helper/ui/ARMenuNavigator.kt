package id.nexus.farmap.helper.ui

sealed class ARMenuNavigator(val route: String) {
    object MainPanel: ARMenuNavigator("MainPanel")
    object ARRoute: ARMenuNavigator("ARRoute")
    object MapInit: ARMenuNavigator("MapInit")
    object AddNode: ARMenuNavigator("AddNode")
    object NodeDetail: ARMenuNavigator("NodeDetail")
    object ShowNode: ARMenuNavigator("ShowNode")
    object MapUpload: ARMenuNavigator("MapUpload")

}