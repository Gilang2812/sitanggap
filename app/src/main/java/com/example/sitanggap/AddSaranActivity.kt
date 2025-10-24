package com.example.sitanggap

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.core.app.ActivityCompat.startActivityForResult
import com.example.sitanggap.ui.theme.SitanggapTheme


class AddSaranActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SitanggapTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopBarHeader("Aspirasi dan Saran") { } }) { innerPadding ->
                    FormSaran(innerPadding)
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    SitanggapTheme {
        AddSaranScreen()
    }
}

@Composable
fun AddSaranScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBarHeader("Aspirasi dan Saran", onClick = {}) })
    { innerPadding ->
        FormSaran(innerPadding)
    }
}

@Composable
fun FormSaran(innerPadding: PaddingValues) {
    val context = LocalContext.current
    var textFieldValue by remember { mutableStateOf("") }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(30.dp)
    ) {
        Column {
            Text(text = "Judul")
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                label = { Text("Judul") },
                maxLines = 1
            )
        }
        Column {
            Text(text = "Deskripsi")
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                label = { Text("Deskripsi") },
                minLines = 4,
                maxLines = 5
            )
        }
        Column {
            Text(text="Upload Foto ")
            Row {
                //open camera button + function
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(context as ComponentActivity, intent, 1, null)
                    }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Photo")
                    Text(text = "Camera")
                }
                Button(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp),
                    onClick = {}
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add Photo")
                    Text(text = "Gallery")
                }
                //open gallery button + function
            }
        }
    }
}

