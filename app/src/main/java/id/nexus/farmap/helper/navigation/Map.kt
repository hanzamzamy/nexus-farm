package id.nexus.farmap.helper.navigation

import id.nexus.farmap.ui.MainUI

class Map(name: String) {
    val docRef = MainUI.mapDB.document(name)
    var mapNodes: MutableMap<String, MutableMap<String, Any>> = mutableMapOf()
    var updatedNode: List<String> = mutableListOf()
    var availableId = 1

    lateinit var mapName: String
    lateinit var mapAddress: String
    lateinit var iconUrl: String

    init {
        docRef.get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    doc.data?.let {
                        updateMetadata(it)
                    }

                    docRef.collection("nodes").get()
                        .addOnSuccessListener { nodes ->
                            for (node in nodes){
                                mapNodes += node.id to node.data
                            }

                            updateIdCounter()

                            throw Exception("Map loaded.")
                        }
                        .addOnFailureListener { e ->
                            throw Error("Couldn't load nodes: $e.")
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
                           docRef.collection("nodes").document("0").set(dataNode)
                               .addOnSuccessListener {
                                   mapNodes += "0" to dataNode
                                   updateIdCounter()
                                   throw Exception("Map created.")
                               }
                               .addOnFailureListener { e ->
                                   throw Error("Couldn't create node: $e.")
                               }
                        }
                        .addOnFailureListener { e ->
                            throw Error("Couldn't create map: $e.")
                        }
                }else {
                    throw Exception("Map didn't exist.")
                }
            }
            .addOnFailureListener { e ->
                throw Error("Couldn't load map: $e.")
            }
    }

    fun updateMetadata(src: MutableMap<String, Any>){
        mapName = src["name"] as String
        mapAddress = src["address"] as String
        iconUrl = src["iconUrl"] as String
    }

    fun updateIdCounter(){
        while (mapNodes.containsKey("$availableId")){
            availableId++
        }
    }

    fun changeMetadata(name: String, address: String, iconUrl: String){
        val data = hashMapOf<String, Any>(
            "name" to name,
            "address" to address,
            "iconUrl" to iconUrl
        )
        docRef.set(data)
            .addOnSuccessListener {
                updateMetadata(data)
                throw Exception("Metadata changed.")
            }
            .addOnFailureListener { e ->
                throw Error("Couldn't change metadata: $e.")
            }
    }
}