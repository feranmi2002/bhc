package com.faithdeveloper.believersheritagechurch.ui.programmes

import androidx.compose.runtime.Composable
import com.faithdeveloper.believersheritagechurch.data.messages.Message
import com.faithdeveloper.believersheritagechurch.ui.MainActivity
import com.faithdeveloper.believersheritagechurch.viewmodel.ProgrammesViewModel

@Composable
fun ProgrammesRoute(
    programmesViewModel: ProgrammesViewModel,
    mainActivity: MainActivity,
    navigateToPlayingActivity:(message: Message) -> Unit
) {
    programmesViewModel.getProgrammes()
    ProgrammesScreen(
        programmesViewModel = programmesViewModel,
        {
            programmesViewModel.retry()
        },
        mainActivity = mainActivity,
        navigateToPlayingActivity = {
            navigateToPlayingActivity.invoke(it)
        }
    )
}