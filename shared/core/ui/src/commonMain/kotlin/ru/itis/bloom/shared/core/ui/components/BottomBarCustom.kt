package ru.itis.bloom.shared.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.itis.bloom.shared.core.ui.components.settings.BottomBarSettings
import ru.itis.bloom.shared.core.ui.theme.BloomTheme
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.IconsCustom

@Composable
fun BottomBarCustom(
    bottomBarSettings: BottomBarSettings,
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        modifier = Modifier
            .height(DimensionsCustom.bottomBarHeight)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(
                onClick = bottomBarSettings.onRoutineSectionClick
            ) {
                Icon(
                    painter = IconsCustom.iconRoutine(),
                    contentDescription = "",
                    modifier = Modifier
                        .size(DimensionsCustom.bottomBarIconSize)
                )
            }
            IconButton(
                onClick = bottomBarSettings.onSkinDiarySectionClick
            ) {
                Icon(
                    painter = IconsCustom.iconSkinDiary(),
                    contentDescription = "",
                    modifier = Modifier
                        .size(DimensionsCustom.bottomBarIconSize)
                )
            }
            IconButton(
                onClick = bottomBarSettings.onMakeupBagSectionClick
            ) {
                Icon(
                    painter = IconsCustom.iconMakeupBag(),
                    contentDescription = "",
                    modifier = Modifier
                        .size(DimensionsCustom.bottomBarIconSize)
                )
            }
            IconButton(
                onClick = bottomBarSettings.onProfileSectionClick
            ) {
                Icon(
                    painter = IconsCustom.iconProfile(),
                    contentDescription = "",
                    modifier = Modifier
                        .size(DimensionsCustom.bottomBarIconSize)
                )
            }
        }
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
fun BottomBarCustomPreview() {
    BloomTheme {
        BottomBarCustom(
            bottomBarSettings = BottomBarSettings({}, {}, {}, {})
        )
    }
}