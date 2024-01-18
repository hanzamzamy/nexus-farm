package id.nexus.farmap.helper.ar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue


class ViewerData {
    var showBottomSheet by mutableStateOf(false)
    var tempNodeID by mutableStateOf("")
    var tempNodePL by mutableStateOf((hashMapOf<String, Any>()))
    var selectNode by mutableStateOf("")
    var startNode by mutableStateOf("")
    var endNode by mutableStateOf("")
    var mode by mutableStateOf(Mode.NOP)
}