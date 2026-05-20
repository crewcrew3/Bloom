package ru.itis.bloom.shared.feature.makeupbag.impl.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import ru.itis.bloom.shared.feature.makeupbag.api.navigation.MakeupBagNavRoute
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productdetail.ProductDetailViewModel
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform.ProductFormViewModel
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist.ProductListViewModel

fun EntryProviderScope<NavKey>.makeupBagEntryBuilder() {
    entry<MakeupBagNavRoute.ProductList> {
        val vm: ProductListViewModel = koinViewModel()
        val navigationHandler: MakeupBagNavigationHandler = koinInject()
        val state by vm.state.collectAsState()

        LaunchedEffect(vm) {
            vm.effect.collect(navigationHandler::handleListEffect)
        }

        /* ProductListScreen(state = state, onIntent = vm::processIntent) */
    }

    entry<MakeupBagNavRoute.ProductDetail> { route ->
        val vm: ProductDetailViewModel = koinViewModel()
        val navigationHandler: MakeupBagNavigationHandler = koinInject()
        val state by vm.state.collectAsState()

        LaunchedEffect(Unit) {
            vm.processIntent(ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productdetail.ProductDetailIntent.LoadProduct(route.productId))
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
            vm.processIntent(ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform.ProductFormIntent.LoadProduct(route.productId))
        }
        LaunchedEffect(vm) {
            vm.effect.collect(navigationHandler::handleFormEffect)
        }

        /* ProductFormScreen(state = state, onIntent = vm::processIntent, isEditMode = true) */
    }
}