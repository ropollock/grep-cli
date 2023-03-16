package service

import java.io.File

class LogServiceImpl : LogService {

    companion object {
        const val LOG_EXTENSION = ".log"
    }

    override fun loadLogs(logPath: String) : MutableList<String> {
        val logLines: MutableList<String> = mutableListOf()
        getLogFiles(logPath).forEach {
            logLines += it.bufferedReader().readLines()
        }
        return logLines
    }

    private fun getLogFiles(logPath: String) : Sequence<File> {
        val logFiles = File(logPath).walkTopDown().filter { it.name.endsWith(LOG_EXTENSION) }
        logFiles.forEach {
            println("Loading log $it")
        }
        return logFiles
    }
}