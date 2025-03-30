package com.nanit.websocket.app.ui.composables.birthday_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nanit.websocket.app.R

@Composable
fun ShareNewsSection(modifier: Modifier, onShareButtonClick: () -> Unit) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top

    ) {
        Image(
            painter = painterResource(id = R.drawable.logo), // Replace with actual image
            contentDescription = null,
            modifier = Modifier.height(40.dp).padding(top = 16.dp)
        )
        Column(modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center) {
            Button(
                onClick = { onShareButtonClick.invoke() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.share_button_color)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    modifier = Modifier.padding(end = 4.dp),
                    text = stringResource(R.string.share_button_title),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                Icon(
                    painter = painterResource(R.drawable.ic_share),
                    contentDescription = "Share",
                    tint = Color.White,
                    modifier = Modifier
                        .size(30.dp)
                        .padding(start = 4.dp, bottom = 4.dp)
                )
            }
        }
    }
}