package the.autarch.newsgrid.entry.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import the.autarch.newsgrid.base.TaskProgress
import the.autarch.newsgrid.channel.data.ChannelRepository
import the.autarch.newsgrid.entry.data.EntryEntity

class GetEntryUseCase(private val channelRepo: ChannelRepository) {

    fun execute(entryId: String): Flow<TaskProgress<EntryEntity>> = flow {
        emit(TaskProgress.Running())
        when (val result = channelRepo.getEntry(entryId)) {
            null -> emit(TaskProgress.Failure(RuntimeException("no entry with id $entryId exists")))
            else -> emit(TaskProgress.Success(result))
        }
    }
}