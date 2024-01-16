package id.nexus.farmap.helper.ui

sealed class ARMenuNavigator(val route: String) {
    object MainPanel: ARMenuNavigator("MainPanel")
    object ARRoute: ARMenuNavigator("ARRoute")
    object MapInit: ARMenuNavigator("MapInit")
    object AddNode: ARMenuNavigator("AddNode")
    object AddEntry: ARMenuNavigator("AddEntry")
    object AddCalEntry: ARMenuNavigator("AddCalEntry")
    object AddPath: ARMenuNavigator("AddPath")
    object AddRend: ARMenuNavigator("AddRend")
    object NodeDetail: ARMenuNavigator("NodeDetail")
    object LinkNode: ARMenuNavigator("LinkNode")
    object DeleteNode: ARMenuNavigator("DeleteNode")
    object ShowNode: ARMenuNavigator("ShowNode")
    object MapUpload: ARMenuNavigator("MapUpload")

}