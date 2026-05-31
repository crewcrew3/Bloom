package ru.itis.bloom.shared.core.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import bloom.shared.core.ui.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import java.awt.FileDialog
import java.awt.Frame
import javax.swing.SwingUtilities

@Composable
actual fun rememberImagePicker(
    callback: ImagePickerCallback
): () -> Unit {

    val selectImageTitle = stringResource(Res.string.title_select_image)

    return remember {
        {
            SwingUtilities.invokeLater {
                val frame = Frame()
                val fileDialog = FileDialog(frame, selectImageTitle, FileDialog.LOAD).apply {
                    setFilenameFilter { _, name ->
                        name.endsWith(".jpg", ignoreCase = true) ||
                                name.endsWith(".jpeg", ignoreCase = true) ||
                                name.endsWith(".png", ignoreCase = true) ||
                                name.endsWith(".webp", ignoreCase = true)
                    }
                }
                fileDialog.isVisible = true

                val selectedFile = fileDialog.files.firstOrNull()
                callback.onImageSelected(selectedFile?.toURI()?.toString())
                frame.dispose()
            }
        }
    }
}