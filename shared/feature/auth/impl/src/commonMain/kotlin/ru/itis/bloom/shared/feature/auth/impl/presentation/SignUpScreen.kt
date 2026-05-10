package ru.itis.bloom.shared.feature.auth.impl.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import ru.itis.bloom.shared.core.navigation.api.AuthNavigator

@Composable
fun SignUpScreen(
    navigator: AuthNavigator,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "📝 Регистрация",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Confirm Password
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Повторите пароль") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Кнопка "Зарегистрироваться"
        Button(
            onClick = {
                // Простая валидация для теста
                if (password == confirmPassword && password.length >= 6) {
                    //navigator.toMain()
                } else {
                    // Можно показать Snackbar, но для теста просто игнорируем
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = password == confirmPassword && password.length >= 6
        ) {
            Text("Зарегистрироваться")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопка "Уже есть аккаунт?"
        TextButton(onClick = { navigator.toLoginScreen() }) {
            Text("Уже есть аккаунт? Войти")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Кнопка "Назад" (для тестов)
        TextButton(onClick = { navigator.back() }) {
            Text("← Назад")
        }
    }
}