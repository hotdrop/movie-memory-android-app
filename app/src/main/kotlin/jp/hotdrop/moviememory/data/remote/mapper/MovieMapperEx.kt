package jp.hotdrop.moviememory.data.remote.mapper

import jp.hotdrop.moviememory.data.remote.response.MovieResponse
import jp.hotdrop.moviememory.model.Movie
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

fun MovieResponse.toNowPlayingMovies(): List<Movie> =
        results.map { result ->
            val playingDate = result.playingDate?.let { LocalDate.parse(it) }
            val createAt = result.createdAt.let { LocalDateTime.parse(it) }
            Movie(
                    result.id,
                    result.title,
                    result.overview,
                    result.imageUrl,
                    playingDate,
                    "",
                    "",
                    "",
                    createAt,
                    false,
                    null,
                    null,
                    Movie.Status.NowPlaying
            )
        }

fun MovieResponse.toComingSoonMovies(): List<Movie> =
        results.map { result ->
            val playingDate = result.playingDate?.let { LocalDate.parse(it) }
            val createAt = result.createdAt.let { LocalDateTime.parse(it) }
            Movie(
                    result.id,
                    result.title,
                    result.overview,
                    result.imageUrl,
                    playingDate,
                    "",
                    "",
                    "",
                    createAt,
                    false,
                    null,
                    null,
                    Movie.Status.ComingSoon
            )
        }