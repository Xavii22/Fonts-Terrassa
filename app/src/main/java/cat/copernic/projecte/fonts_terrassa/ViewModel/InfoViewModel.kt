package cat.copernic.projecte.fonts_terrassa.ViewModel

import androidx.lifecycle.MutableLiveData

class InfoViewModel {
    val darkModeEnabled: MutableLiveData<Boolean> by lazy{
        MutableLiveData<Boolean>()
    }
}