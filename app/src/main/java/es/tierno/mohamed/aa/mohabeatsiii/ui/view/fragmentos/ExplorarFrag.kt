package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.data.provider.CategoriasProvider
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentExplorarBinding
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_categorias.AdapterCategorias
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.helper_views.ErrorDialogFragment
import es.tierno.mohamed.aa.mohabeatsiii.utils.ConexionInternet

class ExplorarFrag : Fragment() {

    private var _binding: FragmentExplorarBinding? = null
    private val binding get() = _binding!!

    private var idUsuario: String = ""

    companion object {
        private const val ARG_ID_USUARIO = "arg_id_usuario"
        fun newInstance(idUsuario: String): ExplorarFrag {
            val fragment = ExplorarFrag()
            val bundle = Bundle()
            bundle.putString(ARG_ID_USUARIO, idUsuario)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idUsuario = arguments?.getString(ARG_ID_USUARIO) ?: "invitado"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExplorarBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listaCategorias = CategoriasProvider.obtenerCategorias()

        binding.recyclerViewCategorias.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.recyclerViewCategorias.setHasFixedSize(true)
        binding.recyclerViewCategorias.clipToPadding = false
        binding.recyclerViewCategorias.setPadding(32, 0, 32, 0)

        binding.recyclerViewCategorias.adapter = AdapterCategorias(listaCategorias) { categoria ->
            if (ConexionInternet.isNetworkAvailable(requireContext())) {
                val resultadoBusquedaFrag = ResultadoBusquedaFrag.newInstance(categoria, idUsuario)

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragFullScreenContainer, resultadoBusquedaFrag)
                    .addToBackStack(null)
                    .commit()

                (parentFragment as? InicioFrag)?.showFullScreenContainer()
            } else {
                val errorMessage = "Necesitas conexión a internet para explorar las categorías. Por favor, revisa tu conexión."
                val errorDialog = ErrorDialogFragment.newInstance(errorMessage)
                errorDialog.show(parentFragmentManager, "ErrorNetworkDialog")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}