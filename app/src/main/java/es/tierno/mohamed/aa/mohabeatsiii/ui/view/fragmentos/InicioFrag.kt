package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Guideline
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentInicioBinding

@AndroidEntryPoint
class InicioFrag : Fragment(), BuscarFrag.BuscarQueryListener {

    private var _binding: FragmentInicioBinding? = null
    private val binding get() = _binding!!

    private lateinit var guidelineHalf: Guideline
    private lateinit var rootLayout: ConstraintLayout

    private var guidelinePercent = 0.5f
    private var idUsuario: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInicioBinding.inflate(inflater, container, false)
        rootLayout = binding.root
        guidelineHalf = rootLayout.findViewById(R.id.guidelineHalf)

        idUsuario = arguments?.getString("idUsuario") ?: "invitado"

        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .replace(binding.fragExContainer.id, ExplorarFrag.newInstance(idUsuario))
                .commit()

            childFragmentManager.beginTransaction()
                .replace(binding.fragCancionesContainer.id, CancionesFrag.newInstance(idUsuario))
                .commit()

            val buscarFrag = BuscarFrag()
            buscarFrag.setBuscarQueryListener(this)

            childFragmentManager.beginTransaction()
                .replace(binding.fragBuscadorContainer.id, buscarFrag)
                .commit()

            binding.fragBuscadorContainer.visibility = View.VISIBLE
        }

        setupTouchListener()

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouchListener() {
        binding.fragCancionesContainer.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    animateGuideline(0f)
                    v.performClick()
                    true
                }
                else -> false
            }
        }
    }

    private fun animateGuideline(newPercent: Float) {
        val transition = ChangeBounds()
        transition.duration = 400
        TransitionManager.beginDelayedTransition(rootLayout, transition)
        guidelineHalf.setGuidelinePercent(newPercent)
        guidelinePercent = newPercent
    }

    fun showFullScreenContainer() {
        binding.fragExContainer.visibility = View.GONE
        binding.fragCancionesContainer.visibility = View.GONE
        binding.fragFullScreenContainer.visibility = View.VISIBLE
    }

    fun hideFullScreenContainer() {
        binding.fragExContainer.visibility = View.VISIBLE
        binding.fragCancionesContainer.visibility = View.VISIBLE
        binding.fragFullScreenContainer.visibility = View.GONE
    }

    override fun onQueryTextChanged(query: String) {
        if (query.isNotEmpty()) {
            binding.resultadoBusquedaTiempoReal.visibility = View.VISIBLE

            val fragment = ResultadoBusquedaFrag.newInstanceTexto(query, idUsuario)

            childFragmentManager.beginTransaction()
                .replace(binding.resultadoBusquedaTiempoReal.id, fragment)
                .commit()
        } else {
            binding.resultadoBusquedaTiempoReal.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}