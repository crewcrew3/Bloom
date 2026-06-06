package ru.itis.bloom.shared.feature.makeupbag.impl.presentation.productdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bloom.shared.feature.makeup_bag.impl.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.BaseScreen
import ru.itis.bloom.shared.core.ui.components.AsyncImageBox
import ru.itis.bloom.shared.core.ui.components.CollapsibleTextSection
import ru.itis.bloom.shared.core.ui.components.StarRating
import ru.itis.bloom.shared.core.ui.components.settings.*
import ru.itis.bloom.shared.core.ui.theme.*
import ru.itis.bloom.shared.core.ui.utils.useHorizontalProductDetailLayout
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.Product
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductStatus
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productdetail.ProductDetailIntent
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productdetail.ProductDetailState
import java.text.SimpleDateFormat
import java.util.*

@Composable
internal fun ProductDetailScreen(
    state: ProductDetailState,
    onIntent: (ProductDetailIntent) -> Unit,
    bottomBarSettings: BottomBarSettings?,
    topBarSettings: TopBarSettings?,
    burgerMenuSettings: BurgerMenuSettings?,
    modifier: Modifier = Modifier
) {
    val useHorizontalLayout = useHorizontalProductDetailLayout()

    BaseScreen(
        topBarSettings = topBarSettings,
        bottomBarSettings = bottomBarSettings,
        burgerMenuSettings = burgerMenuSettings,
        floatActBtnSettings = FloatingActionButtonSettings(
            onClick = { onIntent(ProductDetailIntent.NavigateToEdit) },
            iconSettings = IconSettings(
                iconPainter = IconsCustom.iconEdit(),
                description = stringResource(Res.string.product_detail_action_edit),
            )
        ),
        content = { paddingValues ->
            if (state.isLoading && state.product == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                state.product?.let { product ->
                    ProductDetailContent(
                        product = product,
                        useHorizontalLayout = useHorizontalLayout,
                        onDeleteClick = { onIntent(ProductDetailIntent.Delete) },
                        onArchiveClick = { onIntent(ProductDetailIntent.Archive) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                            .padding(DimensionsCustom.productDetailContentPadding)
                    )
                }
            }
        }
    )
}

@Composable
private fun ProductDetailContent(
    product: Product,
    useHorizontalLayout: Boolean,
    onDeleteClick: () -> Unit,
    onArchiveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (useHorizontalLayout) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ProductDetailImage(
                imageUrl = product.photoUrl,
                modifier = Modifier.weight(1f).height(DimensionsCustom.productDetailImageSize)
            )
            ProductDetailInfo(
                product = product,
                onDeleteClick = onDeleteClick,
                onArchiveClick = onArchiveClick,
                modifier = Modifier.weight(1.5f)
            )
        }
    } else {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProductDetailImage(
                imageUrl = product.photoUrl,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DimensionsCustom.productDetailImageSize)
            )
            ProductDetailInfo(
                product = product,
                onDeleteClick = onDeleteClick,
                onArchiveClick = onArchiveClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ProductDetailImage(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(DimensionsCustom.productDetailImageRadius)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImageBox(
            model = imageUrl,
            modifier = Modifier
                .fillMaxSize(),
            iconModifier = Modifier
                .size(100.dp),
            placeholderIcon = IconsCustom.iconPlaceholderProduct(),
            placeholderTint = MaterialTheme.colorScheme.tertiary,
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun ProductDetailInfo(
    product: Product,
    onDeleteClick: () -> Unit,
    onArchiveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Top actions: Edit + Delete
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = onDeleteClick,
            ) {
                Icon(
                    painter = IconsCustom.iconDelete(),
                    contentDescription = stringResource(Res.string.product_detail_action_delete),
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(DimensionsCustom.productDetailTopIconSize)
                )
            }
        }

        // Main info card
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Name, Brand, Category
                Text(
                    text = product.name,
                    style = StylesCustom.productDetailTitle,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = product.brand ?: stringResource(Res.string.product_detail_brand_unknown),
                    style = StylesCustom.productDetailSubtitle,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Text(
                    text = stringResource(
                        Res.string.product_detail_category_format,
                        stringResource(product.category.toDisplayString())
                    ),
                    style = StylesCustom.productDetailLabel,
                    color = MaterialTheme.colorScheme.onSurface
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                // Dates and status
                ProductInfoRow(
                    label = stringResource(Res.string.product_detail_opened_label),
                    value = product.openedDate?.formatDate()
                        ?: stringResource(Res.string.product_detail_opened_not_opened)
                )
                ProductInfoRow(
                    label = stringResource(Res.string.product_detail_shelf_life_label),
                    value = product.shelfLifeAfterOpening?.let {
                        stringResource(Res.string.product_detail_shelf_life_format, it)
                    } ?: stringResource(Res.string.product_detail_value_unknown)
                )
                ProductInfoRow(
                    label = stringResource(Res.string.product_detail_expiry_label),
                    value = product.expiryDate?.formatDate()
                        ?: stringResource(Res.string.product_detail_value_unknown)
                )
                ProductInfoRow(
                    label = stringResource(Res.string.product_detail_status_label),
                    value = stringResource(
                        if (product.status == ProductStatus.Active) {
                            Res.string.product_detail_status_active
                        } else {
                            Res.string.product_detail_status_archived
                        }
                    )
                )

                // Collapsible: Composition
                CollapsibleTextSection(
                    title = stringResource(Res.string.product_detail_section_composition),
                    content = product.inciComposition,
                    placeholder = stringResource(Res.string.product_detail_value_unknown)
                )

                // Rating
                Text(
                    text = stringResource(Res.string.product_detail_section_rating),
                    style = StylesCustom.sectionTitle,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                StarRating(rating = product.personalRating)

                // Collapsible: Review
                CollapsibleTextSection(
                    title = stringResource(Res.string.product_detail_section_review),
                    content = product.personalReview,
                    placeholder = stringResource(Res.string.product_detail_value_unknown)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Archive button
        Button(
            onClick = onArchiveClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(Res.string.product_detail_button_archive))
        }
    }
}

@Composable
private fun ProductInfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = StylesCustom.productDetailLabel,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = StylesCustom.productDetailValue,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun String.formatDate(): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        val date = inputFormat.parse(this)
        date?.let { outputFormat.format(it) } ?: this
    } catch (e: Exception) {
        this
    }
}

@Composable
private fun ProductCategory.toDisplayString(): org.jetbrains.compose.resources.StringResource {
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

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=portrait")
@Composable
private fun ProductDetailScreenPreview() {
    BloomTheme {
        ProductDetailScreen(
            state = ProductDetailState(
                product = Product(
                    id = "1",
                    userId = "user1",
                    name = "Hydrating Cleanser",
                    brand = "CeraVe",
                    category = ProductCategory.Cleanser,
                    inciComposition = "Aqua, Glycerin, Niacinamide",
                    personalRating = 4,
                    personalReview = "Отличный очищающий гель, не сушит кожу",
                    openedDate = "2024-01-15",
                    shelfLifeAfterOpening = 12,
                    expiryDate = "2025-01-15",
                    status = ProductStatus.Active,
                    createdAt = "",
                    updatedAt = ""
                )
            ),
            onIntent = {},
            bottomBarSettings = null,
            topBarSettings = TopBarSettings(
                text = stringResource(Res.string.makeup_title_product_detail),
                iconType = TopBarIconType.BACK,
                onIconClick = {}
            ),
            burgerMenuSettings = null
        )
    }
}