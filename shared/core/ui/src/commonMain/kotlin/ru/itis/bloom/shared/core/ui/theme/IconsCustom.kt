package ru.itis.bloom.shared.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import bloom.shared.core.ui.generated.resources.*
import org.jetbrains.compose.resources.painterResource

object IconsCustom {

    @Composable
    fun iconMakeupBag(): Painter = painterResource(Res.drawable.ic_makeup_bag)

    @Composable
    fun iconRoutine(): Painter = painterResource(Res.drawable.ic_routine)

    @Composable
    fun iconSkinDiary(): Painter = painterResource(Res.drawable.ic_skin_diary)

    @Composable
    fun iconProfile(): Painter = painterResource(Res.drawable.ic_profile)

    @Composable
    fun iconArrowBack(): Painter = painterResource(Res.drawable.ic_arrow_back)

    @Composable
    fun iconPlus(): Painter = painterResource(Res.drawable.ic_plus)

    @Composable
    fun iconPlaceholderProduct(): Painter = painterResource(Res.drawable.ic_placeholder_product)

    @Composable
    fun iconBurgerMenu(): Painter = painterResource(Res.drawable.ic_menu)

    @Composable
    fun iconStarFilled(): Painter = painterResource(Res.drawable.ic_star_filled)

    @Composable
    fun iconStarOutline(): Painter = painterResource(Res.drawable.ic_star_outline)

    @Composable
    fun iconExpand(): Painter = painterResource(Res.drawable.ic_expand)

    @Composable
    fun iconEdit(): Painter = painterResource(Res.drawable.ic_edit)

    @Composable
    fun iconDelete(): Painter = painterResource(Res.drawable.ic_delete)
}