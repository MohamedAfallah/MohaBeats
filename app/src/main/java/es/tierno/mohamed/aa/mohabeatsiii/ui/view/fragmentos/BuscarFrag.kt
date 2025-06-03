package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentBuscarBinding

class BuscarFrag : Fragment() {

    private var _binding: FragmentBuscarBinding? = null
    private val binding get() = _binding!!

    private var buscarFocusListener: BuscarFocusListener? = null
    private var buscarQueryListener: BuscarQueryListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBuscarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchView.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                buscarFocusListener?.onSearchViewFocused()
            } else {
                buscarFocusListener?.onSearchViewFocusLost()
            }
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("PSG", "onQueryTextChange: $newText")
                buscarQueryListener?.onQueryTextChanged(newText.orEmpty())
                return true
            }
        })
    }

    fun setBuscarFocusListener(listener: BuscarFocusListener) {
        this.buscarFocusListener = listener
    }

    fun setBuscarQueryListener(listener: BuscarQueryListener) {
        this.buscarQueryListener = listener
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface BuscarFocusListener {
        fun onSearchViewFocused()
        fun onSearchViewFocusLost()
    }

    interface BuscarQueryListener {
        fun onQueryTextChanged(query: String)
    }
}

