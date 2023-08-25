package com.faithdeveloper.believersheritagechurch.ui.aboutdetails

import androidx.compose.runtime.Composable
import com.faithdeveloper.believersheritagechurch.data.about.AboutItem
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.ui.MainActivity

@Composable
fun AboutDetailsRoute(
    aboutItem: AboutItem, mainActivity: MainActivity,
    navigateToPlayingActivity: (message: Message) -> Unit
) {
    AboutDetailsScreen(aboutItem = aboutItem,
        mainActivity = mainActivity,
    navigateToPlayingActivity = {
        navigateToPlayingActivity.invoke(it)
    })
}