package com.faithdeveloper.believersheritagechurch.ui.imageviewer

import androidx.compose.runtime.Composable
import com.faithdeveloper.believersheritagechurch.data.home.ImageSliderImage

@Composable
fun ImageViewerRoute(
    imageSliderImage: ImageSliderImage
) {
    ImageViewerScreen(imageLink = imageSliderImage.image_link)

}