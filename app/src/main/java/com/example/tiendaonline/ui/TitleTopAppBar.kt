package com.example.tiendaonline.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.tiendaonline.ui.theme.Blue500
import com.example.tiendaonline.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TitleTopAppBar(
    title: String,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        title = { Text(text = title, color = White) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Blue500
        ),
        actions = actions
    )
}
