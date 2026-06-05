package ru.itis.bloom.shared.feature.profile.impl.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import bloom.shared.feature.profile.impl.generated.resources.*
import io.github.tbib.compose_toast.AdvToast
import io.github.tbib.compose_toast.rememberAdvToastStates
import io.github.tbib.compose_toast.toast_ui.EnumToastType
import io.github.the_best_is_best.toast_kmp.KMPNativeShowToast
import io.github.the_best_is_best.toast_kmp.KMPNativeToastType
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import ru.itis.bloom.shared.core.navigation.api.BottomBarNavigator
import ru.itis.bloom.shared.core.navigation.api.BurgerMenuNavigator
import ru.itis.bloom.shared.core.ui.components.settings.TopBarIconType
import ru.itis.bloom.shared.core.ui.utils.provideBottomBarSettings
import ru.itis.bloom.shared.core.ui.utils.provideBurgerMenuSettings
import ru.itis.bloom.shared.core.ui.utils.provideTopBarSettings
import ru.itis.bloom.shared.core.ui.utils.useNativeToast
import ru.itis.bloom.shared.feature.profile.api.navigation.ProfileNavRoute
import ru.itis.bloom.shared.feature.profile.impl.mvi.ProfileEffect
import ru.itis.bloom.shared.feature.profile.impl.mvi.ProfileIntent
import ru.itis.bloom.shared.feature.profile.impl.mvi.ProfileViewModel
import ru.itis.bloom.shared.feature.profile.impl.presentation.ProfileScreen

fun EntryProviderScope<NavKey>.profileEntryBuilder() {
    entry<ProfileNavRoute.Profile> {
        val vm: ProfileViewModel = koinViewModel()
        val navigationHandler: ProfileNavigationHandler = koinInject()
        val bottomBarNav: BottomBarNavigator = koinInject()
        val burgerMenuNav: BurgerMenuNavigator = koinInject()
        val useNativeToast = useNativeToast()

        val state by vm.state.collectAsState()
        val scope = rememberCoroutineScope()

        val stateToast = rememberAdvToastStates()
        AdvToast.MakeToast(
            state = stateToast,
            toastType = EnumToastType.INFO,
            paddingBottom = 50
        )

        LaunchedEffect(Unit) {
            vm.processIntent(ProfileIntent.LoadProfile)
        }

        LaunchedEffect(vm) {
            vm.effect.collect { effect ->
                when (effect) {
                    is ProfileEffect.NavigateToLogin-> {
                        navigationHandler.handleEffect(effect)
                    }
                    is ProfileEffect.ShowMessage -> {
                        scope.launch {
                            val text = getString(effect.message.toResourceId())
                            if (useNativeToast) {
                                KMPNativeShowToast.show(
                                    text,
                                    KMPNativeToastType.LONG
                                )
                            } else {
                                stateToast.show(text)
                            }
                        }
                    }
                }
            }
        }

        val bottomBarSettings = provideBottomBarSettings(
            onRoutine = bottomBarNav::toRoutineSection,
            onSkinDiary = bottomBarNav::toSkinDiarySection,
            onMakeupBag = bottomBarNav::toMakeupBagSection,
            onProfile = bottomBarNav::toProfileSection
        )

        val topBarSettings = provideTopBarSettings(
            title = stringResource(Res.string.profile_title),
            iconType = TopBarIconType.BURGER,
            onIconClick = { /* handled by BaseScreen */ }
        )

        val burgerMenuSettings = provideBurgerMenuSettings(
            onRoutine = burgerMenuNav::toRoutineSection,
            onSkinDiary = burgerMenuNav::toSkinDiarySection,
            onMakeupBag = burgerMenuNav::toMakeupBagSection,
            onProfile = burgerMenuNav::toProfileSection
        )

        ProfileScreen(
            state = state,
            onIntent = vm::processIntent,
            bottomBarSettings = bottomBarSettings,
            topBarSettings = topBarSettings,
            burgerMenuSettings = burgerMenuSettings
        )
    }
}