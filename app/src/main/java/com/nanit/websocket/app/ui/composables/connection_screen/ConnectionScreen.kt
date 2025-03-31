package com.nanit.websocket.app.ui.composables.connection_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nanit.websocket.app.MainViewModel
import com.nanit.websocket.app.R
import com.nanit.websocket.app.ViewState

@Composable
fun ConnectionScreen(viewState: ViewState, viewModel: MainViewModel,prevIp: String) {
    var ipAddress by remember { mutableStateOf("") }
    
    LaunchedEffect (prevIp){
        ipAddress = prevIp
    }

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
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() || it == '.' }) { // Allows only numbers and dots for IP format
                        ipAddress = newValue
                    }
                },
                label = { Text(stringResource(R.string.ip_hint_title)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
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
                Text(stringResource(R.string.connect_button_title))
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        val status = when (viewState) {
            ViewState.ConnectionRequired -> stringResource(R.string.enter_ip_address_title)
            ViewState.Connecting -> stringResource(R.string.connecting_title)
            ViewState.Failed -> stringResource(R.string.failed_to_connect_title)
            ViewState.FailedDataUnavailable -> stringResource(R.string.no_data_title)
            is ViewState.Success -> ""
        }

        Text(
            text = status,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}