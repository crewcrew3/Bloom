package ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import bloom.shared.feature.skin_diary.impl.generated.resources.Res
import bloom.shared.feature.skin_diary.impl.generated.resources.btn_add_photo
import bloom.shared.feature.skin_diary.impl.generated.resources.btn_remove_photo
import bloom.shared.feature.skin_diary.impl.generated.resources.label_photo
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.components.AsyncImageBox
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.IconsCustom
import ru.itis.bloom.shared.core.ui.theme.StylesCustom
import ru.itis.bloom.shared.core.ui.utils.ImagePickerCallback
import ru.itis.bloom.shared.core.ui.utils.rememberImagePicker
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.mvi.DiaryCreateEditIntent

@Composable
internal fun PhotoSection(
    photoBytes: ByteArray?,
    photoUrl: String?,
    isProcessing: Boolean,
    error: String?,
    onIntent: (DiaryCreateEditIntent) -> Unit
) {
    val imagePickerCallback = remember {
        object : ImagePickerCallback {
            override fun onImageSelected(uri: String?) {
                uri?.let { onIntent(DiaryCreateEditIntent.RequestPhotoSelection(it)) }
            }
        }
    }
    val launchImagePicker = rememberImagePicker(callback = imagePickerCallback)

    Column(verticalArrangement = Arrangement.spacedBy(DimensionsCustom.diaryFieldSpacing)) {
        Text(
            text = stringResource(Res.string.label_photo),
            style = StylesCustom.diaryPhotoLabel.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )

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
                    shape = RoundedCornerShape(DimensionsCustom.diaryPhotoRadius),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DimensionsCustom.diaryPhotoPlaceholderHeight)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(DimensionsCustom.diaryIconSizeLarge),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            photoBytes != null || photoUrl != null -> {
                // Показываем фото
                Box {
                    AsyncImageBox(
                        model = photoBytes ?: photoUrl,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(DimensionsCustom.diaryPhotoHeight)
                            .clip(RoundedCornerShape(DimensionsCustom.diaryPhotoRadius)),
                        contentScale = ContentScale.Crop,
                        placeholderIcon = IconsCustom.iconPlaceholderProduct(),
                        shape = RoundedCornerShape(DimensionsCustom.diaryPhotoRadius)
                    )
                    IconButton(
                        onClick = { onIntent(DiaryCreateEditIntent.RemovePhoto) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .background(MaterialTheme.colorScheme.error, RoundedCornerShape(DimensionsCustom.diaryCornerRadiusLarge))
                    ) {
                        Icon(
                            painter = IconsCustom.iconClose(),
                            contentDescription = stringResource(Res.string.btn_remove_photo),
                            tint = MaterialTheme.colorScheme.onError,
                            modifier = Modifier.size(DimensionsCustom.diaryIconSizeSmall)
                        )
                    }
                }
            }

            else -> {
                // Placeholder для добавления фото
                Surface(
                    onClick = launchImagePicker,
                    shape = RoundedCornerShape(DimensionsCustom.diaryPhotoRadius),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(DimensionsCustom.diaryPhotoPlaceholderHeight)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(DimensionsCustom.diaryFieldSpacing)
                        ) {
                            Icon(
                                painter = IconsCustom.iconCamera(),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(DimensionsCustom.diaryIconSizeLarge)
                            )
                            Text(
                                text = stringResource(Res.string.btn_add_photo),
                                style = StylesCustom.diaryPhotoLabel.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                            )
                        }
                    }
                }
            }
        }
    }
}