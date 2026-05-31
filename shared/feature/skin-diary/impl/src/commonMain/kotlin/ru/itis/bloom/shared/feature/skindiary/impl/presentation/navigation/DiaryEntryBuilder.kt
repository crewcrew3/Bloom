package ru.itis.bloom.shared.feature.skindiary.impl.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.github.tbib.compose_toast.AdvToast
import io.github.tbib.compose_toast.rememberAdvToastStates
import io.github.tbib.compose_toast.toast_ui.EnumToastType
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import ru.itis.bloom.shared.core.navigation.api.BottomBarNavigator
import ru.itis.bloom.shared.core.ui.components.settings.BottomBarSettings
import ru.itis.bloom.shared.feature.skindiary.api.navigation.DiaryNavRoute
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.mvi.DiaryListEffect
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.mvi.DiaryListViewModel
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.DiaryListScreen

fun EntryProviderScope<NavKey>.diaryEntryBuilder() {
    entry<DiaryNavRoute.List> {
        val vm: DiaryListViewModel = koinViewModel()
        val navigationHandler: DiaryNavigationHandler = koinInject()
        val bottomBarNav: BottomBarNavigator = koinInject()

        val state by vm.state.collectAsState()
        val scope = rememberCoroutineScope()

        // Toast для ошибок
        val stateToast = rememberAdvToastStates()
        AdvToast.MakeToast(
            state = stateToast,
            toastType = EnumToastType.ERROR,
            paddingBottom = 50
        )

        LaunchedEffect(vm) {
            vm.effects.collect { effect ->
                when (effect) {
                    is DiaryListEffect.NavigateToDetail,
                    DiaryListEffect.NavigateToCreate -> {
                        navigationHandler.handleEffect(effect)
                    }
                    is DiaryListEffect.ShowError -> {
                        scope.launch {
                            val text = getString(effect.messageRes)
                            stateToast.show(text)
                        }
                    }
                }
            }
        }

        // UI
        DiaryListScreen(
            state = state,
            onIntent = vm::onIntent,
            bottomBarSettings = BottomBarSettings(
                onRoutineSectionClick = bottomBarNav::toRoutineSection,
                onSkinDiarySectionClick = bottomBarNav::toSkinDiarySection,
                onMakeupBagSectionClick = bottomBarNav::toMakeupBagSection,
                onProfileSectionClick = bottomBarNav::toProfileSection,
            )
        )
    }

    entry<DiaryNavRoute.Detail> { /* TODO */ }
    entry<DiaryNavRoute.Create> { /* TODO */ }
    entry<DiaryNavRoute.Edit> { /* TODO */ }
}