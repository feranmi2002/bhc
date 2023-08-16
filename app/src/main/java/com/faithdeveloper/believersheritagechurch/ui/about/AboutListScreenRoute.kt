package com.faithdeveloper.believersheritagechurch.ui.about

import androidx.compose.runtime.Composable
import com.faithdeveloper.believersheritagechurch.data.about.AboutItem
import com.faithdeveloper.believersheritagechurch.viewmodel.AboutViewModel

@Composable
fun AboutListScreenRoute(
    aboutViewModel: AboutViewModel,
    onClick: (aboutItem: AboutItem) -> Unit,
) {
    aboutViewModel.getAboutList()
    AboutListScreen(aboutViewModel = aboutViewModel, itemClick = {
        onClick.invoke(it)
    }, retry = {
        aboutViewModel.retry()
    })
}