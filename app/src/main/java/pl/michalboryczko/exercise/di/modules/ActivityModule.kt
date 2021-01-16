package pl.michalboryczko.exercise.di.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import pl.michalboryczko.exercise.di.ViewModelKey
import pl.michalboryczko.exercise.ui.login.SampleActivity
import pl.michalboryczko.exercise.ui.login.SampleViewModel

@Module
internal abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun bindLoginActivity(): SampleActivity

    @Binds
    @IntoMap
    @ViewModelKey(SampleViewModel::class)
    abstract fun bindLoginViewModel(viewModel: SampleViewModel): ViewModel

}