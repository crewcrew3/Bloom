package ru.itis.bloom.shared.feature.makeupbag.impl.presentation.productform

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bloom.shared.feature.makeup_bag.impl.generated.resources.*
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.BaseScreen
import ru.itis.bloom.shared.core.ui.components.*
import ru.itis.bloom.shared.core.ui.components.settings.*
import ru.itis.bloom.shared.core.ui.theme.*
import ru.itis.bloom.shared.core.ui.utils.ImagePickerCallback
import ru.itis.bloom.shared.core.ui.utils.rememberImagePicker
import ru.itis.bloom.shared.core.ui.utils.useHorizontalProductFormLayout
import ru.itis.bloom.shared.feature.makeupbag.api.model.response.ProductCategory
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform.ProductFormIntent
import ru.itis.bloom.shared.feature.makeupbag.impl.mvi.productform.ProductFormState
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
internal fun ProductFormScreen(
    state: ProductFormState,
    onIntent: (ProductFormIntent) -> Unit,
    topBarSettings: TopBarSettings?,
    isEditMode: Boolean,
    modifier: Modifier = Modifier
) {
    val useHorizontalLayout = useHorizontalProductFormLayout()

    // Date formatters
    val dateFormatterApi = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val dateFormatterUi = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    // Image picker callback
    val imagePickerCallback = remember {
        object : ImagePickerCallback {
            override fun onImageSelected(uri: String?) {
                uri?.let { onIntent(ProductFormIntent.RequestPhotoSelection(uri)) }
            }
        }
    }
    val launchImagePicker = rememberImagePicker(callback = imagePickerCallback)

    // Date pickers state
    var showOpenedDatePicker by remember { mutableStateOf(false) }

    BaseScreen(
        topBarSettings = topBarSettings,
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
                ProductFormContent(
                    state = state,
                    useHorizontalLayout = useHorizontalLayout,
                    onIntent = onIntent,
                    onImageClick = { launchImagePicker() },
                    onOpenedDateClick = { showOpenedDatePicker = true },
                    isEditMode = isEditMode,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(DimensionsCustom.productFormContentPadding)
                )
            }
        }
    )

    // Date pickers
    if (showOpenedDatePicker) {
        val initialDate = state.form.openedDate?.let {
            try {
                LocalDate.parse(it, dateFormatterApi)
                    .atStartOfDay(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()
            } catch (e: Exception) { null }
        }

        SingleDatePickerModal(
            onDateSelected = { millis ->
                millis?.let {
                    val date = Instant.ofEpochMilli(it)
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                    onIntent(ProductFormIntent.OpenedDateChanged(date.format(dateFormatterApi)))
                }
            },
            onDismiss = { showOpenedDatePicker = false },
            initialSelectedDateMillis = initialDate
        )
    }
}

@Composable
private fun ProductFormContent(
    state: ProductFormState,
    useHorizontalLayout: Boolean,
    onIntent: (ProductFormIntent) -> Unit,
    isEditMode: Boolean,
    onImageClick: () -> Unit,
    onOpenedDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    if (useHorizontalLayout) {
        Row(
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            ProductFormImageSection(
                photoBytes = state.form.photoBytes,
                imageUrl = state.form.photoUri,
                isProcessing = state.isPhotoProcessing,
                error = state.form.photoError?.let { stringResource(it) },
                onIntent = onIntent,
                onImageClick = onImageClick,
                modifier = Modifier.weight(1f)
            )
            ProductFormFieldsSection(
                state = state,
                onIntent = onIntent,
                onOpenedDateClick = onOpenedDateClick,
                isEditMode = isEditMode,
                modifier = Modifier.weight(1.5f)
            )
        }
    } else {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProductFormImageSection(
                photoBytes = state.form.photoBytes,
                imageUrl = state.form.photoUri,
                isProcessing = state.isPhotoProcessing,
                error = state.form.photoError?.let { stringResource(it) },
                onIntent = onIntent,
                onImageClick = onImageClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DimensionsCustom.productFormImageSize)
            )
            ProductFormFieldsSection(
                state = state,
                onIntent = onIntent,
                onOpenedDateClick = onOpenedDateClick,
                isEditMode = isEditMode,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ProductFormImageSection(
    photoBytes: ByteArray?,
    imageUrl: String?,
    error: String?,
    onIntent: (ProductFormIntent) -> Unit,
    isProcessing: Boolean,
    onImageClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(verticalArrangement = Arrangement.spacedBy(DimensionsCustom.productFormSpacing)) {
        // Ошибка обработки фото
        error?.let { errMsg ->
            Text(
                text = errMsg,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 4.dp)
            )
        }

        when {
            isProcessing -> {
                // Индикатор загрузки
                Surface(
                    shape = RoundedCornerShape(DimensionsCustom.productFormImageRadius),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DimensionsCustom.productFormImagePlaceholderHeight)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(DimensionsCustom.productFormCircularProgressIndicatorSize),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            photoBytes != null || imageUrl != null -> {
                //показываем фото
                Box(
                    modifier = modifier
                        .clip(RoundedCornerShape(DimensionsCustom.productFormImageRadius))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable(onClick = onImageClick),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = photoBytes ?: imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    IconButton(
                        onClick ={ onIntent(ProductFormIntent.RemovePhoto) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(
                                MaterialTheme.colorScheme.error,
                                RoundedCornerShape(DimensionsCustom.productFormCornerRadius)
                            )
                    ) {
                        Icon(
                            painter = IconsCustom.iconClose(),
                            contentDescription = stringResource(Res.string.form_button_remove_photo),
                            tint = MaterialTheme.colorScheme.onError,
                            modifier = Modifier.size(DimensionsCustom.diaryIconSizeSmall)
                        )
                    }
                }
            }

            else -> {
                // Placeholder для добавления фото
                Box(
                    modifier = modifier
                        .clip(RoundedCornerShape(DimensionsCustom.productFormImageRadius))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable(onClick = onImageClick),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = IconsCustom.iconPlaceholderProduct(),
                        contentDescription = stringResource(Res.string.form_image_placeholder_desc),
                        modifier = Modifier.size(80.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductFormFieldsSection(
    state: ProductFormState,
    onIntent: (ProductFormIntent) -> Unit,
    isEditMode: Boolean,
    onOpenedDateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val form = state.form
    val calendarIcon = IconsCustom.iconCalendar()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Name
        FormField(
            settings = FormFieldSettings(
                label = Res.string.form_label_name,
                value = form.name,
                onValueChange = { onIntent(ProductFormIntent.NameChanged(it)) },
                placeholder = Res.string.form_placeholder_name,
                isError = form.nameError != null,
                supportingText = form.nameError
            )
        )

        // Brand
        FormField(
            settings = FormFieldSettings(
                label = Res.string.form_label_brand,
                value = form.brand ?: "",
                onValueChange = { onIntent(ProductFormIntent.BrandChanged(it)) },
                placeholder = Res.string.form_placeholder_brand,
                isError = form.brandError != null,
                supportingText = form.brandError
            )
        )

        // Opened Date (filled + calendar)
        FormField(
            settings = FormFieldSettings(
                label = Res.string.form_label_opened_date,
                value = form.openedDate?.let { formatDateForUi(it) } ?: "",
                onValueChange = { /* readonly, use picker */ },
                placeholder = Res.string.form_placeholder_date,
                trailingIcon = calendarIcon,
                onTrailingIconClick = onOpenedDateClick,
                enabled = false
            ),
            isFilled = true
        )

        // Shelf Life After Opening (filled, integer only)
        FormField(
            settings = FormFieldSettings(
                label = Res.string.form_label_shelf_life,
                value = form.shelfLifeAfterOpening?.toString() ?: "",
                onValueChange = {
                    it.toIntOrNull()?.let { num ->
                        onIntent(ProductFormIntent.ShelfLifeChanged(num))
                    } ?: onIntent(ProductFormIntent.ShelfLifeChanged(0))
                },
                placeholder = Res.string.form_placeholder_months,
                isError = form.shelfLifeError != null,
                supportingText = form.shelfLifeError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            ),
            isFilled = true
        )

        // Categories (FlowRow of chips)
        Text(
            text = stringResource(Res.string.form_label_category),
            style = StylesCustom.formSectionTitle,
            color = MaterialTheme.colorScheme.onSurface
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            ProductCategory.entries.forEach { category ->
                CategoryChip(
                    settings = CategoryChipSettings(
                        text = stringResource(category.toDisplayString()),
                        isSelected = form.category == category,
                        onClick = { onIntent(ProductFormIntent.CategoryChanged(category)) },
                    )
                )
            }
        }

        // Composition (multiline outlined)
        FormField(
            settings = FormFieldSettings(
                label = Res.string.form_label_composition,
                value = form.inciComposition ?: "",
                onValueChange = { onIntent(ProductFormIntent.InciChanged(it)) },
                placeholder = Res.string.form_placeholder_composition,
                singleLine = false
            )
        )

        // Review (multiline outlined)
        FormField(
            settings = FormFieldSettings(
                label = Res.string.form_label_review,
                value = form.personalReview ?: "",
                onValueChange = { onIntent(ProductFormIntent.ReviewChanged(it)) },
                placeholder = Res.string.form_placeholder_review,
                isError = form.reviewError != null,
                supportingText = form.reviewError,
                singleLine = false
            )
        )

        // Rating (clickable stars in Surface)
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.form_label_rating),
                    style = StylesCustom.formSectionTitle,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                InteractiveStarRating(
                    rating = form.personalRating ?: 0,
                    onRatingChange = { onIntent(ProductFormIntent.RatingChanged(it)) }
                )
            }
        }

        if (isEditMode) {
            // "Finished" checkbox
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.6f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(Res.string.form_label_finished),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )

                    Checkbox(
                        checked = form.isFinished,
                        onCheckedChange = { onIntent(ProductFormIntent.FinishedChanged(it)) }
                    )
                }
            }
        }

        // Save button
        Button(
            onClick = { onIntent(ProductFormIntent.Submit) },
            enabled = !state.isLoading && form.isValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(
                    text = stringResource(Res.string.form_button_save),
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }

        // Delete button (только в режиме редактирования)
        if (isEditMode) {
            OutlinedButton(
                onClick = { onIntent(ProductFormIntent.Delete) },
                enabled = !state.isLoading,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error,

                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(Res.string.form_button_delete))
            }
        }
    }
}

@Composable
private fun InteractiveStarRating(
    rating: Int?,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    maxStars: Int = 5
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        repeat(maxStars) { index ->
            val starIndex = index + 1
            Icon(
                painter = if (rating != null && index < rating) {
                    IconsCustom.iconStarFilled()
                } else {
                    IconsCustom.iconStarOutline()
                },
                contentDescription = stringResource(Res.string.form_rating_star_desc, starIndex),
                tint = if (rating != null && index < rating) {
                    MaterialTheme.colorScheme.tertiary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                },
                modifier = Modifier
                    .size(DimensionsCustom.starSize)
                    .clickable { onRatingChange(starIndex) }
            )
        }
    }
}

private fun formatDateForUi(dateString: String): String {
    return try {
        val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val date = LocalDate.parse(dateString, inputFormat)
        date.format(outputFormat)
    } catch (e: Exception) {
        dateString
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
private fun ProductFormScreenPreview() {
    BloomTheme {
        ProductFormScreen(
            state = ProductFormState(
                form = ProductFormState.FormFields(
                    name = "Hydrating Cleanser",
                    brand = "CeraVe",
                    category = ProductCategory.Cleanser,
                    inciComposition = "Aqua, Glycerin, Niacinamide",
                    personalRating = 4,
                    personalReview = "Отличный гель",
                    openedDate = "2024-01-15",
                    shelfLifeAfterOpening = 12
                )
            ),
            onIntent = {},
            topBarSettings = TopBarSettings(
                text = stringResource(Res.string.form_title_edit),
                iconType = TopBarIconType.BACK,
                onIconClick = {}
            ),
            isEditMode = true
        )
    }
}