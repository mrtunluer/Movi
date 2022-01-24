package com.yks.movi.view
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.yks.movi.Movi
import com.yks.movi.adapter.PopularMovieAdapter
import com.yks.movi.footer.MovieFooterLoadStateAdapter
import com.yks.movi.adapter.NowPlayingMovieAdapter
import com.yks.movi.adapter.UpcomingMovieAdapter
import com.yks.movi.databinding.FragmentMainBinding
import com.yks.movi.utils.hideKeyboard
import com.yks.movi.viewmodel.MainFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<MainFragmentViewModel>()
    private var popularMovieAdapter = PopularMovieAdapter()
    private var upcomingMovieAdapter = UpcomingMovieAdapter()
    private var nowPlayingMovieAdapter = NowPlayingMovieAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        _binding = FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        loadStateListener()
        observeLiveData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            getData()
        }

        binding.searchTxt.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = binding.searchTxt.text.toString()
                    Movi.QUERY = query
                    if (Movi.QUERY.isNotBlank()){
                        binding.searchTxt.hideKeyboard()
                        goSearchFragment()
                        return true
                    }
                }
                return false
            }
        })

        binding.searchImg.setOnClickListener {
            val query = binding.searchTxt.text.toString()
            Movi.QUERY = query
            if (Movi.QUERY.isNotBlank()){
                binding.searchTxt.hideKeyboard()
                goSearchFragment()
            }
        }

    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        binding.searchTxt.setText(Movi.QUERY)
    }

    private fun goSearchFragment(){
        val action = MainFragmentDirections.actionMainFragmentToSearchFragment()
        view?.findNavController()?.navigate(action)
    }

    private fun getData(){
        viewModel.getPopularMovieData()
        viewModel.getUpcomingMovieData()
        viewModel.getNowPlayingMovieData()
    }

    private fun visibilityOfViewComponent(it: CombinedLoadStates){
        binding.swipeRefreshLayout.isRefreshing = it.source.refresh is LoadState.Loading
        binding.errorTxt.isVisible = it.source.refresh is LoadState.Error
    }

    private fun loadStateListener(){
        popularMovieAdapter.addLoadStateListener {
            visibilityOfViewComponent(it)
        }
        upcomingMovieAdapter.addLoadStateListener {
            visibilityOfViewComponent(it)
        }
        nowPlayingMovieAdapter.addLoadStateListener {
            visibilityOfViewComponent(it)
        }
    }

    private fun init() {
        binding.swipeRefreshLayout.setColorSchemeColors(Color.WHITE)
        binding.swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.rgb(0,0,0))

        binding.popularRecyclerView.apply {
            this.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            this.adapter = popularMovieAdapter.withLoadStateFooter(MovieFooterLoadStateAdapter{
                popularMovieAdapter.retry() })
        }

        binding.upcomingRecyclerView.apply {
            this.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            this.adapter = upcomingMovieAdapter.withLoadStateFooter(MovieFooterLoadStateAdapter{
                upcomingMovieAdapter.retry() })
        }

        binding.nowPlayingRecyclerView.apply {
            this.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            this.adapter = nowPlayingMovieAdapter.withLoadStateFooter(MovieFooterLoadStateAdapter{
                nowPlayingMovieAdapter.retry() })
        }

    }

    private fun observeLiveData(){
        viewModel.popularMovieResults.observe(viewLifecycleOwner, { popularMovieResult ->
            popularMovieResult?.let {
                popularMovieAdapter.submitData(lifecycle,it)
            }
        })
        viewModel.upcomingMovieResults.observe(viewLifecycleOwner, { upcomingMovieResult ->
            upcomingMovieResult?.let {
                upcomingMovieAdapter.submitData(lifecycle,it)
            }
        })
        viewModel.nowPlayingMovieResults.observe(viewLifecycleOwner, { nowPlayingMovieResult ->
            nowPlayingMovieResult?.let {
                nowPlayingMovieAdapter.submitData(lifecycle,it)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}