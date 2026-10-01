package the.autarch.newsgrid.channel.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.channel.data.ChannelRepository

class MoveChannelUseCase(private val channelRepo: ChannelRepository) {

    fun execute(fromIndex: Int, toIndex: Int): Flow<TaskProgress<Unit>> = flow {
        channelRepo.moveChannel(fromIndex, toIndex)
    }
}