package com.faithdeveloper.believersheritagechurch.ui.imageviewer

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageViewerScreen(
    imageLink: String,

) {
    Column(modifier = Modifier.fillMaxSize()) {
        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            alignment = Alignment.Center,
            model = imageLink, contentDescription = "Image"
        )
    }
}

