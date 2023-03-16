package service

interface LogService {
    fun loadLogs(logPath: String) : MutableList<String>
}