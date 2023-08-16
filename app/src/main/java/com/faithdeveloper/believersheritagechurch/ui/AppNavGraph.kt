package com.faithdeveloper.believersheritagechurch.ui

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.faithdeveloper.believersheritagechurch.data.AppContainer
import com.faithdeveloper.believersheritagechurch.data.about.AboutItem
import com.faithdeveloper.believersheritagechurch.data.announcement.Announcement
import com.faithdeveloper.believersheritagechurch.data.home.ImageSliderImage
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.navtype.AboutNavType
import com.faithdeveloper.believersheritagechurch.navtype.AnnouncementNavType
import com.faithdeveloper.believersheritagechurch.navtype.ImageSliderNavType
import com.faithdeveloper.believersheritagechurch.navtype.MessageNavType
import com.faithdeveloper.believersheritagechurch.ui.about.AboutListScreenRoute
import com.faithdeveloper.believersheritagechurch.ui.aboutdetails.AboutDetailsRoute
import com.faithdeveloper.believersheritagechurch.ui.announcementdetails.AnnouncementDetailRoute
import com.faithdeveloper.believersheritagechurch.ui.announcements.AnnouncementRoute
import com.faithdeveloper.believersheritagechurch.ui.home.HomeRoute
import com.faithdeveloper.believersheritagechurch.ui.imageviewer.ImageViewerRoute
import com.faithdeveloper.believersheritagechurch.ui.messages.MessagesRoute
import com.faithdeveloper.believersheritagechurch.ui.messages_section.MessagesSectionRoute
import com.faithdeveloper.believersheritagechurch.ui.playing.PlayingRoute
import com.faithdeveloper.believersheritagechurch.ui.programmes.ProgrammesRoute
import com.faithdeveloper.believersheritagechurch.viewmodel.*

@Composable
fun AppNavGraph(
    appContainer: AppContainer,
    modifier: Modifier,
    navController: NavHostController,
    startDestination: String = AppDestinations.Home.route
) {
    var loadScreen = false

    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier
    ) {


        composable(AppDestinations.Home.route) {
            val homeViewModel: HomeViewModel = viewModel(
                factory = HomeViewModel.provideFactory(appContainer.homeRepository)
            )

            HomeRoute(homeViewModel = homeViewModel, onClickSliderImage = { imageSliderImage ->
                AppNavigationActions.navigateToImageViewer(navController, imageSliderImage)
            }, navigateTo = {
                when (it) {
                    AppDestinations.MessagesSection -> {
                        AppNavigationActions.navigateToMessagesSection(navController)
                    }
                    AppDestinations.About -> {
                        AppNavigationActions.navigateToAbout(navController)
                    }
                    AppDestinations.Announcements -> {
                        AppNavigationActions.navigateToAnnouncement(navController)
                    }
                    AppDestinations.Programmes -> {
                        AppNavigationActions.navigateToProgrammes(navController)
                    }
                    else -> {
//                         do nothing
                    }
                }
            })
        }

        composable(AppDestinations.Programmes.route) {
            val programmesViewModel: ProgrammesViewModel = viewModel(
                factory = ProgrammesViewModel.provideFactory(appContainer.programmesRepository)
            )

            ProgrammesRoute(programmesViewModel = programmesViewModel)
        }

        composable(AppDestinations.MessagesSection.route) {
            val messageSectionViewModel: MessageSectionViewModel = viewModel(
                factory =
                MessageSectionViewModel.provideFactory(appContainer.messagesRepository)
            )

            MessagesSectionRoute(
                messageSectionViewModel = messageSectionViewModel
            ) { messageType ->
                AppNavigationActions.navigateToMessages(navController, messageType)
            }
        }

        composable(
            AppDestinations.Messages.route + "/{$MESSAGE_SECTION_ARG}",
            arguments = listOf(navArgument(MESSAGE_SECTION_ARG) {
                type = NavType.StringType
            })
        ) { navBackStackEntry ->
            val messageViewModel: MessageViewModel = viewModel(
                factory = MessageViewModel.provideFactory(
                    appContainer.messagesRepository, navBackStackEntry.arguments?.getString(
                        MESSAGE_SECTION_ARG
                    )!!
                )
            )
            MessagesRoute(
                messageViewModel = messageViewModel
            ) { message ->
                loadScreen = true
                AppNavigationActions.navigateToPlayingMessage(navController, message)
            }
        }

        composable(
            AppDestinations.Playing.route + "/{$MESSAGE_DATA}",
            arguments = listOf(navArgument(MESSAGE_DATA) {
                type = MessageNavType()
            })
        ) { navBackStackEntry ->
            val playingViewModel: PlayingViewModel = viewModel(
                factory = PlayingViewModel.provideFactory(
                    appContainer.playingRepository,
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
                        navBackStackEntry.arguments?.getParcelable(MESSAGE_DATA)!!
                    else navBackStackEntry.arguments?.getParcelable(
                        MESSAGE_DATA,
                        Message::class.java
                    )!!,
                    appContainer.downloadRepository
                )
            )
            if (loadScreen) {
                PlayingRoute(
                    onClickBack = {
                        loadScreen = false
                        AppNavigationActions.clickBack(navController)
                    }, playingViewModel = playingViewModel
                )
            }
        }

        composable(AppDestinations.Announcements.route) { navBackStackEntry ->
            val announcementViewModel: AnnouncementViewModel = viewModel(
                factory = AnnouncementViewModel.provideFactory(appContainer.announcementRepository)
            )
            AnnouncementRoute(announcementViewModel = announcementViewModel) { announcement ->
                AppNavigationActions.navigateToAnnouncementDetails(navController, announcement)
            }
        }

        composable(
            AppDestinations.AnnouncementDetail.route + "/{$ANNOUNCEMENT_DETAIL_ARG}",
            arguments = listOf(navArgument(ANNOUNCEMENT_DETAIL_ARG) {
                type = AnnouncementNavType()
            })
        ) { navBackStackEntry ->
            AnnouncementDetailRoute(
                announcement = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
                    navBackStackEntry.arguments?.getParcelable(ANNOUNCEMENT_DETAIL_ARG)!!
                else navBackStackEntry.arguments?.getParcelable(
                    ANNOUNCEMENT_DETAIL_ARG,
                    Announcement::class.java
                )!!
            )
        }

        composable(AppDestinations.About.route) { navBackStackEntry ->
            val aboutViewModel: AboutViewModel = viewModel(
                factory = AboutViewModel.provideFactory(appContainer.aboutRepository)
            )
            AboutListScreenRoute(aboutViewModel = aboutViewModel) { aboutItem ->
                AppNavigationActions.navigateToAboutDetails(navController, aboutItem)
            }
        }

        composable(AppDestinations.AboutDetails.route + "/{$ABOUT_ITEM_ARG}",
            arguments = listOf(navArgument(ABOUT_ITEM_ARG) {
                type = AboutNavType()
            }
            )) { navBackStackEntry ->
            AboutDetailsRoute(
                aboutItem = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
                    navBackStackEntry.arguments?.getParcelable(ABOUT_ITEM_ARG)!!
                else navBackStackEntry.arguments?.getParcelable(
                    ABOUT_ITEM_ARG,
                    AboutItem::class.java
                )!!
            )
        }

        composable(
            AppDestinations.ImageViewer.route + "/{$IMAGE_SLIDER_IMAGE}",
            arguments = listOf(navArgument(IMAGE_SLIDER_IMAGE) {
                type = ImageSliderNavType()
            })
        ) { navBackStackEntry ->
            ImageViewerRoute(
                imageSliderImage = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU)
                    navBackStackEntry.arguments?.getParcelable(IMAGE_SLIDER_IMAGE)!!
                else navBackStackEntry.arguments?.getParcelable(
                    IMAGE_SLIDER_IMAGE,
                    ImageSliderImage::class.java
                )!!
            )
        }
    }
}

const val MESSAGE_SECTION_ARG = "sectionTitle"
const val MESSAGE_DATA = "message_data"
const val ANNOUNCEMENT_DETAIL_ARG = "announcement_detail"
const val ABOUT_ITEM_ARG = "about_item"
const val IMAGE_SLIDER_IMAGE = "image_slider_image"