package ru.itis.bloom.shared.feature.makeupbag.impl.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import bloom.shared.feature.makeup_bag.impl.generated.resources.*
import io.github.tbib.compose_toast.AdvToast
import io.github.tbib.compose_toast.rememberAdvToastStates
import io.github.tbib.compose_toast.toast_ui.EnumToastType
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
import ru.itis.bloom.shared.feature.makeupbag.api.navigation.MakeupBagNavRoute
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productdetail.ProductDetailEffect
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productdetail.ProductDetailIntent
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productdetail.ProductDetailViewModel
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform.ProductFormEffect
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform.ProductFormIntent
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform.ProductFormViewModel
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist.ProductListEffect
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist.ProductListIntent
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist.ProductListViewModel
import ru.itis.bloom.shared.feature.makeupbag.impl.presentation.productdetail.ProductDetailScreen
import ru.itis.bloom.shared.feature.makeupbag.impl.presentation.productform.ProductFormScreen
import ru.itis.bloom.shared.feature.makeupbag.impl.presentation.productlist.ProductListScreen

fun EntryProviderScope<NavKey>.makeupBagEntryBuilder() {

    entry<MakeupBagNavRoute.ProductList> {
        val vm: ProductListViewModel = koinViewModel()
        val navigationHandler: MakeupBagNavigationHandler = koinInject()
        val bottomBarNav: BottomBarNavigator = koinInject()
        val burgerMenuNav: BurgerMenuNavigator = koinInject()

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

        val bottomBarSettings = provideBottomBarSettings(
            onRoutine = bottomBarNav::toRoutineSection,
            onSkinDiary = bottomBarNav::toSkinDiarySection,
            onMakeupBag = bottomBarNav::toMakeupBagSection,
            onProfile = bottomBarNav::toProfileSection
        )

        val topBarSettings = provideTopBarSettings(
            title = stringResource(Res.string.makeup_title_my_bag),
            iconType = TopBarIconType.BURGER,
            onIconClick = { /* Открывается Drawer через BaseScreen */ }
        )

        val burgerMenuSettings = provideBurgerMenuSettings(
            onRoutine = burgerMenuNav::toRoutineSection,
            onSkinDiary = burgerMenuNav::toSkinDiarySection,
            onMakeupBag = burgerMenuNav::toMakeupBagSection,
            onProfile = burgerMenuNav::toProfileSection
        )

        ProductListScreen(
            state = state,
            onIntent = vm::processIntent,
            bottomBarSettings = bottomBarSettings,
            topBarSettings = topBarSettings,
            burgerMenuSettings = burgerMenuSettings
        )
    }

    entry<MakeupBagNavRoute.ProductDetail> { route ->
        val vm: ProductDetailViewModel = koinViewModel()
        val navigationHandler: MakeupBagNavigationHandler = koinInject()

        val state by vm.state.collectAsState()
        val scope = rememberCoroutineScope()

        val stateToast = rememberAdvToastStates()
        AdvToast.MakeToast(
            state = stateToast,
            toastType = EnumToastType.INFO,
            paddingBottom = 50
        )

        LaunchedEffect(Unit) {
            vm.processIntent(ProductDetailIntent.LoadProduct(route.productId))
        }

        LaunchedEffect(vm) {
            vm.effect.collect { effect ->
                when (effect) {
                    is ProductDetailEffect.NavigateBack, is ProductDetailEffect.NavigateToEdit  -> navigationHandler.handleDetailEffect(effect)
                    is ProductDetailEffect.ShowMessage -> {
                        scope.launch {
                            val text = getString(effect.message.toResourceId())
                            stateToast.show(text)
                        }
                    }
                }
            }
        }

        val topBarSettings = provideTopBarSettings(
            title = stringResource(Res.string.makeup_title_product_detail),
            iconType = TopBarIconType.BACK,
            onIconClick = { vm.processIntent(ProductDetailIntent.NavigateBack) }
        )

        ProductDetailScreen(
            state = state,
            onIntent = vm::processIntent,
            topBarSettings = topBarSettings,
            bottomBarSettings = null,
            burgerMenuSettings = null,
        )
    }

    entry<MakeupBagNavRoute.CreateProduct> {
        val vm: ProductFormViewModel = koinViewModel()
        val navigationHandler: MakeupBagNavigationHandler = koinInject()

        val state by vm.state.collectAsState()
        val scope = rememberCoroutineScope()

        val stateToast = rememberAdvToastStates()
        AdvToast.MakeToast(
            state = stateToast,
            toastType = EnumToastType.INFO,
            paddingBottom = 50
        )

        LaunchedEffect(vm) {
            vm.effect.collect { effect ->
                when (effect) {
                    is ProductFormEffect.NavigateBack -> navigationHandler.handleFormEffect(effect)
                    is ProductFormEffect.ShowMessage -> {
                        scope.launch {
                            val text = getString(effect.message.toResourceId())
                            stateToast.show(text)
                        }
                    }
                }
            }
        }

        val topBarSettings = provideTopBarSettings(
            title = stringResource(Res.string.makeup_title_product_add),
            iconType = TopBarIconType.BACK,
            onIconClick = { vm.processIntent(ProductFormIntent.NavigateBack) }
        )

        ProductFormScreen(
            state = state,
            onIntent = vm::processIntent,
            topBarSettings = topBarSettings,
            isEditMode = false
        )
    }

    entry<MakeupBagNavRoute.EditProduct> { route ->
        val vm: ProductFormViewModel = koinViewModel()
        val navigationHandler: MakeupBagNavigationHandler = koinInject()

        val state by vm.state.collectAsState()
        val scope = rememberCoroutineScope()

        val stateToast = rememberAdvToastStates()
        AdvToast.MakeToast(
            state = stateToast,
            toastType = EnumToastType.INFO,
            paddingBottom = 50
        )

        LaunchedEffect(Unit) {
            vm.processIntent(ProductFormIntent.LoadProduct(route.productId))
        }

        LaunchedEffect(vm) {
            vm.effect.collect { effect ->
                when (effect) {
                    is ProductFormEffect.NavigateBack -> navigationHandler.handleFormEffect(effect)
                    is ProductFormEffect.ShowMessage -> {
                        scope.launch {
                            val text = getString(effect.message.toResourceId())
                            stateToast.show(text)
                        }
                    }
                }
            }
        }

        val topBarSettings = provideTopBarSettings(
            title = stringResource(Res.string.makeup_title_product_edit),
            iconType = TopBarIconType.BACK,
            onIconClick = { vm.processIntent(ProductFormIntent.NavigateBack) }
        )

        ProductFormScreen(
            state = state,
            onIntent = vm::processIntent,
            topBarSettings = topBarSettings,
            isEditMode = true
        )
    }
}