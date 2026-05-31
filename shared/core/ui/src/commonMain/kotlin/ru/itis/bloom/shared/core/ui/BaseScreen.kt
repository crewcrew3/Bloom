package ru.itis.bloom.shared.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bloom.shared.core.ui.generated.resources.*
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.components.BottomBarCustom
import ru.itis.bloom.shared.core.ui.components.DrawerItem
import ru.itis.bloom.shared.core.ui.components.FloatingActionButtonCustom
import ru.itis.bloom.shared.core.ui.components.TopBarCustom
import ru.itis.bloom.shared.core.ui.components.settings.BottomBarSettings
import ru.itis.bloom.shared.core.ui.components.settings.BurgerMenuSettings
import ru.itis.bloom.shared.core.ui.components.settings.DrawerMenuItem
import ru.itis.bloom.shared.core.ui.components.settings.FloatingActionButtonSettings
import ru.itis.bloom.shared.core.ui.components.settings.TopBarIconType
import ru.itis.bloom.shared.core.ui.components.settings.TopBarSettings
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom

@Composable
fun BaseScreen(
    bottomBarSettings: BottomBarSettings? = null,
    topBarSettings: TopBarSettings? = null,
    burgerMenuSettings: BurgerMenuSettings? = null,
    floatActBtnSettings: FloatingActionButtonSettings? = null,
    content: @Composable (PaddingValues) -> Unit,
) {
    val systemInsets = WindowInsets.systemBars
    val customInsets = WindowInsets(left = DimensionsCustom.baseInsets, right = DimensionsCustom.baseInsets)

    if (burgerMenuSettings != null) {
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()

        val openDrawer = { scope.launch { drawerState.open() } }
        val closeDrawer = { scope.launch { drawerState.close() } }

        val effectiveTopBarSettings = topBarSettings?.let { settings ->
            if (settings.iconType == TopBarIconType.BURGER) {
                settings.copy(onIconClick = { openDrawer() })
            } else {
                settings // Для BACK или NONE оставляем как есть (клик обрабатывается снаружи)
            }
        }

        val menuItems = listOf(
            DrawerMenuItem(
                icon = IconsCustom.iconRoutine(),
                titleRes = Res.string.navigation_section_text_routine,
                onClick = {
                    burgerMenuSettings.onRoutineClick()
                    closeDrawer()
                }
            ),
            DrawerMenuItem(
                icon = IconsCustom.iconSkinDiary(),
                titleRes = Res.string.navigation_section_text_skin_diary,
                onClick = {
                    burgerMenuSettings.onSkinDiaryClick()
                    closeDrawer()
                }
            ),
            DrawerMenuItem(
                icon = IconsCustom.iconMakeupBag(),
                titleRes = Res.string.navigation_section_text_makeup_bag,
                onClick = {
                    burgerMenuSettings.onMakeupBagClick()
                    closeDrawer()
                }
            ),
            DrawerMenuItem(
                icon = IconsCustom.iconProfile(),
                titleRes = Res.string.navigation_section_text_profile,
                onClick = {
                    burgerMenuSettings.onProfileClick()
                    closeDrawer()
                }
            )
        )

        ModalNavigationDrawer(
            drawerState = drawerState,
            scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = 0.3f),
            drawerContent = {
                Surface(
                    modifier = Modifier
                        .width(DimensionsCustom.menuWidth) // Ширина меню
                        .fillMaxHeight(),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    tonalElevation = 4.dp,
                    shadowElevation = 4.dp
                ) {
                    LazyColumn (
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        item {
                            Text(
                                text = stringResource(Res.string.navigation_burger_title),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
                            )
                        }
                        items(menuItems) { item ->
                            DrawerItem(
                                icon = item.icon,
                                text = stringResource(item.titleRes),
                                onClick = item.onClick
                            )
                        }
                    }
                }
            },
            gesturesEnabled = true
        ) {
            Scaffold(
                contentWindowInsets = systemInsets.union(customInsets),
                topBar = { effectiveTopBarSettings?.let { TopBarCustom(topBarSettings = it) } },
                bottomBar = { bottomBarSettings?.let { BottomBarCustom(bottomBarSettings = it) } },
                floatingActionButton = { floatActBtnSettings?.let { FloatingActionButtonCustom(settings = it) } },
                content = content,
            )
        }
    } else {
        Scaffold(
            contentWindowInsets = systemInsets.union(customInsets),
            topBar = { topBarSettings?.let { TopBarCustom(topBarSettings = it) } },
            bottomBar = { bottomBarSettings?.let { BottomBarCustom(bottomBarSettings = it) } },
            floatingActionButton = { floatActBtnSettings?.let { FloatingActionButtonCustom(settings = it) } },
            content = content,
        )
    }
}