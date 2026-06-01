package ru.itis.bloom.shared.feature.skindiary.api.model

enum class ProblemZone(val displayName: String) {
    FOREHEAD("Лоб"),
    NOSE("Нос"),
    CHIN("Подбородок"),
    LEFT_CHEEK("Левая щека"),
    RIGHT_CHEEK("Правая щека"),
    T_ZONE("T-зона"),
    JAWLINE("Линия челюсти"),
    NECK("Шея"),
    UNDER_EYES("Под глазами");

    companion object {
        fun fromJson(zones: String?): List<ProblemZone> {
            if (zones.isNullOrBlank()) return emptyList()
            // Парсим JSON массив ["t_zone", "forehead"]
            return try {
                val cleaned = zones.removeSurrounding("[", "]")
                    .replace("\"", "")
                    .replace(" ", "")
                if (cleaned.isBlank()) return emptyList()

                cleaned.split(",").mapNotNull { zone ->
                    entries.find { it.name.equals(zone, ignoreCase = true) }
                }
            } catch (e: Exception) {
                emptyList()
            }
        }

        fun toJson(zones: List<ProblemZone>): String {
            if (zones.isEmpty()) return ""
            return zones.joinToString(
                prefix = "[",
                postfix = "]",
                separator = ","
            ) { "\"${it.name.lowercase()}\"" }
        }
    }
}