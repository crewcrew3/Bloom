package ru.itis.bloom.shared.feature.makeupbag.impl.presentation.productlist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.BaseScreen
import ru.itis.bloom.shared.core.ui.components.CategoryChip
import ru.itis.bloom.shared.core.ui.components.ProductCard
import ru.itis.bloom.shared.core.ui.components.settings.BottomBarSettings
import ru.itis.bloom.shared.core.ui.components.settings.CategoryChipSettings
import ru.itis.bloom.shared.core.ui.components.settings.ProductCardSettings
import ru.itis.bloom.shared.core.ui.components.settings.TopBarSettings
import ru.itis.bloom.shared.core.ui.theme.*
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.Product
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist.ProductListIntent
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productlist.ProductListState
import bloom.shared.feature.makeup_bag.impl.generated.resources.*
import org.jetbrains.compose.resources.StringResource
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductStatus

@Composable
internal fun ProductListScreen(
    state: ProductListState,
    onIntent: (ProductListIntent) -> Unit,
    bottomBarSettings: BottomBarSettings,
    modifier: Modifier = Modifier
) {
    BaseScreen(
        topBarSettings = TopBarSettings(
            text = stringResource(Res.string.makeup_title_my_bag)
        ),
        bottomBarSettings = bottomBarSettings,
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = DimensionsCustom.baseInsets)
            ) {
                // Категория-чипы
                CategoryChipsRow(
                    selectedCategory = state.filterCategory,
                    onCategorySelected = { category ->
                        onIntent(ProductListIntent.FilterByCategory(category))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Список продуктов или индикатор загрузки
                if (state.isLoading && state.products.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(
                            items = state.products,
                            key = { it.id }
                        ) { product ->
                            ProductCard(
                                settings = product.toProductCardSettings(
                                    onClick = { onIntent(ProductListIntent.SelectProduct(product.id)) }
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            // FAB для добавления продукта
            FloatingActionButton(
                onClick = { onIntent(ProductListIntent.NavigateToCreate) },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .padding(
                        end = DimensionsCustom.baseInsets + 8.dp,
                        bottom = DimensionsCustom.baseInsets + 8.dp + DimensionsCustom.bottomBarHeight
                    )
            ) {
                Icon(
                    imageVector = IconsCustom.iconPlus(),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}

@Composable
private fun CategoryChipsRow(
    selectedCategory: ProductCategory?,
    onCategorySelected: (ProductCategory?) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = remember {
        listOf(null) + ProductCategory.entries
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { category ->
            CategoryChip(
                settings = CategoryChipSettings(
                    text = category?.let { stringResource(category.toDisplayString()) }
                        ?: stringResource(Res.string.makeup_chip_all),
                    isSelected = selectedCategory == category,
                    onClick = { onCategorySelected(category) },
                )
            )
        }
    }
}

@Composable
private fun ProductCategory.toDisplayString(): StringResource {
    return when (this) {
        ProductCategory.Cleanser -> Res.string.makeup_chip_cleanser
        ProductCategory.Toner -> Res.string.makeup_chip_toner
        ProductCategory.Serum -> Res.string.makeup_chip_serum
        ProductCategory.Moisturizer -> Res.string.makeup_chip_moisturizer
        ProductCategory.Sunscreen -> Res.string.makeup_chip_sunscreen
        ProductCategory.Mask -> Res.string.makeup_chip_mask
        ProductCategory.EyeCream -> Res.string.makeup_chip_eye_cream
        ProductCategory.Exfoliant -> Res.string.makeup_chip_exfoliant
        ProductCategory.Oil -> Res.string.makeup_chip_oil
        ProductCategory.Other -> Res.string.makeup_chip_other
    }
}

@Composable
private fun Product.toProductCardSettings(onClick: () -> Unit): ProductCardSettings {
    return ProductCardSettings(
        productName = name,
        brand = brand,
        category = stringResource(category.toDisplayString()),
        imageUrl = photoUrl,
        onClick = onClick,
    )
}

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=portrait")
@Composable
private fun ProductListScreenPreview() {
    BloomTheme {
        ProductListScreen(
            state = ProductListState(
                products = listOf(
                    Product(
                        id = "1",
                        userId = "user1",
                        name = "Hydrating Cleanser",
                        brand = "CeraVe",
                        category = ProductCategory.Cleanser,
                        status = ProductStatus.Active,
                        createdAt = "",
                        updatedAt = ""
                    ),
                    Product(
                        id = "2",
                        userId = "user1",
                        name = "Niacinamide 10% + Zinc 1%",
                        brand = "The Ordinary",
                        category = ProductCategory.Serum,
                        photoUrl = "https://via.placeholder.com/100",
                        status = ProductStatus.Active,
                        createdAt = "",
                        updatedAt = ""
                    )
                )
            ),
            onIntent = {},
            bottomBarSettings = BottomBarSettings({}, {}, {}, {})
        )
    }
}