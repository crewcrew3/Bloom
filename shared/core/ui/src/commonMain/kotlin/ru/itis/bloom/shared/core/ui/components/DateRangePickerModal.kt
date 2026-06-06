package ru.itis.bloom.shared.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import bloom.shared.core.ui.generated.resources.Res
import bloom.shared.core.ui.generated.resources.btn_cancel
import bloom.shared.core.ui.generated.resources.btn_ok
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (Pair<LocalDate?, LocalDate?>) -> Unit,
    onDismiss: () -> Unit,
    initialStartDate: LocalDate? = null,
    initialEndDate: LocalDate? = null,
    modifier: Modifier = Modifier
) {
    val timeZone = TimeZone.currentSystemDefault()

    // Конвертация начальных дат в миллисекунды
    val startMillis = initialStartDate?.atStartOfDayIn(timeZone)?.toEpochMilliseconds()

    val endMillis = initialEndDate?.atStartOfDayIn(timeZone)?.toEpochMilliseconds()

    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = startMillis,
        initialSelectedEndDateMillis = endMillis
    )

    // Встроенный диалог Material3 с DateRangePicker
    MaterialTheme {
        DateRangePickerDialog(
            onDismiss = onDismiss,
            onConfirm = {
                val startDate: LocalDate? = dateRangePickerState.selectedStartDateMillis?.let { millis ->
                    Instant.fromEpochMilliseconds(millis)
                        .toLocalDateTime(timeZone)
                        .date
                }

                val endDate: LocalDate? = dateRangePickerState.selectedEndDateMillis?.let { millis ->
                    Instant.fromEpochMilliseconds(millis)
                        .toLocalDateTime(timeZone)
                        .date
                }

                onDateRangeSelected(Pair(startDate, endDate))
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = stringResource(Res.string.btn_cancel),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        dateRangePickerState.selectedStartDateMillis?.let {
                            val startDate = Instant.fromEpochMilliseconds(it)
                                .toLocalDateTime(timeZone)
                                .date
                            val endDate = dateRangePickerState.selectedEndDateMillis?.let { endMillis ->
                                Instant.fromEpochMilliseconds(endMillis)
                                    .toLocalDateTime(timeZone)
                                    .date
                            }
                            onDateRangeSelected(Pair(startDate, endDate))
                            onDismiss()
                        }
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.btn_ok),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            },
            modifier = modifier
        ) {
            DateRangePicker(
                state = dateRangePickerState,
                showModeToggle = false,
                title = null,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Вспомогательный компонент диалога
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    dismissButton: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = confirmButton,
        dismissButton = dismissButton,
        modifier = modifier
    ) {
        content()
    }
}