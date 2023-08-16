package com.faithdeveloper.believersheritagechurch.ui.splash_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun SplashScreen(modifier:Modifier = Modifier ) {
    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background) ,
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "Believers' Heritage Church", modifier = Modifier.padding(24.dp),
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = TextUnit(30F, TextUnitType.Sp),
        textAlign = TextAlign.Center)
    }

}