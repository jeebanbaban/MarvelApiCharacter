package marvel.api.marvelcharacters.ui.characterlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import marvel.api.marvelcharacters.data.model.Data
import marvel.api.marvelcharacters.data.model.Results
import marvel.api.marvelcharacters.data.source.local.MarvelDatabase
import marvel.api.marvelcharacters.data.source.remote.ApiClient
import marvel.api.marvelcharacters.databinding.FragmentCharListBinding
import marvel.api.marvelcharacters.ui.characterlist.adapter.CharacterListAdapter
import marvel.api.marvelcharacters.ui.characterlist.adapter.FooterAdapter
import marvel.api.marvelcharacters.ui.characterlist.callback.OnItemClickListener
import marvel.api.marvelcharacters.ui.characterlist.viewmodel.CharacterListViewModel

/**
 * Created by Jeeban Bagdi on 1/28/2022.
 */
@ExperimentalPagingApi
class CharacterListFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentCharListBinding? = null
    private val binding get() = _binding!!

    private val characterListViewModel: CharacterListViewModel by viewModels()
    private lateinit var adapter: CharacterListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        collectUIData()
    }

    private fun collectUIData() {
        viewLifecycleOwner.lifecycleScope.launch {
            characterListViewModel.getCharacters().distinctUntilChanged().collectLatest {
                adapter.addLoadStateListener {
                    if (it.refresh is LoadState.Loading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                }
                adapter.submitData(it)
            }
        }
    }


    private fun getCharacterResults() {
        viewLifecycleOwner.lifecycleScope.launch {
            characterListViewModel.getResults(
                ApiClient.getService(),
                MarvelDatabase.getInstance(requireContext())
            ).collect {
                adapter.addLoadStateListener {
                    if (it.refresh is LoadState.Loading) {
                        binding.progressBar.visibility = View.VISIBLE
                    } else {
                        binding.progressBar.visibility = View.GONE
                    }
                }
                adapter.submitData(it)
            }
        }
    }

    private fun getCharacters() {
        viewLifecycleOwner.lifecycleScope.launch {
            characterListViewModel.getCharacters(
                ApiClient.getService(),
                MarvelDatabase.getInstance(requireContext())
            ).collectLatest {
                adapter.addLoadStateListener { state ->
                    when (state.refresh) {
                        is LoadState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is LoadState.NotLoading -> {
                            binding.progressBar.visibility = View.GONE
                        }
                        is LoadState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Paging data Error Occured",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }

                    }
                }
                adapter.submitData(it)
            }
        }
    }

    private fun getMarvels() {
        viewLifecycleOwner.lifecycleScope.launch {
            characterListViewModel.getMarvels(
                ApiClient.getService(),
                MarvelDatabase.getInstance(requireContext())
            ).collectLatest {
                adapter.submitData(it)
                adapter.addLoadStateListener { state ->
                    when (state.refresh) {
                        is LoadState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        is LoadState.NotLoading -> {
                            binding.progressBar.visibility = View.GONE
                        }
                        is LoadState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(
                                requireContext(),
                                "Paging data Error Occured",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }

                    }
                }
            }
        }
    }


    private fun initView() {
        adapter = CharacterListAdapter(this)
        binding.rvChacterList.adapter = adapter.withLoadStateFooter(
            footer = FooterAdapter { adapter.retry() }
        )
    }

    //on Character Item Click
    override fun onItemClick(result: Results) {
        findNavController().navigate(CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(result = result))
    }
}