package com.faithdeveloper.believersheritagechurch.ui.aboutdetails

import android.text.Html
import android.util.TypedValue
import android.widget.TextView
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.faithdeveloper.believersheritagechurch.R
import com.faithdeveloper.believersheritagechurch.data.about.AboutItem
import com.faithdeveloper.believersheritagechurch.ui.messages_section.ReusableTop

@Composable
fun AboutDetailsScreen(aboutItem: AboutItem) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            ReusableTop(title = aboutItem.title)
        }
        items(aboutItem.imagesList) { imageLink ->
            ImageRow(imageLink = imageLink)
        }
        item {
            AndroidView(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp)
                .wrapContentHeight(), factory = {
                TextView(it)
            }, update = {
                it.typeface = ResourcesCompat.getFont(it.context, R.font.nunito_semi_bold)
                it.text = Html.fromHtml(aboutItem.description, Html.FROM_HTML_MODE_COMPACT)
                it.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
            })
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageRow(imageLink: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(align = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        GlideImage(
            modifier = Modifier
                .width(250.dp)
                .height(250.dp)
                .clip(shape = RoundedCornerShape(16.dp)),
            model = imageLink,
            contentScale = ContentScale.FillBounds,
            contentDescription = null
        )
    }
}