package com.nanit.websocket.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nanit.websocket.app.connect.ServerSocketConnectorImpl
import com.nanit.websocket.app.helpers.GenericViewModelFactory
import com.nanit.websocket.app.ui.ThemeManager
import com.nanit.websocket.app.ui.composables.birthday_screen.BirthdayViewScreen
import com.nanit.websocket.app.ui.theme.NanitTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val factory = GenericViewModelFactory { MainViewModel(ServerSocketConnectorImpl()) }
        setContent {
            NanitTheme {
                BirthdayApp(viewModel(factory = factory))
            }
        }
    }
}

@Composable
fun BirthdayApp(viewModel: MainViewModel) {
    val viewState by viewModel.viewState.collectAsState()
    val babyImageState by viewModel.imageState.collectAsState()

    if (viewState is ViewState.Success) {
        val info =  (viewState as ViewState.Success).info
        val themedResources = ThemeManager.getScreenTheme(info.theme)
        BirthdayViewScreen(themedResources = themedResources, birthdayInfo = info, imagePath = babyImageState, onShareButtonClick = {
            // Add handling share button click
        }, onCameraClick = {
            // Add camera button click
        })
    } else {
        ConnectionScreen(viewState, viewModel)
    }
}

@Composable
fun ConnectionScreen(viewState: ViewState, viewModel: MainViewModel) {
    var ipAddress by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (viewState == ViewState.Connecting) {
            CircularProgressIndicator()
        } else {
            // Connection UI
            TextField(
                value = ipAddress,
                onValueChange = { ipAddress = it },
                label = { Text("Server IP address") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    if (ipAddress.isNotEmpty()) {
                        viewModel.makeConnection(ipAddress)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Connect")
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        val status = when (viewState) {
            ViewState.ConnectionRequired -> "Enter server IP-address and press Connect"
            ViewState.Connecting -> "Connecting..."
            ViewState.Failed -> "Failed to connect, check your ip address and try again"
            ViewState.FailedDataUnavailable -> "No data available!"
            is ViewState.Success -> ""
        }
        Text(
            text = status,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val factory = GenericViewModelFactory { MainViewModel(ServerSocketConnectorImpl()) }
    NanitTheme {
        BirthdayApp(viewModel(factory = factory))
    }
}

