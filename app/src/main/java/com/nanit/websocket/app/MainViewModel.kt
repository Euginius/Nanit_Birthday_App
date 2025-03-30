package com.nanit.websocket.app

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nanit.websocket.app.connect.ServerSocketConnector
import com.nanit.websocket.app.connect.Status
import com.nanit.websocket.app.data.BirthdayInfo
import com.nanit.websocket.app.data.IpStore
import com.nanit.websocket.app.helpers.ImageStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val serverConnector: ServerSocketConnector, private val imageStore: ImageStorage,private val ipStore: IpStore) : ViewModel() {

    private val _viewState = MutableStateFlow<ViewState>(ViewState.ConnectionRequired)
    val viewState: StateFlow<ViewState> = _viewState.asStateFlow()

    private val _imageState = MutableStateFlow("")
    val imageState: StateFlow<String> = _imageState.asStateFlow()

    private val _lastIpState = MutableStateFlow("")
    val lastIpState: StateFlow<String> = _lastIpState.asStateFlow()

    init {
        viewModelScope.launch {
            ipStore.getLastUsedIp().collect {
                it?.let {
                    _lastIpState.emit(it)
                }
            }
        }
    }

    fun makeConnection(ipAddress: String) {
        viewModelScope.launch {
            _viewState.value = ViewState.Connecting
            serverConnector.connectToServer(ipAddress) { result ->
               when(result.status) {
                   Status.SUCCESS -> {
                        if(result.info == null) {
                            _viewState.value = ViewState.FailedDataUnavailable
                        }else {
                            saveLastSuccessfulIpConnected(ipAddress)
                            _viewState.value = ViewState.Success(info = result.info)
                            loadImage()
                        }
                   }
                   Status.FAILED -> {
                       _viewState.value = ViewState.Failed
                   }
               }
            }
        }
    }

    private fun saveLastSuccessfulIpConnected(ip: String) {
        viewModelScope.launch {  ipStore.saveLastUsedIp(ip) }
    }

    private fun loadImage() {
        viewModelScope.launch {
            val savedImage =  imageStore.getImage()
            savedImage?.let {  _imageState.value = savedImage }
        }
    }

    fun saveImage(uri: Uri) {
        viewModelScope.launch {
            val savedImage =  imageStore.addImage(uri)
            savedImage?.let {  _imageState.value = savedImage }
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