@file:OptIn(ExperimentalTime::class)

import kotlinx.cli.*
import kotlinx.coroutines.runBlocking
import model.QueryParams
import service.LogService
import service.LogServiceImpl
import service.MatchingService
import service.MatchingServiceImpl
import java.io.File
import java.time.LocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime
import kotlin.time.measureTimedValue

fun main(args: Array<String>) = runBlocking {
    val parser = ArgParser("Log Grep")
    val input by parser.option(ArgType.String, shortName = "i", description = "Input directory").required()
    val query by parser.option(ArgType.String, shortName = "q", description = "Query regular expression").required()
    val output by parser.option(ArgType.String, shortName = "o", description = "Output file name")
    val from by parser.option(
        ArgType.String,
        shortName = "f",
        description = "Datetime to filter from (dd/MMM/yyyy:HH:mm:ss Z)"
    )
    val to by parser.option(
        ArgType.String,
        shortName = "t",
        description = "Datetime to filter to (dd/MMM/yyyy:HH:mm:ss Z)"
    )
    val numResults by parser.option(ArgType.Int, shortName = "n", description = "Number of results to display")
        .default(10)
    parser.parse(args)

    val matchingService = getMatchingService()
    val logService = getLogService()

    val pattern = matchingService.dateTimePattern
    var fromTime: LocalDateTime? = null
    if (!from.isNullOrBlank()) {
        fromTime = LocalDateTime.parse(from, pattern)
    }

    var toTime: LocalDateTime? = null
    if (!to.isNullOrBlank()) {
        toTime = LocalDateTime.parse(to, pattern)
    }

    val logLines: MutableList<String> = mutableListOf()
    val loadTime = measureTime {
        logLines += logService.loadLogs(input)
    }

    println("Loaded logs in ${loadTime.inWholeMilliseconds}ms")

    val queryRegex = query.toRegex()
    val (results, searchTime) = measureTimedValue {
        matchingService.findResults(logLines, QueryParams(queryRegex, fromTime, toTime))
    }

    println("Found ${results.count()} matches for \"$query\" ${if (fromTime != null) "from: $fromTime" else ""} ${if (toTime != null) " to: $toTime" else ""} in ${searchTime.inWholeMilliseconds}ms")
    if (results.count() > numResults) {
        println("Displaying $numResults of ${results.count()}")
    }

    results.take(numResults).forEach {
        println(it)
    }

    if (!output.isNullOrBlank()) {
        println("Writing results to file  $output")
        File(output!!).writeText(results.joinToString("\n"))
    }
}

fun getMatchingService(): MatchingService {
    return MatchingServiceImpl()
}

fun getLogService(): LogService {
    return LogServiceImpl()
}