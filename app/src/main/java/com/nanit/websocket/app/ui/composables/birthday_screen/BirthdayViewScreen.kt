package com.nanit.websocket.app.ui.composables.birthday_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.nanit.websocket.app.R
import com.nanit.websocket.app.utils.Utils
import com.nanit.websocket.app.data.BirthdayInfo
import com.nanit.websocket.app.ui.ThemedResources

@Composable
fun BirthdayViewScreen(themedResources: ThemedResources ,birthdayInfo: BirthdayInfo,imagePath:String,onShareButtonClick: () -> Unit,onCameraClick: () -> Unit) {
    val topSectionFractionPercent = 0.37f
    val midSectionFractionPercent = 0.35f
    val bottomSectionFractionPercent = 0.28f

    val age = Utils.getAge(birthdayInfo.dob)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(themedResources.bgColorRes))
    ) {

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(topSectionFractionPercent))
            BabyImageSection(modifier = Modifier
                .fillMaxWidth()
                .weight(midSectionFractionPercent),
                themedResources,
                imagePath,
                onCameraClick
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(bottomSectionFractionPercent))
        }
        // Background image
        Image(
            painter = painterResource(id = themedResources.bgLayer),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(modifier = Modifier.fillMaxSize()) {

            val text = when {
                age.isMonths -> stringResource(R.string.month_old_text)
                age.number > 1 -> stringResource(R.string.years_old_text)
                else -> stringResource(R.string.year_old_text)
            }
            // Top view
            AgeNameSection(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(topSectionFractionPercent),
                themedResources,
                age.number,
                birthdayInfo.name ?:"",
                text
            )
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(midSectionFractionPercent))
            // Bottom view
            ShareNewsSection(modifier = Modifier
                .fillMaxWidth()
                .weight(bottomSectionFractionPercent),
                onShareButtonClick = onShareButtonClick)
        }
    }
}