package the.autarch.newsgrid.base

sealed interface TaskProgress<out T> {
    data object Idle: TaskProgress<Nothing>
    data class Running(val progressPercent: Int? = null): TaskProgress<Nothing>    // null is continuous (Loading)
    data class Success<out T>(val result: T): TaskProgress<T>
    data class Failure(val error: Throwable): TaskProgress<Nothing>
}