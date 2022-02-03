package marvel.api.marvelcharacters.ui.characterlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import marvel.api.marvelcharacters.data.model.Data
import marvel.api.marvelcharacters.data.model.Results
import marvel.api.marvelcharacters.data.source.local.MarvelDatabase
import marvel.api.marvelcharacters.data.source.remote.ApiClient
import marvel.api.marvelcharacters.data.source.remote.ApiService
import marvel.api.marvelcharacters.databinding.FragmentCharListBinding
import marvel.api.marvelcharacters.databinding.ItemCharacterBinding
import marvel.api.marvelcharacters.ui.characterlist.adapter.CharacterListAdapter
import marvel.api.marvelcharacters.ui.characterlist.adapter.FooterAdapter
import marvel.api.marvelcharacters.ui.characterlist.callback.OnItemClickListener
import marvel.api.marvelcharacters.ui.characterlist.viewmodel.CharacterListViewModel
import javax.inject.Inject

/**
 * Created by Jeeban Bagdi on 1/28/2022.
 */
@ExperimentalPagingApi
@AndroidEntryPoint
class CharacterListFragment : Fragment(), OnItemClickListener {

    private var _binding: FragmentCharListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var apiService: ApiService

    @Inject
    lateinit var marvelDatabase: MarvelDatabase

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
        setUI()
    }

    private fun setUI() {
        collectUIData()
        binding.retryButton.setOnClickListener {
            collectUIData()
        }
    }

    private fun collectUIData() {
        viewLifecycleOwner.lifecycleScope.launch {
            characterListViewModel.getCharacters().distinctUntilChanged().collectLatest {
                adapter.addLoadStateListener {
                    when (it.refresh) {
                        is LoadState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.tvNoDataFound.visibility = View.GONE
                            binding.retryButton.visibility = View.GONE
                        }
                        is LoadState.NotLoading -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvNoDataFound.visibility = View.GONE
                            binding.retryButton.visibility = View.GONE
                        }
                        is LoadState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.tvNoDataFound.visibility = View.VISIBLE
                            binding.retryButton.visibility = View.VISIBLE
                        }

                    }
                }
                adapter.submitData(it)
            }
        }
    }

    private fun getMarvels() {
        viewLifecycleOwner.lifecycleScope.launch {
            characterListViewModel.getMarvels().collectLatest {
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
    override fun onItemClick(result: Results, imageView: ImageView) {
        val extras = FragmentNavigatorExtras(imageView to "image_detail")
        navigate(
            CharacterListFragmentDirections.actionCharacterListFragmentToCharacterDetailFragment(
                result = result
            ),
            extras
        )
    }

    private fun navigate(destination: NavDirections, extraInfo: FragmentNavigator.Extras) =
        with(findNavController()) {
            // 1
            currentDestination?.getAction(destination.actionId)
                ?.let {
                    navigate(destination, extraInfo) //2 }
                }
        }
}