package ru.itis.bloom.shared.core.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bloom.shared.core.ui.generated.resources.*
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.components.settings.ProductCardSettings
import ru.itis.bloom.shared.core.ui.theme.BloomTheme
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.IconsCustom
import ru.itis.bloom.shared.core.ui.theme.StylesCustom

@Composable
fun ProductCard(
    settings: ProductCardSettings,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(DimensionsCustom.productCardRadius),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = modifier
            .clickable(onClick = settings.onClick)
            .padding(vertical = DimensionsCustom.productCardVerticalPadding)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(DimensionsCustom.productCardInnerPadding),
            verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImageBox(
                model = settings.imageUrl,
                placeholderIcon = IconsCustom.iconPlaceholderProduct(),
                placeholderTint = MaterialTheme.colorScheme.tertiary,
                shape = RoundedCornerShape(DimensionsCustom.productCardImageRadius),
                modifier = Modifier
                    .size(DimensionsCustom.productCardImageSize),
                iconModifier = Modifier.size(56.dp)
                            .padding(8.dp)
                            .align(Alignment.CenterVertically),
            )

            Spacer(modifier = Modifier.width(DimensionsCustom.productCardTextSpacing))

            // Текст: название, бренд, категория
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(DimensionsCustom.productCardTextSpacing)
            ) {
                Text(
                    text = settings.productName,
                    style = StylesCustom.productCardName,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = settings.brand ?: stringResource(Res.string.placeholder_text_unknown),
                    style = StylesCustom.productCardBrand,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = settings.category,
                    style = StylesCustom.productCardCategory,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductCardPreview() {
    BloomTheme {
        ProductCard(
            settings = ProductCardSettings(
                productName = "Hydrating Cleanser",
                brand = "CeraVe",
                category = "Cleanser"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductCardWithImagePreview() {
    BloomTheme {
        ProductCard(
            settings = ProductCardSettings(
                productName = "Vitamin C Serum",
                brand = "The Ordinary",
                category = "Serum",
                imageUrl = "https://via.placeholder.com/100"
            )
        )
    }
}