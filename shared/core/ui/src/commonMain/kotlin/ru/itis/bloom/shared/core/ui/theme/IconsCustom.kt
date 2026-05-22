package ru.itis.bloom.shared.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import bloom.shared.core.ui.generated.resources.*
import org.jetbrains.compose.resources.vectorResource

object IconsCustom {

    @Composable
    fun iconMakeupBag(): ImageVector = vectorResource(Res.drawable.ic_makeup_bag)

    @Composable
    fun iconRoutine(): ImageVector = vectorResource(Res.drawable.ic_routine)

    @Composable
    fun iconSkinDiary(): ImageVector = vectorResource(Res.drawable.ic_skin_diary)

    @Composable
    fun iconProfile(): ImageVector = vectorResource(Res.drawable.ic_profile)

    @Composable
    fun iconArrowBack(): ImageVector = vectorResource(Res.drawable.ic_arrow_back)

    @Composable
    fun iconPlus(): ImageVector = vectorResource(Res.drawable.ic_plus)
}