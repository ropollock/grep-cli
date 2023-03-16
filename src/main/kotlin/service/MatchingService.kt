package service

import model.QueryParams
import java.time.format.DateTimeFormatter

interface MatchingService {
    val dateTimePattern : DateTimeFormatter
    fun findResults(logLines: MutableList<String>, queryParams: QueryParams) : List<String>
}