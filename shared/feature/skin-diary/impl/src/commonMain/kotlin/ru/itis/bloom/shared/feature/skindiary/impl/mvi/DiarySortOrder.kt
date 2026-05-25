package ru.itis.bloom.shared.feature.skindiary.impl.mvi

enum class DiarySortOrder(val apiValue: String) {
    DATE_DESC("date_desc"),
    DATE_ASC("date_asc"),
    SKIN_CONDITION_DESC("skin_condition_desc")
}