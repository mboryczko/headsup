package pl.michalboryczko.exercise.ui.main

import android.arch.lifecycle.MutableLiveData
import io.reactivex.disposables.Disposable
import pl.michalboryczko.exercise.app.BaseViewModel
import pl.michalboryczko.exercise.model.ChannelSimple
import pl.michalboryczko.exercise.model.api.convertToChannelSimple
import pl.michalboryczko.exercise.model.base.Resource
import pl.michalboryczko.exercise.model.exceptions.ApiException
import pl.michalboryczko.exercise.model.exceptions.NoInternetException
import pl.michalboryczko.exercise.source.repository.ChannelRepository
import javax.inject.Inject


class MainViewModel
    @Inject constructor(private val repository : ChannelRepository) :BaseViewModel() {

    val channels: MutableLiveData<Resource<List<ChannelSimple>>> = MutableLiveData()
    private var channelDisposable: Disposable? = null


    fun getChannels(){
        channelDisposable?.dispose()
        channelDisposable = null

        channelDisposable = repository.getChannels()
                .doOnSubscribe{ channels.value = Resource.loading() }
                .map {
                    val simpleChannels = mutableListOf<ChannelSimple>()
                    it.items.forEach { simpleChannels.add(it.convertToChannelSimple()) }
                    simpleChannels
                }
                .map { it.apply { it.sortBy { it.description.length } } }
                .subscribe(
                        { channels.value = Resource.success(it) },
                        {
                            if(it is NoInternetException){
                                channels.value = Resource.error("No internet connection")
                            } else if(it is ApiException){
                                channels.value = Resource.error("")
                                toastInfo.value = it.errorMsg
                            }
                        }
                )
    }

    override fun onStop() {
        channelDisposable?.dispose()
    }

    override fun onResume() {
        val currentChannels = channels.value
        if(currentChannels == null || currentChannels.status.isError())
            getChannels()

    }

    override fun onCleared() {
        super.onCleared()
        channelDisposable?.dispose()
    }


}