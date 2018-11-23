package jp.hotdrop.moviememory.data.remote

import io.reactivex.Flowable
import io.reactivex.Single
import jp.hotdrop.moviememory.data.remote.response.MovieResult
import jp.hotdrop.moviememory.model.Category

/**
 * TODO これちゃんとAPIできたら削除する
 */
class DummyApi {

    private val dummyData: List<MovieResult>

    init {
        val data = mutableListOf<MovieResult>()
        data.add(createMovie(id = 1, title = "ファーストコンタクト", overview = "映画その1の情報です", playDate = "2018-11-10"))
        data.add(createMovie(id = 2, title = "セカンドダガシ", overview = "駄菓子のダミー映画です。", playDate = "2018-11-04"))
        data.add(createMovie(id = 3, title = "アベンジャーズ・インフィニティーウォー",
                category = Category(2, "SF・ファンタジー"),
                overview = "6つ全てを手に入れると、全宇宙の生命の半分を滅ぼす力を得る「インフィニティーストーン」その無限のエネルギーを持つ石を手にせんと迫り来るサノス。\n" +
                        "彼の野望を阻止するため、最強ヒーロチーム・アベンジャーズが結集し、人類の命運をかけた壮絶なバトルが今、始まる！",
                playDate = "2018-11-23",
                imageUrl = ""))
        data.add(createMovie(id = 4, title = "シャークフォース", overview = "ダミーサメが出てくる映画です。", playDate = "2018-10-16"))
        data.add(createMovie(id = 5, title = "ファイブセンス", overview = "碁盤目なだけに光ると五番映画ができます。際がそろそろ消える気がします。", playDate = "2018-10-16"))
        data.add(createMovie(id = 6, title = "シックスエレメンツ", overview = "色々混じったダミー映画であります。", playDate = "2018-07-21"))
        data.add(createMovie(id = 7, title = "セブンセンシズ", overview = "正当姿勢やの宇宙が目覚めルカも知れません。", playDate = "2018-06-10"))
        data.add(createMovie(id = 8, title = "エイトヒーローズ", overview = "実写版のヒーロアカデミアがどうなるのか木になるところです。", playDate = "2017-09-21"))
        data.add(createMovie(id = 9, title = "ナインブラック3K物語", overview = "ブラック企業の3Kを踏襲した名作ダミー映画です", playDate = "2017-09-21"))
        data.add(createMovie(id = 10, title = "ビガヂュヴと元気な冒険", overview = "ダークビガーが世界を1兆ボルトで消滅させるとぅるるる物語です。とぅるるるるはいつででくるのでしょうかどっぴ", playDate = "2017-09-21"))
        (11..100).forEach {
            data.add(createMovie(id = it, title = "映画その$it"))
        }
        dummyData = data
    }

    fun nowPlaying(fromId: Int?): Single<List<MovieResult>> =
            fromId?.let {
                Single.just(dummyData.subList(fromId - 1, dummyData.size - 1))
            } ?: Single.just(dummyData)


    fun comingSoon(index: Int, offset: Int): Flowable<List<MovieResult>> =
        Flowable.just(dummyData.subList(index, index + offset))

    private fun createMovie(
            id: Int,
            title: String? = null,
            category: Category = Category(1, "アクション"),
            overview: String? = null,
            imageUrl: String? = null,
            playDate: String? = null,
            filmDirector: String? = null,
            url: String? = null): MovieResult =
            MovieResult(
                    id = id,
                    title = title ?: "ダミータイトルです。",
                    categoryId = category.id,
                    categoryName = category.name,
                    overview = overview ?: "ここには概要を入れます。ある程度長い概要でも表示されるようにちょっと長いものを入れます。\nアベンジャーズ4が楽しみですがその前にキャプテンマーベルがどうなるのか楽しみです。\nそれとエージェントオブヒドラ（エージェントオブシールドのシーズン4の後半）はちょっとないなと思いました。。",
                    imageUrl = imageUrl,
                    playingDate = playDate ?: "2018-11-01",
                    filmDirector = filmDirector ?: "ダミーディレクター",
                    url = url ?: "https://www.google.co.jp"
            )
}