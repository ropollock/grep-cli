package service

import model.QueryParams
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class MatchingServiceImpl : MatchingService {

    companion object {
        val ACCESS_LOG_REGEX =
            """^(\S+) (\S+) (\S+) \[([\w:/]+\s[+\-]\d{4})\] (\S+) (\S+)\s*(\S+)?\s* (\d{3}) (\S+)""".toRegex()
        val DATETIME_PATTERN = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ss Z")
    }

    override val dateTimePattern: DateTimeFormatter
        get() = DATETIME_PATTERN

    override fun findResults(logLines: MutableList<String>, queryParams: QueryParams): List<String> {
        return logLines.filter {
            matchLine(it, queryParams)
        }
    }

    private fun matchLine(logLine: String, queryParams: QueryParams): Boolean {
        var match = true
        val matchResult = ACCESS_LOG_REGEX.find(logLine)
        val logGroups = matchResult?.groupValues
        val logDateTime = logGroups?.get(4)?.let {
            try {
                return@let LocalDateTime.parse(it, DATETIME_PATTERN)
            } catch (e: DateTimeParseException) {
                // Trying to filter by date but date is not parsable, so lets discard this result.
                if (queryParams.toTime != null || queryParams.fromTime != null) {
                    return false
                }
            }
            null
        }

        val searchGroups = logGroups?.toMutableList()?.apply {
            removeAt(0)
            removeAt(3)
        }
        val searchLog = searchGroups?.joinToString(" ") ?: logLine

        if (queryParams.toTime != null) {
            if (logDateTime == null) {
                return false
            }

            if (queryParams.toTime < logDateTime) {
                match = false
            }
        }

        if (queryParams.fromTime != null) {
            if (logDateTime == null) {
                return false
            }
            if (queryParams.fromTime > logDateTime) {
                match = false
            }
        }

        if (!queryParams.queryRegex.containsMatchIn(searchLog)) {
            match = false
        }

        return match
    }
}