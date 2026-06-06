package ru.itis.bloom.shared.feature.profile.impl.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import bloom.shared.feature.profile.impl.generated.resources.Res
import bloom.shared.feature.profile.impl.generated.resources.profile_action_change_photo
import bloom.shared.feature.profile.impl.generated.resources.profile_action_delete_photo
import bloom.shared.feature.profile.impl.generated.resources.profile_action_logout
import bloom.shared.feature.profile.impl.generated.resources.profile_change_avatar
import bloom.shared.feature.profile.impl.generated.resources.profile_change_password
import bloom.shared.feature.profile.impl.generated.resources.profile_edit_email_title
import bloom.shared.feature.profile.impl.generated.resources.profile_edit_name_title
import bloom.shared.feature.profile.impl.generated.resources.profile_email_not_verified
import bloom.shared.feature.profile.impl.generated.resources.profile_field_email
import bloom.shared.feature.profile.impl.generated.resources.profile_field_name
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.BaseScreen
import ru.itis.bloom.shared.core.ui.components.AsyncImageBox
import ru.itis.bloom.shared.core.ui.components.settings.BottomBarSettings
import ru.itis.bloom.shared.core.ui.components.settings.BurgerMenuSettings
import ru.itis.bloom.shared.core.ui.components.settings.TopBarSettings
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.IconsCustom
import ru.itis.bloom.shared.feature.auth.api.model.UserProfile
import ru.itis.bloom.shared.feature.profile.impl.mvi.details.EditDialogType
import ru.itis.bloom.shared.feature.profile.impl.mvi.details.ProfileDetailsIntent
import ru.itis.bloom.shared.feature.profile.impl.mvi.details.ProfileDetailsState
import ru.itis.bloom.shared.feature.profile.impl.presentation.components.ChangePasswordDialog
import ru.itis.bloom.shared.feature.profile.impl.presentation.components.EditFieldDialog

@Composable
internal fun ProfileDetailsScreen(
    state: ProfileDetailsState,
    onIntent: (ProfileDetailsIntent) -> Unit,
    onAvatarClick: () -> Unit,
    bottomBarSettings: BottomBarSettings?,
    topBarSettings: TopBarSettings?,
    burgerMenuSettings: BurgerMenuSettings?,
    modifier: Modifier = Modifier
) {
    var showAvatarSheet by remember { mutableStateOf(false) }

    BaseScreen(
        topBarSettings = topBarSettings,
        bottomBarSettings = bottomBarSettings,
        burgerMenuSettings = burgerMenuSettings,
        content = { paddingValues ->
            if (state.isLoading && state.userProfile == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                state.userProfile?.let { profile ->
                    ProfileDetailsContent(
                        profile = profile,
                        isLoading = state.isLoading,
                        onNameClick = { onIntent(ProfileDetailsIntent.OpenDialog(EditDialogType.NAME)) },
                        onEmailClick = { onIntent(ProfileDetailsIntent.OpenDialog(EditDialogType.EMAIL)) },
                        onAvatarClick = { showAvatarSheet = true },
                        onPasswordClick = { onIntent(ProfileDetailsIntent.OpenDialog(EditDialogType.PASSWORD)) },
                        onLogoutClick = { onIntent(ProfileDetailsIntent.Logout) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                }
            }

            // Диалоги
            when (state.activeDialog) {
                EditDialogType.NAME -> EditFieldDialog(
                    title = stringResource(Res.string.profile_edit_name_title),
                    currentValue = state.userProfile?.name ?: "",
                    errorMessage = state.dialogError,
                    onConfirm = { onIntent(ProfileDetailsIntent.UpdateName(it)) },
                    onDismiss = { onIntent(ProfileDetailsIntent.CloseDialog) }
                )

                EditDialogType.EMAIL -> EditFieldDialog(
                    title = stringResource(Res.string.profile_edit_email_title),
                    currentValue = state.userProfile?.email ?: "",
                    errorMessage = state.dialogError,
                    onConfirm = { onIntent(ProfileDetailsIntent.UpdateEmail(it)) },
                    onDismiss = { onIntent(ProfileDetailsIntent.CloseDialog) }
                )

                EditDialogType.PASSWORD -> ChangePasswordDialog(
                    errorMessage = state.dialogError,
                    onConfirm = { current, new, confirm ->
                        onIntent(ProfileDetailsIntent.ChangePassword(current, new, confirm))
                    },
                    onDismiss = { onIntent(ProfileDetailsIntent.CloseDialog) }
                )

                null -> { /* No dialog */
                }
            }
            if (showAvatarSheet) {
                AvatarOptionsBottomSheet(
                    hasAvatar = state.userProfile?.avatarUrl != null,
                    onChangePhoto = {
                        showAvatarSheet = false
                        onAvatarClick()
                    },
                    onDeletePhoto = {
                        showAvatarSheet = false
                        onIntent(ProfileDetailsIntent.DeleteAvatar)
                    },
                    onDismiss = { showAvatarSheet = false }
                )
            }
        }
    )
}

@Composable
private fun ProfileDetailsContent(
    profile: UserProfile,
    isLoading: Boolean,
    onNameClick: () -> Unit,
    onEmailClick: () -> Unit,
    onAvatarClick: () -> Unit,
    onPasswordClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(DimensionsCustom.baseInsets),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Аватар
        Box(
            modifier = Modifier.size(DimensionsCustom.profileAvatarSize)
        ) {
            AsyncImageBox(
                model = profile.avatarUrl,
                contentScale = ContentScale.Crop,
                placeholderIcon = IconsCustom.iconProfile(),
                placeholderTint = MaterialTheme.colorScheme.tertiary,
                shape = CircleShape,
                modifier = Modifier.fillMaxSize(),
                iconModifier = Modifier.size(96.dp).padding(8.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .clickable(enabled = !isLoading, onClick = onAvatarClick)
            )
            // Иконка камеры поверх аватара
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(40.dp)
                    .padding(4.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = IconsCustom.iconCamera(),
                        contentDescription = stringResource(Res.string.profile_change_avatar),
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // Статус верификации
        if (!profile.isVerified) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.errorContainer
            ) {
                Text(
                    text = stringResource(Res.string.profile_email_not_verified),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }

        // Поля профиля
        ProfileFieldCard(
            label = stringResource(Res.string.profile_field_name),
            value = profile.name,
            onClick = onNameClick,
            isLoading = isLoading
        )

        ProfileFieldCard(
            label = stringResource(Res.string.profile_field_email),
            value = profile.email,
            onClick = onEmailClick,
            isLoading = isLoading
        )

        // Кнопка смены пароля
        Surface(
            shape = RoundedCornerShape(DimensionsCustom.cardCornerRadius),
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !isLoading, onClick = onPasswordClick)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.profile_change_password),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Icon(
                    painter = IconsCustom.iconChevronRight(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        // Кнопка выхода
        Surface(
            shape = RoundedCornerShape(DimensionsCustom.cardCornerRadius),
            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = !isLoading, onClick = onLogoutClick)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = IconsCustom.iconLogout(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    text = stringResource(Res.string.profile_action_logout),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AvatarOptionsBottomSheet(
    hasAvatar: Boolean,
    onChangePhoto: () -> Unit,
    onDeletePhoto: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Опция "Выбрать фото" — всегда доступна
            Surface(
                shape = RoundedCornerShape(DimensionsCustom.cardCornerRadius),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onChangePhoto)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = IconsCustom.iconCamera(),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = stringResource(Res.string.profile_action_change_photo),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Опция "Удалить фото" — только если аватар есть
            if (hasAvatar) {
                Surface(
                    shape = RoundedCornerShape(DimensionsCustom.cardCornerRadius),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onDeletePhoto)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = IconsCustom.iconDelete(),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = stringResource(Res.string.profile_action_delete_photo),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileFieldCard(
    label: String,
    value: String,
    onClick: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(DimensionsCustom.cardCornerRadius),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = !isLoading, onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    painter = IconsCustom.iconEdit(),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}