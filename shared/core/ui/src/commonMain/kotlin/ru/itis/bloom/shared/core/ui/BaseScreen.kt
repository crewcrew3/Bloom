package ru.itis.bloom.shared.core.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.itis.bloom.shared.core.ui.components.BottomBarCustom
import ru.itis.bloom.shared.core.ui.components.FloatingActionButtonCustom
import ru.itis.bloom.shared.core.ui.components.TopBarCustom
import ru.itis.bloom.shared.core.ui.components.settings.BottomBarSettings
import ru.itis.bloom.shared.core.ui.components.settings.FloatingActionButtonSettings
import ru.itis.bloom.shared.core.ui.components.settings.IconSettings
import ru.itis.bloom.shared.core.ui.components.settings.TopBarSettings
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.IconsCustom

@Composable
fun BaseScreen(
    bottomBarSettings: BottomBarSettings? = null,
    topBarSettings: TopBarSettings? = null,
    topBarIconSettings: IconSettings? = null,
    floatActBtnSettings: FloatingActionButtonSettings? = null,
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
        floatingActionButton = {
            floatActBtnSettings?.let {
                FloatingActionButtonCustom(
                    settings = floatActBtnSettings
                )
            }
        },
        content = content,
    )
}