package com.faithdeveloper.believersheritagechurch.ui.home

import androidx.compose.runtime.Composable
import com.faithdeveloper.believersheritagechurch.data.home.ImageSliderImage
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.ui.AppDestinations
import com.faithdeveloper.believersheritagechurch.ui.MainActivity
import com.faithdeveloper.believersheritagechurch.viewmodel.HomeViewModel

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel,
    navigateTo: (destination: AppDestinations) -> Unit,
    onClickSliderImage: (imageSliderImage:ImageSliderImage) -> Unit,
    mainActivity: MainActivity,
    navigateToPlayingActivity:(message: Message) -> Unit
) {
    homeViewModel.getImageSliderImages()
    HomeScreen(
        homeViewModel = homeViewModel,
        navigateTo = navigateTo,
        onClickSliderImage = { imageSliderImage ->
            onClickSliderImage.invoke(imageSliderImage)
        },
        retryLoadImages = {
            homeViewModel.retry()
        },
        mainActivity = mainActivity,
        navigateToPlayingActivity = {
            navigateToPlayingActivity.invoke(it)
        }
    )
}