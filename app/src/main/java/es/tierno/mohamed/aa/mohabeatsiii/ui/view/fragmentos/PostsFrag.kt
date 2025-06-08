package es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import es.tierno.mohamed.aa.mohabeatsiii.R
import es.tierno.mohamed.aa.mohabeatsiii.databinding.FragmentPostsBinding
import es.tierno.mohamed.aa.mohabeatsiii.ui.view.fragmentos.rv_post.AdapterPost
import es.tierno.mohamed.aa.mohabeatsiii.ui.viewModel.ViewModelPost
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostsFrag : Fragment() {

    private var _binding: FragmentPostsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ViewModelPost by viewModels()

    private lateinit var postsAdapter: AdapterPost

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postsAdapter = AdapterPost { playlistId ->
            val playlistFrag = PlayListFrag.newInstance(playlistId)
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragContainer, playlistFrag)
                .addToBackStack(null)
                .commit()
        }

        binding.recyclerViewPosts.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postsAdapter
        }

        viewModel.todosLosPostsQueHay()

        observarViewModel()
    }

    private fun observarViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.allPosts.collectLatest { postsList ->
                _binding?.let { currentBinding ->
                    currentBinding.recyclerViewPosts.visibility = if (postsList.isNotEmpty()) View.VISIBLE else View.GONE
                    currentBinding.textErrorPosts.visibility = if (postsList.isEmpty() && !viewModel.isLoading.value) View.VISIBLE else View.GONE
                    postsAdapter.submitList(postsList)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isLoading.collectLatest { isLoading ->
                _binding?.let { currentBinding ->
                    currentBinding.progressBarPosts.visibility = if (isLoading) View.VISIBLE else View.GONE
                    currentBinding.recyclerViewPosts.visibility = if (isLoading && viewModel.allPosts.value.isEmpty()) View.GONE else View.VISIBLE
                    if (viewModel.allPosts.value.isEmpty() && !isLoading) {
                        currentBinding.textErrorPosts.visibility = View.VISIBLE
                    } else {
                        if (viewModel.error.value == null) {
                            currentBinding.textErrorPosts.visibility = View.GONE
                        }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.error.collectLatest { errorMessage ->
                _binding?.let { currentBinding ->
                    errorMessage?.let {
                        currentBinding.textErrorPosts.text = it
                        currentBinding.textErrorPosts.visibility = View.VISIBLE
                        currentBinding.recyclerViewPosts.visibility = View.GONE
                    } ?: run {
                        if (!viewModel.isLoading.value && viewModel.allPosts.value.isNotEmpty()) {
                            currentBinding.textErrorPosts.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}