package com.yks.movi.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.yks.movi.Movi
import com.yks.movi.R
import com.yks.movi.adapter.SearchResultAdapter
import com.yks.movi.databinding.FragmentSearchBinding
import com.yks.movi.footer.SearchFooterLoadStateAdapter
import com.yks.movi.utils.hideKeyboard
import com.yks.movi.viewmodel.SearchFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<SearchFragmentViewModel>()
    private val searchResultAdapter = SearchResultAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        loadStateListener()
        observeLiveData()

        binding.swipeRefreshLayout.setOnRefreshListener {
            getData()
        }

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

        binding.searchTxt.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = binding.searchTxt.text.toString()
                    Movi.QUERY = query
                    if (Movi.QUERY.isNotBlank()){
                        binding.searchTxt.hideKeyboard()
                        searchResultAdapter.submitData(lifecycle, PagingData.empty())
                        getData()
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
                searchResultAdapter.submitData(lifecycle, PagingData.empty())
                getData()
            }
        }

        searchResultAdapter.setOnItemClickListener { movieResult ->
            movieResult.id?.let {
                val bundle = bundleOf("movieId" to it)
                findNavController().navigate(R.id.action_searchFragment_to_detailsFragment, bundle)
            }
        }

    }

    private fun init(){
        binding.swipeRefreshLayout.setColorSchemeColors(Color.WHITE)
        binding.swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.rgb(0,0,0))
        binding.searchTxt.setText(Movi.QUERY)

        binding.searchRecyclerView.apply {
            this.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
            this.adapter = searchResultAdapter.withLoadStateFooter(SearchFooterLoadStateAdapter{
                searchResultAdapter.retry() })
        }
    }

    private fun visibilityOfViewComponent(it: CombinedLoadStates){
        binding.swipeRefreshLayout.isRefreshing = it.source.refresh is LoadState.Loading
        binding.errorTxt.isVisible = it.source.refresh is LoadState.Error
    }

    private fun loadStateListener(){
        searchResultAdapter.addLoadStateListener {
            visibilityOfViewComponent(it)
        }
    }

    private fun observeLiveData(){
        viewModel.searchResults.observe(viewLifecycleOwner,{ searchResult ->
            searchResult?.let {
                searchResultAdapter.submitData(lifecycle,it)
            }
        })
    }

    private fun getData(){
        viewModel.getSearchResult()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}