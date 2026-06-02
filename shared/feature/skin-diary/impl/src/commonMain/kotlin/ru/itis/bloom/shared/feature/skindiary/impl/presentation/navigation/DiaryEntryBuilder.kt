package ru.itis.bloom.shared.feature.skindiary.impl.presentation.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import bloom.shared.feature.skin_diary.impl.generated.resources.Res
import bloom.shared.feature.skin_diary.impl.generated.resources.msg_entry_saved
import bloom.shared.feature.skin_diary.impl.generated.resources.msg_entry_updated
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
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.DiaryCreateEditScreen
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.mvi.DiaryCreateEditEffect
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.mvi.DiaryCreateEditViewModel
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.detail.DiaryDetailScreen
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.detail.mvi.DiaryDetailEffect
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.detail.mvi.DiaryDetailIntent
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.detail.mvi.DiaryDetailViewModel
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.list.DiaryListScreen
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.list.mvi.DiaryListEffect
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.list.mvi.DiaryListViewModel

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

    entry<DiaryNavRoute.Detail> { route ->
        val entryId = route.entryId
        val vm: DiaryDetailViewModel = koinViewModel(
            parameters = { org.koin.core.parameter.parametersOf(entryId) }
        )
        val navigationHandler: DiaryNavigationHandler = koinInject()

        val state by vm.state.collectAsState()
        val scope = rememberCoroutineScope()
        val stateToast = rememberAdvToastStates()

        AdvToast.MakeToast(
            state = stateToast,
            toastType = EnumToastType.ERROR,
            paddingBottom = 50
        )
        LaunchedEffect(entryId) {
            vm.onIntent(DiaryDetailIntent.Reload)
        }
        LaunchedEffect(vm) {
            vm.effects.collect { effect ->
                when (effect) {
                    is DiaryDetailEffect.NavigateToEdit -> {
                        navigationHandler.handleDetailEffect(effect)
                    }

                    DiaryDetailEffect.NavigateBack -> {
                        navigationHandler.handleDetailEffect(effect)
                    }

                    is DiaryDetailEffect.ShowError -> {
                        scope.launch {
                            stateToast.show(effect.message)
                        }
                    }

                }
            }
        }

        DiaryDetailScreen(
            state = state,
            onIntent = vm::onIntent
        )
    }

    entry<DiaryNavRoute.Create> {
        val vm: DiaryCreateEditViewModel = koinViewModel()
        val navigationHandler: DiaryNavigationHandler = koinInject()

        val state by vm.state.collectAsState()
        val scope = rememberCoroutineScope()
        val stateToast = rememberAdvToastStates()

        AdvToast.MakeToast(
            state = stateToast,
            toastType = EnumToastType.ERROR,
            paddingBottom = 50
        )

        LaunchedEffect(Unit) {
            vm.init(entryId = null)
        }

        LaunchedEffect(vm) {
            vm.effects.collect { effect ->
                when (effect) {
                    is DiaryCreateEditEffect.NavigateBack -> navigationHandler.handleCreateEditEffect(
                        effect
                    )

                    is DiaryCreateEditEffect.ShowSuccess -> {
                        scope.launch {
                            val text = getString(Res.string.msg_entry_saved)
                            stateToast.show(text)
                        }
                    }

                    is DiaryCreateEditEffect.ShowError -> {
                        scope.launch { stateToast.show(effect.message) }
                    }

                    else -> {}
                }
            }
        }

        DiaryCreateEditScreen(
            state = state,
            onIntent = vm::onIntent
        )
    }

    entry<DiaryNavRoute.Edit> { route ->
        val vm: DiaryCreateEditViewModel = koinViewModel()
        val navigationHandler: DiaryNavigationHandler = koinInject()

        val entryId = route.entryId

        val state by vm.state.collectAsState()
        val scope = rememberCoroutineScope()
        val stateToast = rememberAdvToastStates()

        AdvToast.MakeToast(
            state = stateToast,
            toastType = EnumToastType.ERROR,
            paddingBottom = 50
        )

        LaunchedEffect(entryId) {
            entryId.let { vm.init(entryId = it) }
        }

        LaunchedEffect(vm) {
            vm.effects.collect { effect ->
                when (effect) {
                    is DiaryCreateEditEffect.NavigateBack -> navigationHandler.handleCreateEditEffect(
                        effect
                    )

                    is DiaryCreateEditEffect.NavigateBackToDetail -> {
                        navigationHandler.handleCreateEditEffect(effect)
                    }

                    is DiaryCreateEditEffect.ShowSuccess -> {
                        scope.launch {
                            val text = getString(Res.string.msg_entry_updated)
                            stateToast.show(text)
                        }
                    }

                    is DiaryCreateEditEffect.ShowError -> {
                        scope.launch { stateToast.show(effect.message) }
                    }

                    else -> {}
                }
            }
        }

        DiaryCreateEditScreen(
            state = state,
            onIntent = vm::onIntent
        )
    }
}