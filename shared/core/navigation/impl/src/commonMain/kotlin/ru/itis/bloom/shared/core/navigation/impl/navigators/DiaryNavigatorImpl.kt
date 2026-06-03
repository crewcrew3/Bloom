package ru.itis.bloom.shared.core.navigation.impl.navigators

import ru.itis.bloom.shared.core.navigation.api.DiaryNavigator
import ru.itis.bloom.shared.core.navigation.impl.BackStackHolder
import ru.itis.bloom.shared.feature.skindiary.api.navigation.DiaryNavRoute

class DiaryNavigatorImpl(
    private val backStackHolder: BackStackHolder
) : DiaryNavigator {
    override fun toDiaryList() {
        backStackHolder.backStack?.add(DiaryNavRoute.List)
    }

    override fun toDiaryDetail(entryId: String) {
        backStackHolder.backStack?.add(DiaryNavRoute.Detail(entryId))
    }

    override fun toDiaryCreate() {
        backStackHolder.backStack?.add(DiaryNavRoute.Create)
    }

    override fun toDiaryEdit(entryId: String) {
        backStackHolder.backStack?.add(DiaryNavRoute.Edit(entryId))
    }

    override fun back() {
        backStackHolder.backStack?.removeLastOrNull()
    }
}