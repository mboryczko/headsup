package pl.michalboryczko.exercise.app

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


abstract class BaseActivity<T: BaseViewModel>  : DaggerAppCompatActivity() {

    @Inject lateinit var  viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: Navigator

    lateinit var viewModel : T

    inline fun <reified T: BaseViewModel> getGenericViewModel(): T {
        return ViewModelProviders.of(this, viewModelFactory).get(T::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        lifecycle.addObserver(viewModel)
        observeToastMessage()
    }

    abstract fun initViewModel()

    fun observeToastMessage(){
        viewModel.toastInfo.observe(this, Observer {
            it?.let { msg ->
                Toast.makeText(this, msg.getContentIfNotHandled(), Toast.LENGTH_LONG).show()
            }
        })

        viewModel.toastInfoResource.observe(this, Observer {
            it?.let { msg ->
                val res = msg.getContentIfNotHandled()
                if(res != null)
                    Toast.makeText(this, getString(res), Toast.LENGTH_LONG).show()

            }
        })

    }

    fun hideViews(vararg t: View)
        = t.iterator().forEach { it.visibility = View.GONE }

    fun showViews(vararg  t: View)
        = t.iterator().forEach { it.visibility = View.VISIBLE }

    fun showSnackbar(res: Int){
        Snackbar.make(window.decorView.rootView, res, Snackbar.LENGTH_LONG).show()
    }

}