package es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import es.tierno.mohamed.aa.mohabeatsiii.domain.useCase.Musica.GetCancionesPorGeneroUseCase
import javax.inject.Inject

@HiltViewModel
class ExplorarViewModel @Inject constructor(
    private val getCancionesPorGeneroUseCase: GetCancionesPorGeneroUseCase
) : ViewModel(){

}