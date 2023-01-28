package io.gitlab.aecsocket.ignacio.core.util

class TimedCache<T>(var buffer: Long) {
    private data class Entry<T>(val at: Long, val value: T)

    private val entries = ArrayList<Entry<T>>()

    fun add(value: T) {
        val now = System.currentTimeMillis()
        // prune old values
        val threshold = now - buffer
        while (entries.isNotEmpty() && entries[0].at < threshold) {
            entries.removeAt(0)
        }

        entries.add(Entry(now, value))
    }

    fun getLast(ms: Long): List<T> {
        val result = ArrayList<T>()
        val threshold = System.currentTimeMillis() - ms
        var i = entries.size - 1
        while (i >= 0 && entries[i].at > threshold) {
            result.add(0, entries[i].value)
            i -= 1
        }
        return result
    }
}

fun TimedCache<Long>.timeNanos(block: () -> Unit): Long {
    val start = System.nanoTime()
    block()
    val end = System.nanoTime()
    val delta = end - start
    add(delta)
    return delta
}

data class TimingResults(
    val avg: Double,
    val top5: Double,
    val bottom5: Double
)

fun TimedCache<Long>.getLastResults(ms: Long): TimingResults {
    val times = getLast(ms).sorted()
    return if (times.isEmpty()) TimingResults(0.0, 0.0, 0.0)
    else TimingResults(
        times.average() / 1.0e6,
        times[(times.size * 0.95).toInt()] / 1.0e6,
        times[(times.size * 0.05).toInt()] / 1.0e6
    )
}
