package com.nanit.websocket.app.ui.composables.birthday_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nanit.websocket.app.R
import com.nanit.websocket.app.ui.ThemedResources

@Composable
fun AgeNameSection(modifier: Modifier, theme: ThemedResources, ageNum: Int,name: String,dateText: String) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Age Text Section
        Column(
            modifier = Modifier.fillMaxHeight(0.65f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.name_text, name),
                textAlign = TextAlign.Center,
                color = Color.Black,
                style = MaterialTheme.typography.labelLarge,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = theme.getDrawableResourceForNum(ageNum)),
                    contentDescription = null,
                    modifier = Modifier.fillMaxHeight().fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(12.dp))


            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = dateText,
                style = MaterialTheme.typography.labelLarge,
                fontSize = 22.sp
            )
        }
    }
}