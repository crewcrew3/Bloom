package ru.itis.bloom.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import bloom.composeapp.generated.resources.Res
import bloom.composeapp.generated.resources.ic_arrow_back
import bloom.composeapp.generated.resources.ic_makeup_bag
import bloom.composeapp.generated.resources.ic_profile
import bloom.composeapp.generated.resources.ic_routine
import bloom.composeapp.generated.resources.ic_skin_diary
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
}