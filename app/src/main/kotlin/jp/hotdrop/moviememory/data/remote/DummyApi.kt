package jp.hotdrop.moviememory.data.remote

import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.remote.response.MovieResult
import jp.hotdrop.moviememory.model.Category
import org.threeten.bp.LocalDate

/**
 * TODO これちゃんとAPIできたら削除する
 */
class DummyApi {

    private val dummyData: List<MovieResult>

    init {
        val data = mutableListOf<MovieResult>()
        val now = LocalDate.now()

        // now playing data
        data.add(createMovie(id = 1, title = "ファーストコンタクト", overview = "映画その1の情報です。1日前に公開", playDate = now.minusDays(1L).toString()))
        data.add(createMovie(id = 2, title = "セカンドダガシ", overview = "駄菓子のダミー映画です。2日前に公開", playDate = now.minusDays(2L).toString()))
        data.add(createMovie(id = 3, title = "アベンジャーズ・インフィニティーウォー",
                category = Category(2, "SF・ファンタジー"),
                overview = "6つ全てを手に入れると、全宇宙の生命の半分を滅ぼす力を得る「インフィニティーストーン」その無限のエネルギーを持つ石を手にせんと迫り来るサノス。\n" +
                        "彼の野望を阻止するため、最強ヒーロチーム・アベンジャーズが結集し、人類の命運をかけた壮絶なバトルが今、始まる！本日公開！",
                playDate = now.toString(),
                imageUrl = "https://firebasestorage.googleapis.com/v0/b/movie-memory.appspot.com/o/infini.jpg?alt=media&token=484ef333-a0bb-498b-b9cd-25a4205b00d3",
                officialUrl = "https://www.google.co.jp",
                trailerMovieUrl = "https://www.youtube.com/watch?v=6ZfuNTqbHE8"))
        data.add(createMovie(id = 4, title = "シャークフォース", overview = "ダミーサメが出てくる映画です。5日前に公開", playDate = now.minusDays(5L).toString()))
        data.add(createMovie(id = 5, title = "ファイブセンス", overview = "碁盤目なだけに光ると五番映画ができます。際がそろそろ消える気がします。6日前に公開", playDate = now.minusDays(6L).toString()))
        data.add(createMovie(id = 6, title = "シックスエレメンツ", overview = "色々混じったダミー映画であります。10日前に公開", playDate = now.minusDays(10L).toString()))
        data.add(createMovie(id = 7, title = "過去のセブン", overview = "正当姿勢やの宇宙が目覚めルカも知れません。1ヶ月に公開", playDate = now.minusMonths(1L).toString()))
        data.add(createMovie(id = 8, title = "過去エイトヒーローズ", overview = "実写版のヒーロアカデミアがどうなるのか木になるところです。2ヶ月前に公開", playDate = now.minusMonths(2L).toString()))
        data.add(createMovie(id = 9, title = "過去ナイン3K物語", overview = "ブラック企業の3Kを踏襲した名作ダミー映画です。2ヶ月前に公開", playDate = now.minusMonths(2L).toString()))
        data.add(createMovie(id = 10, title = "過去ビガヂュヴと元気な冒険", overview = "ダークビガーが世界を1兆ボルトで消滅させるとぅるるる物語です。とぅるるるるはいつででくるのでしょうかどっぴ2ヶ月前に公開", playDate = now.minusYears(2L).toString()))
        (11..100).forEach {
            data.add(createMovie(id = it, title = "映画その$it", playDate = now.minusDays(20L).toString()))
        }

        // coming soon data
        data.add(createMovie(id = 1, title = "グランディア",
                overview = "セガサターンで一番面白かったRPGです。本当はスパロボF完結編のために買ったのですがグランディアがプレイできたことが一番買って良かったことでした。世界の壁はとてもワクワクしました。",
                playDate = now.plusDays(2L).toString()))
        data.add(createMovie(id = 2, title = "ワイルドアームズ",
                overview = "2はラストバトルが最高潮ですが、トータルでみると1の方が好きです。ブーメランがかっこいい。",
                playDate = now.plusDays(3L).toString()))
        data.add(createMovie(id = 3, title = "LUNAR2 エターナルブルー", category = Category(2, "SF・ファンタジー"),
                overview = "初期プレイはPSでしたがRPGではトップレベルでした。シルバースターストーリーもやりましたがこっちの方が好きかなと。そしてボス戦のBGMが素晴らしい。",
                playDate = now.plusDays(1L).toString()))
        data.add(createMovie(id = 4, title = "ポケットモンスター ダイヤモンド・パール", overview = "", playDate = now.plusDays(5L).toString()))
        data.add(createMovie(id = 5, title = "ヴァルキリープロファイル", overview = "", playDate = now.plusDays(6L).toString()))
        data.add(createMovie(id = 6, title = "ドラえもん ギガゾンビの逆襲", overview = "", playDate = now.plusDays(10L).toString()))
        data.add(createMovie(id = 7, title = "オウバードフォース", overview = "", playDate = now.plusDays(2L).toString()))
        data.add(createMovie(id = 8, title = "ロマンシングサガ2", overview = "", playDate = now.plusDays(5L).toString()))
        data.add(createMovie(id = 9, title = "ゼノギアス", overview = "", playDate = now.plusDays(10L).toString()))
        data.add(createMovie(id = 10, title = "クロノトリガー", overview = "", playDate = now.plusDays(1L).toString()))
        (11..100).forEach {
            data.add(createMovie(id = it, title = "ゲームその$it", playDate = now.plusDays(20L).toString()))
        }

        // past data ゲームと映画と・・最後は何だろうか

        dummyData = data
    }

    fun getMovies(fromId: Int?): Single<List<MovieResult>> =
            fromId?.let {
                Single.just(dummyData.subList(fromId - 1, dummyData.size - 1))
            } ?: Single.just(dummyData)

    private fun createMovie(
            id: Int,
            title: String? = null,
            category: Category = Category(1, "アクション"),
            overview: String? = null,
            imageUrl: String? = null,
            playDate: String? = null,
            filmDirector: String? = null,
            officialUrl: String? = null,
            trailerMovieUrl: String? = null): MovieResult =
            MovieResult(
                    id = id,
                    title = title ?: "ダミータイトルです。",
                    categoryId = category.id,
                    categoryName = category.name,
                    overview = overview ?: "ここには概要を入れます。ある程度長い概要でも表示されるようにちょっと長いものを入れます。\nアベンジャーズ4が楽しみですがその前にキャプテンマーベルがどうなるのか楽しみです。\nそれとエージェントオブヒドラ（エージェントオブシールドのシーズン4の後半）はちょっとないなと思いました。。",
                    imageUrl = imageUrl,
                    playingDate = playDate ?: "2018-11-01",
                    filmDirector = filmDirector ?: "ダミーディレクター",
                    officialUrl = officialUrl,
                    trailerMovieUrl = trailerMovieUrl
            )
}