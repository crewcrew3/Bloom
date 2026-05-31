package ru.itis.bloom.shared.core.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import bloom.shared.core.ui.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource

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
    fun iconCheck(): Painter = rememberVectorPainter(image = vectorResource(Res.drawable.ic_check))

    @Composable
    fun iconFilter(): Painter = rememberVectorPainter(image = vectorResource(Res.drawable.ic_filter_fill))

    @Composable
    fun iconSortAsc(): Painter = rememberVectorPainter(image = vectorResource(Res.drawable.ic_sort_asc))

    @Composable
    fun iconSkinCondition(): Painter = rememberVectorPainter(image = vectorResource(Res.drawable.ic_check))

    @Composable
    fun iconCamera(): Painter = rememberVectorPainter(image = vectorResource(Res.drawable.ic_check))

    @Composable
    fun iconDiaryEmpty(): Painter = rememberVectorPainter(image = vectorResource(Res.drawable.ic_check))

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

    @Composable
    fun iconCalendar(): Painter = painterResource(Res.drawable.ic_calendar)
}