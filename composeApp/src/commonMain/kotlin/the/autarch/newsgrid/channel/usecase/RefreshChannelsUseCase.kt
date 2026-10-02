package the.autarch.newsgrid.channel.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.until
import the.autarch.newsgrid.LAST_UPDATE
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.channel.data.ChannelRepository
import kotlin.time.Clock
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Instant

class RefreshChannelsUseCase(
    private val channelRepo: ChannelRepository,
    private val prefs: DataStore<Preferences>
) {

    companion object {
        val REFRESH_THRESHOLD = 10.minutes
        private val mutex = Mutex()
    }

    fun execute(forceRefresh: Boolean = false): Flow<TaskProgress<Unit>> = flow {
        mutex.withLock {

            if (!forceRefresh) {
                val lastUpdateMillis = prefs.data.map {
                    it[LAST_UPDATE] ?: Instant.DISTANT_PAST.toEpochMilliseconds()
                }.first()
                val lastUpdate = Instant.fromEpochMilliseconds(lastUpdateMillis)
                val diffMinutes =
                    lastUpdate.until(Clock.System.now(), DateTimeUnit.MINUTE, TimeZone.UTC)
                if (diffMinutes.minutes < REFRESH_THRESHOLD) {
                    emit(TaskProgress.Success(Unit))
                    return@flow
                }
            }

            channelRepo.refreshChannels()

            prefs.edit { settings ->
                settings[LAST_UPDATE] = Clock.System.now().toEpochMilliseconds()
            }

            emit(TaskProgress.Success(Unit))
        }
    }
}