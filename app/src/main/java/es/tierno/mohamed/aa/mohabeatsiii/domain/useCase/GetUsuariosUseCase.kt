package es.tierno.mohamed.aa.mohabeatsiii.domain.useCase

import es.tierno.mohamed.aa.mohabeatsiii.data.UsuarioRepositorio
import es.tierno.mohamed.aa.mohabeatsiii.data.mapper.toUsuario
import es.tierno.mohamed.aa.mohabeatsiii.data.provider.UsuarioProvider
import es.tierno.mohamed.aa.mohabeatsiii.domain.model.Usuario
import javax.inject.Inject

//caso de uso para obtener el listado de usuario y para verificar el login
class GetUsuariosUseCase@Inject constructor(
    private val repositorio: UsuarioRepositorio
) {
    suspend operator fun invoke() : List<Usuario>{
        var usuarios = repositorio.getUsuarios()

        if(usuarios.isNullOrEmpty()){
            repositorio.insertarUsuarios(UsuarioProvider.usuarios.map { it.toUsuario() })
            usuarios = repositorio.getUsuarios()
        }

        return usuarios
    }
}