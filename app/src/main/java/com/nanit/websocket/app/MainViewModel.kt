package com.nanit.websocket.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanit.websocket.app.connect.ServerSocketConnector
import com.nanit.websocket.app.connect.Status
import com.nanit.websocket.app.data.BirthdayInfo
import com.nanit.websocket.app.data.ThemeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val serverConnector: ServerSocketConnector) : ViewModel() {

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Success(info = BirthdayInfo("Bubu",1735574091000L,ThemeType.ELEPHANT)))
    // private val _viewState = MutableStateFlow<ViewState>(ViewState.ConnectionRequired)
    private val _imageState = MutableStateFlow("https://fastly.picsum.photos/id/237/400/300.jpg?hmac=32RuLp2fb9I2fzPP3U-6REXQ6sZAbN8ML7_dt3R7wQ8")

    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()
    val imageState: StateFlow<String> = _imageState.asStateFlow()

    fun makeConnection(ipAddress: String) {
        viewModelScope.launch {
            _viewState.value = ViewState.Connecting
            serverConnector.connectToServer(ipAddress) { result ->
               when(result.status) {
                   Status.SUCCESS -> {
                        if(result.info == null) {
                            _viewState.value = ViewState.FailedDataUnavailable
                        }else {
                            _viewState.value = ViewState.Success(info = result.info)
                        }
                   }
                   Status.FAILED -> {
                       _viewState.value = ViewState.Failed
                   }
               }
            }
        }
    }
}

sealed class ViewState {
    data object ConnectionRequired : ViewState()
    data object Connecting : ViewState()
    data object Failed : ViewState()
    data object FailedDataUnavailable : ViewState()
    data class Success(val info: BirthdayInfo) : ViewState()
}