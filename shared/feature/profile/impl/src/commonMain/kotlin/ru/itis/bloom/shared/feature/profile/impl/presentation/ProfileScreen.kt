package ru.itis.bloom.shared.feature.profile.impl.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bloom.shared.feature.profile.impl.generated.resources.*
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.BaseScreen
import ru.itis.bloom.shared.core.ui.components.AsyncImageBox
import ru.itis.bloom.shared.core.ui.components.settings.*
import ru.itis.bloom.shared.core.ui.theme.*
import ru.itis.bloom.shared.feature.auth.api.model.UserProfile
import ru.itis.bloom.shared.feature.profile.impl.mvi.ProfileIntent
import ru.itis.bloom.shared.feature.profile.impl.mvi.ProfileState

@Composable
internal fun ProfileScreen(
    state: ProfileState,
    onIntent: (ProfileIntent) -> Unit,
    bottomBarSettings: BottomBarSettings?,
    topBarSettings: TopBarSettings?,
    burgerMenuSettings: BurgerMenuSettings?,
    modifier: Modifier = Modifier
) {
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
                    ProfileContent(
                        profile = profile,
                        onLogoutClick = { onIntent(ProfileIntent.Logout) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(top = DimensionsCustom.profileContentPadding)
                    )
                }
            }
        }
    )
}

@Composable
private fun ProfileContent(
    profile: UserProfile,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {

        AsyncImageBox(
            model = profile.avatarUrl,
            contentScale = ContentScale.Crop,
            placeholderIcon = IconsCustom.iconProfile(),
            placeholderTint = MaterialTheme.colorScheme.tertiary,
            shape = CircleShape,
            modifier = Modifier
                .size(DimensionsCustom.profileAvatarSize),
            iconModifier = Modifier.size(96.dp)
                .padding(8.dp)
                .align(Alignment.CenterHorizontally),
        )

        Text(
            text = profile.name,
            style = StylesCustom.profileUserName,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(Modifier.height(16.dp))

        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onLogoutClick)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = IconsCustom.iconLogout(),
                        contentDescription = stringResource(Res.string.profile_action_logout),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = stringResource(Res.string.profile_action_logout),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:parent=pixel_5,orientation=portrait")
@Composable
private fun ProfileScreenPreview() {
    BloomTheme {
        ProfileScreen(
            state = ProfileState(
                userProfile = ru.itis.bloom.shared.feature.auth.api.model.UserProfile(
                    id = "1",
                    name = "Test User",
                    email = "test@example.com",
                    avatarUrl = null,
                    isVerified = true,
                    createdAt = "",
                    updatedAt = ""
                )
            ),
            onIntent = {},
            bottomBarSettings = BottomBarSettings({}, {}, {}, {}),
            topBarSettings = TopBarSettings(
                text = stringResource(Res.string.profile_title),
                iconType = TopBarIconType.NONE
            ),
            burgerMenuSettings = null
        )
    }
}