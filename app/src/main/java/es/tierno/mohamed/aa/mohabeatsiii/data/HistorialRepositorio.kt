package es.tierno.mohamed.aa.mohabeatsiii.data

import es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.historial.HistorialDAO
import javax.inject.Inject

class HistorialRepositorio @Inject constructor(
    private val historialDao : HistorialDAO
) {
    suspend fun getHistorial(id:String) : List<String>?{
        return historialDao.getHistorial(id)
    }

    suspend fun insertarHistorial(id:String, idCancion : String){
        historialDao.insertarHistorial(id, idCancion)
    }
}