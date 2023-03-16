package model

import java.time.LocalDateTime

data class QueryParams(
    val queryRegex: Regex,
    val fromTime: LocalDateTime?,
    val toTime: LocalDateTime?
)