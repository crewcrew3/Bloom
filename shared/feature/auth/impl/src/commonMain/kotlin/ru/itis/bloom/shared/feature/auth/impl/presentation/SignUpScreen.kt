package ru.itis.bloom.shared.feature.auth.impl.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import bloom.shared.feature.auth.impl.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import ru.itis.bloom.shared.feature.auth.impl.mvi.AuthIntent
import ru.itis.bloom.shared.feature.auth.impl.mvi.AuthState

@Composable
internal fun SignUpScreen(
    state: AuthState,
    onIntent: (AuthIntent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Заголовок
        Text(
            text = stringResource(Res.string.auth_title_register),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Имя
        OutlinedTextField(
            value = state.name,
            onValueChange = { onIntent(AuthIntent.NameChanged(it)) },
            label = { Text(stringResource(Res.string.auth_label_name)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.nameError != null,
            supportingText = state.nameError?.let { { Text(stringResource(it)) } }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Email
        OutlinedTextField(
            value = state.email,
            onValueChange = { onIntent(AuthIntent.EmailChanged(it)) },
            label = { Text(stringResource(Res.string.auth_label_email)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.emailError != null,
            supportingText = state.emailError?.let { { Text(stringResource(it)) } }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Пароль
        OutlinedTextField(
            value = state.password,
            onValueChange = { onIntent(AuthIntent.PasswordChanged(it)) },
            label = { Text(stringResource(Res.string.auth_label_password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.passwordError != null,
            supportingText = state.passwordError?.let { { Text(stringResource(it)) } }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Подтверждение пароля
        OutlinedTextField(
            value = state.passwordConfirmation,
            onValueChange = { onIntent(AuthIntent.PasswordConfirmationChanged(it)) },
            label = { Text(stringResource(Res.string.auth_label_password_confirm)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.passwordConfirmationError != null,
            supportingText = state.passwordConfirmationError?.let { { Text(stringResource(it)) } }
        )

        // Общая ошибка
        state.generalError?.let {
            Text(
                text = stringResource(it),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка регистрации
        Button(
            onClick = { onIntent(AuthIntent.RegisterClicked) },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading && state.isRegisterFormValid
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.height(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text(stringResource(Res.string.auth_button_register))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Переключение на вход
        TextButton(onClick = { onIntent(AuthIntent.NavigateToLogin) }) {
            Text(stringResource(Res.string.auth_link_to_login))
        }
    }
}