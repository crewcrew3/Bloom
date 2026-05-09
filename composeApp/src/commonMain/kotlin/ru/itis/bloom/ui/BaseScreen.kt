package ru.itis.bloom.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import ru.itis.bloom.ui.components.BottomBarCustom
import ru.itis.bloom.ui.components.TopBarCustom
import ru.itis.bloom.ui.components.settings.BottomBarSettings
import ru.itis.bloom.ui.components.settings.IconSettings
import ru.itis.bloom.ui.components.settings.TopBarSettings
import ru.itis.bloom.ui.theme.DimensionsCustom

@Composable
fun BaseScreen(
    bottomBarSettings: BottomBarSettings? = null,
    topBarSettings: TopBarSettings? = null,
    topBarIconSettings: IconSettings? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    val systemInsets = WindowInsets.systemBars
    val customInsets = WindowInsets(left = DimensionsCustom.baseInsets, right = DimensionsCustom.baseInsets)
    Scaffold(
        contentWindowInsets = systemInsets.union(customInsets),
        topBar = {
            topBarSettings?.let {
                TopBarCustom(
                    topBarSettings = topBarSettings,
                    iconSettings = topBarIconSettings,
                )
            }
        },
        bottomBar = {
            bottomBarSettings?.let {
                BottomBarCustom(
                    bottomBarSettings = bottomBarSettings,
                )
            }
        },
        content = content,
    )
}