package com.faithdeveloper.believersheritagechurch.ui.about

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.faithdeveloper.believersheritagechurch.data.about.AboutItem
import com.faithdeveloper.believersheritagechurch.ui.messages_section.ReusableTop
import com.faithdeveloper.believersheritagechurch.utils.Status
import com.faithdeveloper.believersheritagechurch.viewmodel.AboutViewModel

@Composable
fun AboutListScreen(
    aboutViewModel: AboutViewModel,
    itemClick: (aboutItem: AboutItem) -> Unit,
    retry: () -> Unit
) {
    val aboutItems by aboutViewModel.aboutList.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        ReusableTop(title = "About")
        when (aboutItems.type) {
            Status.SUCCESS -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(aboutItems.data) { aboutItem ->
                        AboutListItemRow(aboutItem = aboutItem, itemClick = {
                            itemClick.invoke(aboutItem)
                        })
                    }
                }
            }

            Status.LOADING -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            Status.ERROR -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "No Internet Connection", color = MaterialTheme.colorScheme.error)
                    Button(
                        onClick = { retry.invoke() },
                        modifier = Modifier.wrapContentSize(align = Alignment.Center)
                    ) {
                        Text(text = "RETRY")
                    }
                }
            }

        }

    }
}

@Composable
fun AboutListItemRow(aboutItem: AboutItem, itemClick: (aboutItem: AboutItem) -> Unit) {

    Row(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight(align = Alignment.CenterVertically)
        .padding(8.dp)
        .clickable {
            itemClick.invoke(aboutItem)
        }
    ) {
        Text(
            modifier = Modifier.clickable { itemClick.invoke(aboutItem) },
            text = aboutItem.title,
            style = MaterialTheme.typography.titleMedium
        )
    }
}