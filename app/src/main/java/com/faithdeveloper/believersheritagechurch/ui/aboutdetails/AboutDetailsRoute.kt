package com.faithdeveloper.believersheritagechurch.ui.aboutdetails

import androidx.compose.runtime.Composable
import com.faithdeveloper.believersheritagechurch.data.about.AboutItem

@Composable
fun AboutDetailsRoute(aboutItem: AboutItem) {
    AboutDetailsScreen(aboutItem = aboutItem)
}