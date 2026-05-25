package ru.itis.bloom.shared.core.navigation.api

interface DiaryNavigator {
    fun toDiaryList()
    fun toDiaryDetail(entryId: String)
    fun toDiaryCreate()
    fun toDiaryEdit(entryId: String)
    fun back()
}
