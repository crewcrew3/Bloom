package ru.itis.bloom.shared.feature.skindiary.impl.presentation.add

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import bloom.shared.feature.skin_diary.impl.generated.resources.Res
import bloom.shared.feature.skin_diary.impl.generated.resources.btn_save
import bloom.shared.feature.skin_diary.impl.generated.resources.title_create_diary_entry
import bloom.shared.feature.skin_diary.impl.generated.resources.title_edit_diary_entry
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.core.ui.BaseScreen
import ru.itis.bloom.shared.core.ui.components.SingleDatePickerModal
import ru.itis.bloom.shared.core.ui.components.settings.TopBarIconType
import ru.itis.bloom.shared.core.ui.components.settings.TopBarSettings
import ru.itis.bloom.shared.core.ui.theme.BloomTheme
import ru.itis.bloom.shared.core.ui.theme.DimensionsCustom
import ru.itis.bloom.shared.core.ui.theme.StylesCustom
import ru.itis.bloom.shared.feature.skindiary.api.model.ProblemZone
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.components.DateSection
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.components.HydrationLevelSection
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.components.NotesSection
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.components.PhotoSection
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.components.ProblemZonesSection
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.components.SkinConditionSection
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.mvi.DiaryCreateEditIntent
import ru.itis.bloom.shared.feature.skindiary.impl.presentation.add.mvi.DiaryCreateEditState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryCreateEditScreen(
    state: DiaryCreateEditState,
    onIntent: (DiaryCreateEditIntent) -> Unit,
    topBarSettings: TopBarSettings?,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }

    BaseScreen(
        topBarSettings = topBarSettings
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(horizontal = DimensionsCustom.baseInsets)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Date
            DateSection(
                date = state.date,
                onClick = { showDatePicker = true }
            )

            // Skin Condition Slider (1-10)
            SkinConditionSection(
                value = state.skinCondition,
                onValueChange = { onIntent(DiaryCreateEditIntent.SetSkinCondition(it)) }
            )

            // Hydration Level Slider (1-5)
            HydrationLevelSection(
                value = state.hydrationLevel,
                onValueChange = { onIntent(DiaryCreateEditIntent.SetHydrationLevel(it)) }
            )

            // Problem Zones
            ProblemZonesSection(
                selectedZones = state.problemZones,
                onZoneToggle = { onIntent(DiaryCreateEditIntent.ToggleProblemZone(it)) }
            )

            // Notes
            NotesSection(
                notes = state.notes,
                onNotesChange = { onIntent(DiaryCreateEditIntent.SetNotes(it)) }
            )

            // Photo Section
            PhotoSection(
                photoBytes = state.photoBytes,
                photoUrl = state.photoUrl,
                isProcessing = state.isPhotoProcessing,
                error = state.photoError,
                onIntent = onIntent
            )

            // Save Button
            Button(
                onClick = { onIntent(DiaryCreateEditIntent.SaveEntry) },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(DimensionsCustom.diaryButtonHeight),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.38f),
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.38f)
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(DimensionsCustom.diaryIconSizeMedium),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.btn_save),
                        style = StylesCustom.diaryButtonText
                    )
                }
            }
        }
    }

    if (showDatePicker) {
        SingleDatePickerModal(
            onDateSelected = { millis ->
                millis?.let {
                    val date = LocalDate.fromEpochDays((it / (24 * 60 * 60 * 1000)).toInt())
                    onIntent(DiaryCreateEditIntent.SetDate(date))
                }
            },
            onDismiss = { showDatePicker = false },
            initialSelectedDateMillis = state.date.toEpochDays() * 24 * 60 * 60 * 1000
        )
    }
}

@Preview(showBackground = true, apiLevel = 34)
@Composable
private fun DiaryCreateEditScreenPreview() {
    BloomTheme {
        DiaryCreateEditScreen(
            state = DiaryCreateEditState(
                date = LocalDate(2026, 6, 2),
                skinCondition = 7,
                hydrationLevel = 3,
                problemZones = listOf(ProblemZone.NOSE, ProblemZone.FOREHEAD),
                notes = "Сегодня был хороший день"
            ),
            onIntent = {},
            topBarSettings = TopBarSettings(text = stringResource(Res.string.title_edit_diary_entry))
        )
    }
}