package com.nanit.websocket.app.ui.composables.birthday_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.request.transformations
import coil3.size.Precision
import coil3.size.Scale
import coil3.size.Size
import coil3.transform.CircleCropTransformation
import com.nanit.websocket.app.ui.ThemedResources

@Composable
fun BabyImageSection(modifier :Modifier, theme: ThemedResources, imagePath : String?,onCameraClick: () -> Unit) {
    val density = LocalDensity.current
    var imageSize by remember { mutableStateOf(0.dp) }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier.aspectRatio(1f).padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            //Default Background place holder for the image
            Image(
                painter = painterResource(id = theme.placeHolderIcon), // Replace with actual drawable
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
            // Set baby picture if exists
            if(!imagePath.isNullOrEmpty()) {
                val context = LocalContext.current
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(context)
                        .data(imagePath)
                        .precision(Precision.INEXACT)
                        .size(Size.ORIGINAL)
                        .scale(Scale.FILL)
                        .crossfade(true)
                        .transformations(CircleCropTransformation())
                        .build()
                )
                // Baby image
                Image(
                    painter = painter, // Replace with actual drawable
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(7.dp).clip(CircleShape)
                )
            }

            // Place icon around the placeholder with defined rotation
            val cameraIconClockWiseRotation = 45f
            Box(
                modifier = Modifier.fillMaxSize().rotate(cameraIconClockWiseRotation),
                contentAlignment = Alignment.TopCenter
            ) {
                // Camera Icon at 45 degrees
                Image(
                    painter = painterResource(id = theme.cameraIcon),
                    contentDescription = "Camera button",
                    modifier = Modifier
                        .fillMaxHeight(0.15f)
                        .aspectRatio(1f)
                        .onGloballyPositioned { coordinates ->
                            imageSize = with(density) { coordinates.size.height.toDp() / 2.5f }
                        }
                        .offset {IntOffset(imageSize.roundToPx(), -imageSize.roundToPx()) }
                        .rotate(-cameraIconClockWiseRotation)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = false, radius = imageSize) // New ripple API
                        ) {
                            onCameraClick.invoke()
                        }
                )
            }
        }
    }
}