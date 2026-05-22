package ru.itis.bloom.shared.feature.makeupbag.impl.navigation

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
import ru.itis.bloom.shared.feature.makeupbag.api.navigation.MakeupBagNavRoute
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productdetail.ProductDetailIntent
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productdetail.ProductDetailViewModel
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform.ProductFormIntent
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform.ProductFormViewModel
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist.ProductListEffect
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist.ProductListIntent
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist.ProductListViewModel
import ru.itis.bloom.shared.feature.makeupbag.impl.presentation.productlist.ProductListScreen

fun EntryProviderScope<NavKey>.makeupBagEntryBuilder() {

    entry<MakeupBagNavRoute.ProductList> {
        val vm: ProductListViewModel = koinViewModel()
        val navigationHandler: MakeupBagNavigationHandler = koinInject()
        val bottomBarNav: BottomBarNavigator = koinInject()

        val state by vm.state.collectAsState()
        val scope = rememberCoroutineScope()

        val stateToast = rememberAdvToastStates()
        AdvToast.MakeToast(
            state = stateToast,
            toastType = EnumToastType.INFO,
            paddingBottom = 50
        )

        LaunchedEffect(Unit) {
            vm.processIntent(ProductListIntent.LoadProducts)
        }

        LaunchedEffect(vm) {
            vm.effect.collect { effect ->
                when (effect) {
                    // Навигация -> делегируем хендлеру
                    is ProductListEffect.NavigateToProductDetail, is ProductListEffect.NavigateToCreateScreen -> {
                        navigationHandler.handleListEffect(effect)
                    }

                    is ProductListEffect.ShowMessage -> {
                        scope.launch {
                            val text = getString(effect.message.toResourceId())
                            stateToast.show(text)
                        }
                    }
                }
            }
        }

        ProductListScreen(
            state = state,
            onIntent = vm::processIntent,
            bottomBarSettings = BottomBarSettings(
                onRoutineSectionClick = bottomBarNav::toRoutineSection,
                onMakeupBagSectionClick = bottomBarNav::toMakeupBagSection,
                onProfileSectionClick = bottomBarNav::toProfileSection,
                onSkinDiarySectionClick = bottomBarNav::toSkinDiarySection,
            )
        )
    }

    entry<MakeupBagNavRoute.ProductDetail> { route ->
        val vm: ProductDetailViewModel = koinViewModel()
        val navigationHandler: MakeupBagNavigationHandler = koinInject()
        val state by vm.state.collectAsState()

        LaunchedEffect(Unit) {
            vm.processIntent(ProductDetailIntent.LoadProduct(route.productId))
        }
        LaunchedEffect(vm) {
            vm.effect.collect(navigationHandler::handleDetailEffect)
        }

        /* ProductDetailScreen(state = state, onIntent = vm::processIntent) */
    }

    entry<MakeupBagNavRoute.CreateProduct> {
        val vm: ProductFormViewModel = koinViewModel()
        val navigationHandler: MakeupBagNavigationHandler = koinInject()
        val state by vm.state.collectAsState()

        LaunchedEffect(vm) {
            vm.effect.collect(navigationHandler::handleFormEffect)
        }

        /* ProductFormScreen(state = state, onIntent = vm::processIntent, isEditMode = false) */
    }

    entry<MakeupBagNavRoute.EditProduct> { route ->
        val vm: ProductFormViewModel = koinViewModel()
        val navigationHandler: MakeupBagNavigationHandler = koinInject()
        val state by vm.state.collectAsState()

        LaunchedEffect(Unit) {
            vm.processIntent(ProductFormIntent.LoadProduct(route.productId))
        }
        LaunchedEffect(vm) {
            vm.effect.collect(navigationHandler::handleFormEffect)
        }

        /* ProductFormScreen(state = state, onIntent = vm::processIntent, isEditMode = true) */
    }
}