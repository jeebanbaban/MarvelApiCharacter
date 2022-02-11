package marvel.api.marvelcharacters.ui.characterdetail

import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import marvel.api.marvelcharacters.databinding.FragmentCharDetailBinding

/**
 * Created by Jeeban Bagdi on 1/28/2022.
 */
class CharacterDetailFragment: Fragment() {

    private var _binding : FragmentCharDetailBinding? = null
    private val binding get() = _binding!!

    private val navArgs: CharacterDetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.explode)
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCharDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.results = navArgs.result
    }
}