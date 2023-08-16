package com.faithdeveloper.believersheritagechurch.ui

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import com.faithdeveloper.believersheritagechurch.data.about.AboutItem
import com.faithdeveloper.believersheritagechurch.data.announcement.Announcement
import com.faithdeveloper.believersheritagechurch.data.home.ImageSliderImage
import com.faithdeveloper.believersheritagechurch.data.messages.Message

object AppNavigationActions {

    private fun cleanupNavGraph(
        navOptionsBuilder: NavOptionsBuilder
    ) {
        navOptionsBuilder.launchSingleTop = true
        navOptionsBuilder.restoreState = true
    }

    val navigateToProgrammes: (navController: NavHostController) -> Unit = { navController ->
        navController.navigate(
            AppDestinations.Programmes.route
        ) {
            navController.graph.startDestinationRoute?.let { route ->
                cleanupNavGraph(this)
            }
        }
    }

    val navigateToImageViewer: (navController: NavHostController, imageSliderImage: ImageSliderImage) -> Unit =
        { navController, imageSliderImage ->
            navController.navigate(AppDestinations.ImageViewer.route + "/$imageSliderImage") {
                navController.graph.startDestinationRoute?.let { route ->
                    cleanupNavGraph(this)
                }
            }
        }

    val navigateToAbout: (navController: NavHostController) -> Unit = { navController ->
        navController.navigate(
            AppDestinations.About.route
        ) {
            navController.graph.startDestinationRoute?.let { route ->
                cleanupNavGraph(this)
            }
        }
    }

    val navigateToMessagesSection: (navController: NavHostController) -> Unit = { navController ->
        navController.navigate(
            AppDestinations.MessagesSection.route
        ) {
            navController.graph.startDestinationRoute?.let { route ->
                cleanupNavGraph(this)
            }
        }
    }

    val navigateToAboutDetails: (navController: NavHostController, aboutItem: AboutItem) -> Unit =
        { navController, aboutItem ->
            navController.navigate(
                AppDestinations.AboutDetails.route + "/$aboutItem"
            ) {
                navController.graph.startDestinationRoute?.let { route ->
                    cleanupNavGraph(this)
                }
            }
        }

    val navigateToMessages: (navController: NavController, messageType: String) -> Unit =
        { navController, messageType ->
            navController.navigate(AppDestinations.Messages.route + "/$messageType") {
                navController.graph.startDestinationRoute?.let { route ->
                    cleanupNavGraph(this)
                }
            }
        }


    val navigateToPlayingMessage: (navController: NavController, message: Message) -> Unit =
        { navController, message ->
            navController.navigate(
                AppDestinations.Playing.route + "/$message"
            ) {
                navController.graph.startDestinationRoute?.let { route ->
                    cleanupNavGraph(this)
                }
            }
        }

    val navigateToAnnouncement: (navController: NavController) -> Unit =
        { navController ->
            navController.navigate(
                AppDestinations.Announcements.route
            ) {
                navController.graph.startDestinationRoute?.let { route ->
                    cleanupNavGraph(this)
                }

            }
        }

    val navigateToAnnouncementDetails: (navController: NavController, announcement: Announcement) -> Unit =
        { navController, announcement ->
            navController.navigate(AppDestinations.AnnouncementDetail.route + "/$announcement") {
                navController.graph.startDestinationRoute?.let { route ->
                    cleanupNavGraph(this)
                }
            }

        }

    val clickBack: (navController: NavController) -> Unit = { navController ->
        navController.popBackStack()
    }
}
