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
import ru.itis.bloom.shared.feature.auth.impl.mvi.AuthIntent
import ru.itis.bloom.shared.feature.auth.impl.mvi.AuthState

@Composable
fun SignUpScreen(
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
            text = "📝 Регистрация",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Имя
        OutlinedTextField(
            value = state.name,
            onValueChange = { onIntent(AuthIntent.NameChanged(it)) },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.nameError != null,
            supportingText = state.nameError?.let { { Text(it) } }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Email
        OutlinedTextField(
            value = state.email,
            onValueChange = { onIntent(AuthIntent.EmailChanged(it)) },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.emailError != null,
            supportingText = state.emailError?.let { { Text(it) } }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Пароль
        OutlinedTextField(
            value = state.password,
            onValueChange = { onIntent(AuthIntent.PasswordChanged(it)) },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.passwordError != null,
            supportingText = state.passwordError?.let { { Text(it) } }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Подтверждение пароля
        OutlinedTextField(
            value = state.passwordConfirmation,
            onValueChange = { onIntent(AuthIntent.PasswordConfirmationChanged(it)) },
            label = { Text("Повторите пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            isError = state.passwordConfirmationError != null,
            supportingText = state.passwordConfirmationError?.let { { Text(it) } }
        )

        // Общая ошибка
        state.generalError?.let {
            Text(
                text = it,
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
                Text("Зарегистрироваться")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Переключение на вход
        TextButton(onClick = { onIntent(AuthIntent.NavigateToLogin) }) {
            Text("Уже есть аккаунт? Войти")
        }
    }
}