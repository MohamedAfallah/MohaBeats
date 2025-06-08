package es.tierno.mohamed.aa.mohabeatsiii.data.db_nube.historial

interface HistorialDAO {
    suspend fun getHistorial(idUsuario: String): List<String>?
    suspend fun insertarHistorial(idUsuario: String, idCancion: String)
}