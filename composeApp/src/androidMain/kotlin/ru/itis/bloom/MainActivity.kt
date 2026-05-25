package ru.itis.bloom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin
import ru.itis.bloom.shared.core.data.di.commonDatabaseModule
import ru.itis.bloom.shared.core.data.di.networkModule
import ru.itis.bloom.shared.core.data.di.platformModule
import ru.itis.bloom.shared.core.data.di.qualifierModule
import ru.itis.bloom.shared.core.navigation.impl.di.navigationModule
import ru.itis.bloom.shared.feature.auth.impl.di.authModule
import ru.itis.bloom.shared.feature.auth.impl.di.authNavModule
import ru.itis.bloom.shared.feature.makeupbag.impl.di.makeupBagModule
import ru.itis.bloom.shared.feature.makeupbag.impl.di.makeupBagNavModule
import ru.itis.bloom.shared.feature.skindiary.impl.di.diaryModule
import ru.itis.bloom.shared.feature.skindiary.impl.di.diaryNavModule

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Инициализация Koin (при старте приложения)
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                androidContext(this@MainActivity.applicationContext)
                modules(
                    qualifierModule,

                    platformModule,
                    commonDatabaseModule,
                    networkModule,

                    navigationModule,

                    authNavModule,
                    authModule,

                    makeupBagModule,
                    makeupBagNavModule,

                    diaryModule(),
                    diaryNavModule
                    // ... другие модули фич
                )
            }
        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}