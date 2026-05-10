package ru.itis.bloom.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.itis.bloom.ui.components.settings.IconSettings
import ru.itis.bloom.ui.components.settings.TopBarSettings
import ru.itis.bloom.ui.theme.BloomTheme
import ru.itis.bloom.ui.theme.DimensionsCustom
import ru.itis.bloom.ui.theme.IconsCustom
import ru.itis.bloom.ui.theme.StylesCustom

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarCustom(
    topBarSettings: TopBarSettings,
    iconSettings: IconSettings? = null,
) {
    TopAppBar(
        title = {
            Text(
                text = topBarSettings.text,
                style = StylesCustom.topBarTitle,
                color = topBarSettings.textColor ?: MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp)
            )
        },
        navigationIcon = {
            iconSettings?.let {
                IconButton(
                    onClick = iconSettings.onClick,
                    modifier = Modifier
                        .padding(top = 8.dp, start = 16.dp)

                ) {
                    Icon(
                        imageVector = iconSettings.icon ?: IconsCustom.iconArrowBack(),
                        contentDescription = iconSettings.description,
                        tint = iconSettings.color ?: MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier
                            .height(DimensionsCustom.topBarIconSize)
                            .width(DimensionsCustom.topBarIconSize)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = topBarSettings.containerColor ?: Color.Transparent
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    )
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun TopBarCustomPreview() {
    BloomTheme {
        TopBarCustom(
            topBarSettings = TopBarSettings(
                text = "Заголовок",
            ),
            iconSettings = IconSettings()
        )
    }
}