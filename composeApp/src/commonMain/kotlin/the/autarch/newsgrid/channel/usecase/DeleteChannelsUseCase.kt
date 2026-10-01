package the.autarch.newsgrid.channel.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.channel.data.ChannelEntity
import the.autarch.newsgrid.channel.data.ChannelRepository

class DeleteChannelsUseCase(val channelRepo: ChannelRepository) {

    fun execute(channels: List<ChannelEntity>): Flow<TaskProgress<Unit>> = flow {
        channelRepo.deleteChannels(channels)
        emit(TaskProgress.Success(Unit))
    }
}