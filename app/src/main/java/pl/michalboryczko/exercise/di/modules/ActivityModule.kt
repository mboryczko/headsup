package pl.michalboryczko.exercise.di.modules

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import pl.michalboryczko.exercise.di.ViewModelKey
import pl.michalboryczko.exercise.ui.pairs.CryptocurrencyPairActivity
import pl.michalboryczko.exercise.ui.pairs.CryptocurrencyPairViewModel
import pl.michalboryczko.exercise.ui.details.CryptocurrencyDetailsActivity
import pl.michalboryczko.exercise.ui.details.CryptocurrencyDetailsViewModel
import pl.michalboryczko.exercise.ui.login.LoginActivity
import pl.michalboryczko.exercise.ui.login.LoginViewModel
import pl.michalboryczko.exercise.ui.register.RegisterActivity
import pl.michalboryczko.exercise.ui.register.RegisterViewModel
import pl.michalboryczko.exercise.ui.session.SessionActivity
import pl.michalboryczko.exercise.ui.session.SessionViewModel

@Module
internal abstract class ActivityModule {

    @ContributesAndroidInjector
    internal abstract fun bindSessionActivity(): SessionActivity

    @ContributesAndroidInjector
    internal abstract fun bindRegisterActivity(): RegisterActivity

    @ContributesAndroidInjector
    internal abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    internal abstract fun cryptocurrencyPairActivity(): CryptocurrencyPairActivity

    @ContributesAndroidInjector
    internal abstract fun cryptocurrencyDetailsActivity(): CryptocurrencyDetailsActivity


    @Binds
    @IntoMap
    @ViewModelKey(SessionViewModel::class)
    abstract fun bindSessionViewModel(viewModel: SessionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(RegisterViewModel::class)
    abstract fun bindRegisterViewModel(viewModel: RegisterViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CryptocurrencyPairViewModel::class)
    abstract fun bindCryptocurrencyPairViewModel(viewModel: CryptocurrencyPairViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(CryptocurrencyDetailsViewModel::class)
    abstract fun bindCryptocurrencyDetailsViewModel(viewModel: CryptocurrencyDetailsViewModel): ViewModel

}