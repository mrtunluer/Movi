package com.yks.movi.utils

object Credentials {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val API_KEY = "yourOwnApiKey"
    const val ACTOR_MOVIE_CREDITS = "person/{person_id}"
    const val POPULAR_MOVIE_URL = "movie/popular"
    const val UPCOMING_MOVIE_URL = "movie/upcoming"
    const val MOVIE_DETAIL_URL = "movie/{movie_id}"
    const val NOW_PLAYING_MOVIE_URL = "movie/now_playing"
    const val SEARCH_URL = "search/movie"
    const val BASE_URL_TO_IMAGE = "https://image.tmdb.org/t/p/w500/"
    const val BASE_URL_TO_BACKDROP_IMAGE = "https://image.tmdb.org/t/p/w1280/"
    const val BASE_URL_TO_PROFILE_IMAGE = "https://image.tmdb.org/t/p/w185/"
    const val ORG_BASE_URL_TO_IMAGE = "https://image.tmdb.org/t/p/original/"
    const val APPEND_TO_RESPONSE = "casts,watch/providers"
    const val APPEND_TO_RESPONSE_FOR_ACTOR = "credits"
    const val MAX_CAST = 10
    const val LANGUAGE = "tr-TR"
    const val REGION = "tr"
}