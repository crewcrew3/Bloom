package ru.itis.bloom.shared.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bloom.shared.core.ui.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.components.settings.BottomBarSettings
import ru.itis.bloom.shared.core.ui.theme.BloomTheme
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.IconsCustom
import ru.itis.bloom.shared.core.ui.theme.StylesCustom

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
            BottomNavItem(
                icon = IconsCustom.iconRoutine(),
                label = stringResource(Res.string.navigation_section_text_routine),
                onClick = bottomBarSettings.onRoutineSectionClick
            )

            BottomNavItem(
                icon = IconsCustom.iconSkinDiary(),
                label = stringResource(Res.string.navigation_section_text_skin_diary),
                onClick = bottomBarSettings.onSkinDiarySectionClick
            )

            BottomNavItem(
                icon = IconsCustom.iconMakeupBag(),
                label = stringResource(Res.string.navigation_section_text_makeup_bag),
                onClick = bottomBarSettings.onMakeupBagSectionClick
            )

            BottomNavItem(
                icon = IconsCustom.iconProfile(),
                label = stringResource(Res.string.navigation_section_text_profile),
                onClick = bottomBarSettings.onProfileSectionClick
            )
//            IconButton(
//                onClick = bottomBarSettings.onRoutineSectionClick
//            ) {
//                Icon(
//                    painter = IconsCustom.iconRoutine(),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .size(DimensionsCustom.bottomBarIconSize)
//                )
//            }
//            IconButton(
//                onClick = bottomBarSettings.onSkinDiarySectionClick
//            ) {
//                Icon(
//                    painter = IconsCustom.iconSkinDiary(),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .size(DimensionsCustom.bottomBarIconSize)
//                )
//            }
//            IconButton(
//                onClick = bottomBarSettings.onMakeupBagSectionClick
//            ) {
//                Icon(
//                    painter = IconsCustom.iconMakeupBag(),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .size(DimensionsCustom.bottomBarIconSize)
//                )
//            }
//            IconButton(
//                onClick = bottomBarSettings.onProfileSectionClick
//            ) {
//                Icon(
//                    painter = IconsCustom.iconProfile(),
//                    contentDescription = "",
//                    modifier = Modifier
//                        .size(DimensionsCustom.bottomBarIconSize)
//                )
//            }
        }
    }
}

@Composable
private fun BottomNavItem(
    icon: Painter,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val contentColor = MaterialTheme.colorScheme.onSecondaryContainer

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp)
    ) {
        Icon(
            painter = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(DimensionsCustom.bottomBarIconSize)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = StylesCustom.bottomBarLabel,
            color = contentColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
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