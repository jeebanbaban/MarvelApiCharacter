package marvel.api.marvelcharacters.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import marvel.api.marvelcharacters.databinding.FragmentSplashBinding

/**
 * Created by Jeeban Bagdi on 1/28/2022.
 */
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val splashViewModel: SplashViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(layoutInflater)

        viewLifecycleOwner.lifecycleScope.launch {
            splashViewModel.isSplashTimeCompleted()
                .collect { findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToCharacterListFragment()) }
        }

        return binding.root
    }
}