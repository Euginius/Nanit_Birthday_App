package com.nanit.websocket.app

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nanit.websocket.app.connect.ServerSocketConnectorImpl
import com.nanit.websocket.app.data.IpStore
import com.nanit.websocket.app.data.IpStoreImpl
import com.nanit.websocket.app.helpers.GenericViewModelFactory
import com.nanit.websocket.app.helpers.ImageStorage
import com.nanit.websocket.app.helpers.PictureManager
import com.nanit.websocket.app.ui.ThemeManager
import com.nanit.websocket.app.ui.composables.birthday_screen.BirthdayViewScreen
import com.nanit.websocket.app.ui.composables.connection_screen.ConnectionScreen
import com.nanit.websocket.app.ui.theme.NanitTheme
import com.nanit.websocket.app.utils.ShareUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class MainActivity : ComponentActivity() {

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { uri ->
            viewModel?.saveImage(uri)
        }
    }

    private val dataStore by preferencesDataStore(name = "app_store")
    private var viewModel : MainViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val factory = GenericViewModelFactory { MainViewModel(ServerSocketConnectorImpl(),PictureManager( this,dataStore),IpStoreImpl(dataStore)) }

        setContent {
            NanitTheme {
                viewModel = viewModel(factory = factory)
                BirthdayApp(viewModel!!,pickImageLauncher)
            }
        }
    }
}

@Composable
fun BirthdayApp(viewModel: MainViewModel, pickImageLauncher: ActivityResultLauncher<String>?) {
    val context = LocalContext.current
    val view = LocalView.current

    val viewState by viewModel.viewState.collectAsState()
    val babyImageState by viewModel.imageState.collectAsState()
    val lastValidIp by viewModel.lastIpState.collectAsState()

    if (viewState is ViewState.Success) {
        val info =  (viewState as ViewState.Success).info
        val themedResources = ThemeManager.getScreenTheme(info.theme)
        BirthdayViewScreen(themedResources = themedResources, birthdayInfo = info, imagePath = babyImageState, onShareButtonClick = {
            ShareUtils.shareView(context,view)
        }, onCameraClick = {
            pickImageLauncher?.launch("image/*")
        })
    } else {
        ConnectionScreen(viewState, viewModel,lastValidIp)
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

    class MockImageStore : ImageStorage{
        override fun addImage(imageUri: Uri): String? {
            return null
        }

        override fun removeImage(): Boolean {
            return false
        }

        override fun getImage(): String {
            return "https://fastly.picsum.photos/id/237/400/300.jpg?hmac=32RuLp2fb9I2fzPP3U-6REXQ6sZAbN8ML7_dt3R7wQ8"
        }
    }

    class IpImageStore : IpStore{
        override fun getLastUsedIp(): Flow<String?> = flow {
            emit("192.168.0.170")
        }

        override suspend fun saveLastUsedIp(ip: String) {

        }

        override suspend fun clearLastUsedIp() {

        }
    }

    val factory = GenericViewModelFactory { MainViewModel(ServerSocketConnectorImpl(),MockImageStore(),IpImageStore())}
    NanitTheme {
        BirthdayApp(viewModel(factory = factory), null)
    }
}



