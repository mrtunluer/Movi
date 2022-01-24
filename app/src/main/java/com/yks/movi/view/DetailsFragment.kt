package com.yks.movi.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yks.movi.R
import com.yks.movi.adapter.CastAdapter
import com.yks.movi.adapter.MovieGenreAdapter
import com.yks.movi.adapter.ProviderAdapter
import com.yks.movi.databinding.FragmentDetailsBinding
import com.yks.movi.model.*
import com.yks.movi.status.Status
import com.yks.movi.utils.Credentials
import com.yks.movi.utils.downloadFromUrl
import com.yks.movi.viewmodel.DetailsFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class DetailsFragment: Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<DetailsFragmentViewModel>()
    private var movieGenreAdapter = MovieGenreAdapter()
    private var castAdapter = CastAdapter()
    private var providerAdapter = ProviderAdapter()



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeLiveData()

        binding.progressLayout.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getMovieDetails()
        }

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

    }

    private fun init() {
        binding.progressLayout.swipeRefreshLayout.setColorSchemeColors(Color.WHITE)
        binding.progressLayout.swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.rgb(0,0,0))
        initCastRecyclerView()
        initProviderRecyclerView()
        initGenreRecyclerView()
    }

    private fun initCastRecyclerView(){
        binding.castRecyclerView.apply {
            this.adapter = castAdapter
            this.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun initProviderRecyclerView(){
        binding.providerRecyclerView.apply {
            this.adapter = providerAdapter
            this.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun initGenreRecyclerView(){
        binding.genreRecyclerView.apply {
            this.adapter = movieGenreAdapter
            this.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun displayData(details: MovieDetails?){
        details?.backdropPath?.let {
            binding.backdropPath.downloadFromUrl(Credentials.BASE_URL_TO_BACKDROP_IMAGE.plus(it),requireContext())
        }?:binding.backdropPath.setImageResource(R.drawable.error)

        binding.movieName.text = details?.title
        binding.voteAverageTxt.text = details?.voteAverage.toString()

        if (details?.releaseDate != null && details.releaseDate.isNotBlank()){
            binding.releaseDate.text = convertDate(details.releaseDate)
        }

        binding.originalLanguage.text = details?.originalLanguage
        binding.overView.text = details?.overview
        binding.runtime.text = details?.runtime.toString()

        binding.revenue.text = getString(R.string.revenue).plus(" | ${details?.revenue}")
        binding.budget.text = getString(R.string.budget).plus(" | ${details?.budget}")

    }

    private fun convertDate(date: String): String{
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return formatter.format(parser.parse(date)!!)
    }

    private fun getGenre(genreList: ArrayList<Genre>?){
        genreList?.let {
            movieGenreAdapter.submitList(it)
        }
    }

    private fun getCast(castList: ArrayList<Cast>?){
        castList?.let {
            val displayCast = if (it.size <= Credentials.MAX_CAST) it.size else Credentials.MAX_CAST
            castAdapter.submitList(it.take(displayCast))
        }
    }

    private fun getProvider(flatRateList: ArrayList<FlatRate>?){
        flatRateList?.let {
            binding.providerTxt.isVisible = true
            providerAdapter.submitList(it)
        }
    }

    private fun visibilityOfViewComponents(details: MovieDetails?){
        if (details?.budget == null || details.budget.toString() == "0"){
            binding.budget.isVisible = false
        }
        if (details?.revenue == null || details.revenue.toString() == "0"){
            binding.revenue.isVisible = false
        }
        if (details?.overview == null || details.overview.isEmpty()){
            binding.overViewHeader.visibility = View.GONE
            binding.overView.visibility = View.GONE
        }
        if (details?.casts?.cast == null || details.casts.cast.size == 0){
            binding.castsTxt.visibility = View.GONE
        }
    }

    private fun observeLiveData(){
        viewModel.movieDetails.observe(viewLifecycleOwner, {movieDetails ->
            movieDetails?.let {
                when(it.status){
                    Status.SUCCESS -> {
                        it.data?.let {detailsData ->
                            visibilityOfViewComponents(detailsData)
                            getProvider(detailsData.watchProviders?.results?.tr?.flatRate)
                            getCast(detailsData.casts?.cast)
                            getGenre(detailsData.genres)
                            displayData(detailsData)
                            onSuccess()
                        }
                    }
                    Status.ERROR -> {
                        onError()
                    }
                    Status.LOADING -> {
                        onLoading()
                    }
                }
            }
        })
    }

    private fun onError(){
        binding.progressLayout.root.isVisible = true
        binding.progressLayout.errorTxt.isVisible = true
        binding.progressLayout.lottieAnimationView.isVisible = true
        binding.progressLayout.swipeRefreshLayout.isRefreshing = false
    }

    private fun onLoading(){
        binding.progressLayout.root.isVisible = true
        binding.progressLayout.errorTxt.isVisible = false
        binding.progressLayout.lottieAnimationView.isVisible = false
        binding.progressLayout.swipeRefreshLayout.isRefreshing = true
    }

    private fun onSuccess(){
        binding.progressLayout.root.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}