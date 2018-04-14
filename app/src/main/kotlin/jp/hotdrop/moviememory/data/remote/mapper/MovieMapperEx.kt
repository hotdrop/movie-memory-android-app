package jp.hotdrop.moviememory.data.remote.mapper

import jp.hotdrop.moviememory.data.remote.response.MovieResponse
import jp.hotdrop.moviememory.model.Movie

fun MovieResponse.toNowPlayingMovies(): List<Movie> =
        results.map { result ->
            Movie(
                    result.id,
                    result.title,
                    result.overview,
                    Movie.Status.NowPlaying
            )
        }

fun MovieResponse.toComingSoonMovies(): List<Movie> =
        results.map { result ->
            Movie(
                    result.id,
                    result.title,
                    result.overview,
                    Movie.Status.ComingSoon
            )
        }