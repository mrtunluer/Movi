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
import com.yks.movi.adapter.MovieStarringAdapter
import com.yks.movi.databinding.FragmentActorBinding
import com.yks.movi.model.ActorProfile
import com.yks.movi.model.MovieResult
import com.yks.movi.status.Status
import com.yks.movi.utils.Credentials
import com.yks.movi.utils.downloadFromUrl
import com.yks.movi.viewmodel.ActorFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ActorFragment : Fragment() {

    private var _binding: FragmentActorBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<ActorFragmentViewModel>()
    private val movieStarringAdapter = MovieStarringAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        observeLiveData()

        binding.progressLayout.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getActorProfile()
        }

        binding.backBtn.setOnClickListener {
            it.findNavController().popBackStack()
        }

    }

    private fun init(){
        binding.progressLayout.swipeRefreshLayout.setColorSchemeColors(Color.WHITE)
        binding.progressLayout.swipeRefreshLayout.setProgressBackgroundColorSchemeColor(Color.rgb(0,0,0))
        initMovieStarringRecyclerView()
    }

    private fun initMovieStarringRecyclerView(){
        binding.movieStarringRecyclerView.apply {
            this.adapter = movieStarringAdapter
            this.layoutManager = LinearLayoutManager(
                requireContext(), LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun getActorMovies(movieResultList: ArrayList<MovieResult>?){
        movieResultList?.let {
            movieStarringAdapter.submitList(it)
        }
    }

    private fun observeLiveData(){
        viewModel.actorProfile.observe(viewLifecycleOwner,{ actorProfile ->
            actorProfile?.let {
                when(it.status){
                    Status.SUCCESS -> {
                        it.data?.let { actorProfile ->
                            visibilityOfViewComponents(actorProfile)
                            getActorMovies(actorProfile.credits?.cast)
                            displayData(actorProfile)
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

    private fun displayData(actorProfile: ActorProfile?) {
        actorProfile?.profilePath?.let {
            binding.profileImageView.downloadFromUrl(Credentials.BASE_URL_TO_PROFILE_IMAGE.plus(it),requireContext())
        }?:binding.profileImageView.setImageResource(R.drawable.profile)

        binding.name.text = actorProfile?.name
        binding.biography.text = actorProfile?.biography
        binding.placeOfBirthTxt.text = actorProfile?.placeOfBirth

        if (actorProfile?.birthday != null && actorProfile.birthday.isNotBlank()){
            binding.birthdayTxt.text = convertDate(actorProfile.birthday)
        }

    }

    private fun visibilityOfViewComponents(actorProfile: ActorProfile?){
        if (actorProfile?.biography == null || actorProfile.biography.isEmpty()){
            binding.biography.isVisible = false
            binding.biographyHeader.isVisible = false
        }
        if (actorProfile?.placeOfBirth == null || actorProfile.placeOfBirth.isEmpty()){
            binding.placeOfBirthCard.visibility = View.GONE
        }
        if (actorProfile?.birthday == null || actorProfile.birthday.isEmpty()){
            binding.birthdayCard.visibility = View.GONE
        }
    }


    private fun convertDate(date: String): String{
        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
        return formatter.format(parser.parse(date)!!)
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