package com.faithdeveloper.believersheritagechurch.ui.programmes

import androidx.compose.runtime.Composable
import com.faithdeveloper.believersheritagechurch.viewmodel.ProgrammesViewModel

@Composable
fun ProgrammesRoute(
    programmesViewModel: ProgrammesViewModel
) {
    programmesViewModel.getProgrammes()
    ProgrammesScreen(
        programmesViewModel = programmesViewModel
    ) {
        programmesViewModel.retry()
    }
}