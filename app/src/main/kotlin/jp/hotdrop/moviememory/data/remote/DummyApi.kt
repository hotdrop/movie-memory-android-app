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
        data.add(createMovie(id = 101, title = "グランディア",
                overview = "セガサターンで一番面白かったRPGです。本当はスパロボF完結編のために買ったのですがグランディアがプレイできたことが一番買って良かったことでした。" +
                        "世界の壁はとてもワクワクしました。ジャスティンとフィーナにずっとニヤニヤしていた当時でした。" +
                        "Switchでリメイクされるそうなので楽しみですが続報が全くない。",
                playDate = now.plusDays(2L).toString()))
        data.add(createMovie(id = 102, title = "ワイルドアームズ",
                overview = "セカンドイグニッションはラストバトルが最高潮ですがトータルでみると1の方が好きかなと思います。当時はPSで数少ないRPGだったのでグッズ使用やキャラの動きなど面白いものでした。" +
                        "そしてゲームで初めてデレメタリカで完全に詰まって半年くらい放置した思い出のゲームでもあります。当時はまだ攻略本もそこまでなかったのか・・" +
                        "隠しボスも申し分なく、ゴーレム、アンゴルモア、ブーメランフラッシュは素晴らしかったです。しかし、ラギュオラギュラは初見殺しで炎対策すればすんなり勝てるので拍子抜けでしたね。" +
                        "2は鬼のような強さで専用BGMも用意されていて素晴らしかったですが。" +
                        "なるけみちこさんの曲はどれもカッコいいのですが、WA1の曲は思い出補正と重なってかなり好きです。リメイク版はちょっと微妙でした・・",
                playDate = now.plusDays(3L).toString()))
        data.add(createMovie(id = 103, title = "LUNAR2 エターナルブルー", category = Category(2, "SF・ファンタジー"),
                overview = "初期プレイはPSでしたがRPGではトップレベルで面白かったしボリュームも満足いくものでした。シルバースターストーリーもやりましたがこっちの方が好きです。" +
                        "そしてボス戦のBGMが素晴らしくて盛り上がります。決戦の足音も良いですけどね。" +
                        "また、シルバースターストーリーと繋がっているので各々のキャラが出てくるとニヤケます。当時はアニメーションがゲーム中に入るのが珍しくてそれも面白さの一つでした。",
                playDate = now.plusDays(1L).toString()))
        data.add(createMovie(id = 104, title = "ポケットモンスター ダイヤモンド・パール",
                overview = "赤・緑からX・Yまでプレイしましたが、DPが一番バランスよくて面白いと感じました。ジムリーダーがかなり強いのに驚きました。" +
                        "エメラルドのバトルフロンティアやBWのPWTなども捨てがたいですがトータル的にDPが良かったです。映画もオラシオン良かった。" +
                        "ジムリーダー戦はDPが一番好きですね。チャンピオンはBW2のアイリスが勝ってしまいましたがそれまではシロナ戦が一級でした。" +
                        "ムカつく奴は連勝崩しを除けばドータクソ。おいドータクソ、浮遊のふりしてたいねつやめろ", playDate = now.plusDays(5L).toString()))
        data.add(createMovie(id = 105, title = "ヴァルキリープロファイル",
                overview = "SO2も良かったのですが戦闘の面白さや難易度的にもVPの方が面白かったかなと思います。特にAルートは良かった。" +
                        "奥義が決まると気持ちよくてBGMも素晴らしいのでボス戦や固有BGM戦はとても盛り上がります。大魔法が詠唱できるようになるとカッコよくて撃ちまくったり。" +
                        "ボイスコレクションはSO2ほど凶悪ではないですがそれなりにコンプリートが難しく、特にセラフィックゲートで聴ける奴とかバルバロッサくらいならいいのですが" +
                        "ブラッドヴェインとか集めてる余裕ないです。あの竜が一番苦戦しました。そのあとのフェンリルやロキはあれに比べればさほど強くなかったです。", playDate = now.plusDays(6L).toString()))
        data.add(createMovie(id = 106, title = "ドラえもん ギガゾンビの逆襲",
                overview = "ファミコンのRPGではドラクエ3、FF2、強襲サイヤ人に並んで光る面白さがあると思います。当時ドラえもんでRPGが出てくれたのは嬉しかったです。" +
                        "エンカウント率がめちゃ高く、海底なんか次の街にたどり着ける気がしませんでした。BGMがとても良かったです。", playDate = now.plusDays(10L).toString()))
        data.add(createMovie(id = 107, title = "オウバードフォース",
                overview = "途中セーブ不可のくせに後半は1マップ2時間はかかり、さらに複数マップで構成されているステージなどがあって" +
                        "スーパー難易度が高いゲームでした。攻略本を2冊買ってやっとクリアした記憶があります。" +
                        "BGMはとても素晴らしく最終ステージのボルザック戦のBGMは素晴らしいの一言です。あとドルガシじゃなくてドルガンです。", playDate = now.plusDays(2L).toString()))
        data.add(createMovie(id = 108, title = "星のカービィ・スーパーデラックス",
                overview = "最初のどかっ！0% 0% 0% のせいで洞窟の宝、隠しステージなど全無視してメタナイトもボコって銀河に願いをかなえに行きました。全てはマルク戦の曲を聴くためです。" +
                        "任天堂のBGMは良曲が多いのですがそれらの中でもマルク戦のBGMが群を抜いているのではないでしょうか。ヘルパーシステムも面白くペナルティがほぼないため（1Pのコピーを奪う程度はありますが）" +
                        "楽しく2P共戦ができたのも面白かったです。ミニゲームも面白いのが多かった良作でした。", playDate = now.plusDays(5L).toString()))
        data.add(createMovie(id = 109, title = "ゼノギアス",
                overview = "Disc2のせいで評価が不毛ですが戦闘システムやBGM、ストーリーは素晴らしいもので特にストーリーでこれを超えるものはまだ出会えていません。" +
                        "OP含め全容を理解するのは難しく、本編ではゼポイムの文明後やマハノンなどで断片的な情報でしか入手できないのがまた面白さを引き立ててくれました。" +
                        "エルドリッジを乗っ取ったゾハルが目指した本星、ゼノサーガではロストエルサレムになると思いますが、そこに何があるのか、アーネンエルベを迎えてどうなったのかなど気になる点が多くあります。" +
                        "残念ながら正式な続編が出ることはありませんでしたが良い作品でした。", playDate = now.plusDays(10L).toString()))
        data.add(createMovie(id = 110, title = "キングダムハーツ・ファイナルミックス",
                overview = "2以降、ハードがばらけてから追うのが面倒になって放置していますが、1は素晴らしいと思います。比較的ディズニーが好きなので抵抗もなくプレイできたのですが" +
                        "表向きはディズニーたちの世界を救いつつ裏では主人公たちの物語が少しずつ進んでいき、最後マレフィセントを倒したと思ったら賢者ゼアノートがラスボスだったという一連の流れが好きです。" +
                        "ゼノギアスもそうなのですが、表向きに進んでいる話とは別に散りばめられた伏線を読み解いていくことで、実は裏に壮大なストーリーがあることが発覚する、という話がとても好きです。" +
                        "このファイナルミックスも同様でアンセムレポートを読み進めていき、最後シークレットムービーを観た時はとても感動しました。セフィロスがゲスト登場したのも良かったですw", playDate = now.plusDays(1L).toString()))
        (111..200).forEach {
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