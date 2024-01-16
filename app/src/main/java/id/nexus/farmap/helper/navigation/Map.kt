package id.nexus.farmap.helper.navigation

import id.nexus.farmap.ui.MainUI

class Map(
    name: String,
    val onSuccess: (String) -> Unit = {},
    val onFail: (String) -> Unit = {}
) {
    val docRef = MainUI.mapDB.document(name)
    var mapNodes: MutableMap<String, HashMap<String, Any>> = mutableMapOf()
    var updatedNode: List<String> = mutableListOf()
    var availableId = 1

    lateinit var mapName: String
    lateinit var mapAddress: String
    lateinit var iconUrl: String

    init {
        docRef.get(MainUI.sourceDB)
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    doc.data?.let { updateMetadata(it) }

                    docRef.collection("nodes").get(MainUI.sourceDB)
                        .addOnSuccessListener { nodes ->
                            for (node in nodes){
                                mapNodes += node.id to node.data as HashMap<String, Any>
                            }

                            updateIdCounter()

                            onSuccess("Map loaded.")
                        }
                        .addOnFailureListener { e ->
                            onFail("Couldn't load nodes: $e.")
                        }
                }else if(MainUI.adminMode){
                    val data = hashMapOf<String, Any>(
                        "name" to name,
                        "address" to "Earth",
                        "iconUrl" to "https://firebasestorage.googleapis.com/v0/b/nexus-farmap.appspot.com/o/logo%2FLogo%20ITS-Biru.png?alt=media&token=f9912a17-bc01-4c15-9c59-1e02ca6770fe"
                    ).let {
                        updateMetadata(it)
                    }
                    docRef.set(data)
                        .addOnSuccessListener {
                            val dataNode = hashMapOf(
                                "type" to 0,
                                "neighbor" to listOf<String>(),
                                "orPos" to mapOf(
                                    "qw" to 0,
                                    "qx" to 0,
                                    "qy" to 0,
                                    "qz" to 0,
                                    "x" to 0,
                                    "y" to 0,
                                    "z" to 0
                                )
                            )
                           docRef.collection("nodes").document("path0").set(dataNode)
                               .addOnSuccessListener {
                                   mapNodes += "path0" to dataNode
                                   updateIdCounter()
                                   onSuccess("Map created.")
                               }
                               .addOnFailureListener { e ->
                                   onFail("Couldn't create node: $e.")
                               }
                        }
                        .addOnFailureListener { e ->
                            onFail("Couldn't create map: $e.")
                        }
                }else {
                    onFail("Map didn't exist.")
                }
            }
            .addOnFailureListener { e ->
                onFail("Couldn't load map: $e.")
            }
    }

    fun updateIdCounter(){
        while (mapNodes.containsKey("path$availableId")){
            availableId++
        }
    }

    fun updateMetadata(src: MutableMap<String, Any>){
        mapName = src["name"] as String
        mapAddress = src["address"] as String
        iconUrl = src["iconUrl"] as String
    }

    fun uploadMetadata(name: String, address: String, iconUrl: String){
        val data = hashMapOf<String, Any>(
            "name" to name,
            "address" to address,
            "iconUrl" to iconUrl
        )
        docRef.update(data)
            .addOnSuccessListener {
                updateMetadata(data)
                onSuccess("Metadata ${docRef.id} updated.")
            }
            .addOnFailureListener { e ->
                onFail("Couldn't update metadata: $e.")
            }
    }

    fun updateEntry(id: String, desc: String, url: List<String>){
        val data = hashMapOf<String, Any>(
            "longDesc" to desc,
            "imagesUrl" to url,
        )

        docRef.collection("nodes").document(id).update(data)
            .addOnSuccessListener {
                mapNodes[id]?.putAll(data)
                onSuccess("Entry $id updated.")
            }
            .addOnFailureListener { e ->
                onFail("Couldn't update entry: $e.")
            }
    }

    fun updateARNode(id: String, payload: HashMap<String, Any>){

    }

    fun uploadARNodes(nodes: List<String>){

    }
}