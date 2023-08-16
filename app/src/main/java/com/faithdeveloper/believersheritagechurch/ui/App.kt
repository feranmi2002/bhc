package com.faithdeveloper.believersheritagechurch.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.faithdeveloper.believersheritagechurch.data.AppContainer
import com.faithdeveloper.believersheritagechurch.ui.theme.BHCTheme

@Composable
fun App(appContainer: AppContainer) {
    BHCTheme {
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AppNavGraph(
                appContainer = appContainer,
                modifier = Modifier,
                navController = navController
            )
        }
    }
}