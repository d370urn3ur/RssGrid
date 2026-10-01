package the.autarch.newsgrid.channel.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.channel.data.ChannelRepository
import the.autarch.newsgrid.logging.AppLog

class AddChannelUseCase(
    private val channelRepository: ChannelRepository,
) {

    fun execute(url: String): Flow<TaskProgress<String>> = flow {
        emit(TaskProgress.Running())
        try {
            channelRepository.addChannel(url)
            emit(TaskProgress.Success(url))
        } catch (t: Throwable) {
            AppLog.error(t, { "Error adding channel: $url" }, AppLog.Category.DB)
            emit(TaskProgress.Failure(t))
        }
    }
}