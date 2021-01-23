package pl.michalboryczko.exercise.model.models.base

import io.reactivex.disposables.Disposable
import timber.log.Timber


operator fun Disposables.plusAssign(disposable: Disposable) = add(disposable)
operator fun Disposables.minus(disposable: Disposable) = add(disposable)

class Disposables{
    private val disposables: MutableList<Disposable> = mutableListOf()
    private var uniqueDisposable: Disposable? = null

    fun addAsUniqueDisposable(disposable: Disposable){
        uniqueDisposable?.dispose()
        uniqueDisposable = disposable
        add(disposable)
    }

    fun disposeAll(){
        disposables
                .filter { !it.isDisposed }
                .forEach {
                    it.dispose()
                    Timber.d("disposing $it")
                }
    }

    fun disposeUniqueDisposable(){
        uniqueDisposable?.dispose()
    }

    fun add(disposable: Disposable) {
        if(!disposables.contains(disposable))
            disposables.add(disposable)
    }

    fun dispose(disposable: Disposable) {
        if(disposables.contains(disposable))
            disposables.remove(disposable)
    }

}