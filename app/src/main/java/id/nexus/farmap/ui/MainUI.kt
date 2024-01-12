package id.nexus.farmap.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import id.nexus.farmap.helper.navigation.Map
import id.nexus.farmap.ui.theme.FARMapTheme

class MainUI : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FARMapTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    FARMapScreen()
                }
            }
        }
    }

    companion object {
        val mapDB = Firebase.firestore.collection("maps")
        lateinit var map: Map
        var adminMode: Boolean = false
        var sourceDB = Source.DEFAULT
    }
}

@Preview
@Composable
fun AppPreview(){
    FARMapTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            FARMapScreen()
        }
    }
}