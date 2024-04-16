@file:Suppress("ConstPropertyName")

package com.crow.mangax.copymanga

object BaseStrings {

    const val PATH_WORD = "pathword"
    const val NAME = "name"
    const val IMAGE_URL = "image_url"
    const val ID = "id"
    const val ENABLE_DELAY = "enable_delay"
    const val BACKPRESS = "onBackPress"
    const val EVENT = "EVENT"
    const val OPTION = "OPTION"
    const val VALUE = "VALUE"

    object URL {

        fun setCopyMangaUrl(tld: String) { COPYMANGA = "https://api.copymanga$tld" }

        const val SCHEME_HTTPS = "https"
        const val WUYA_API_ROUTE = "https://api.badwuya.top"
        const val WUYA_API_IMAGE = "image.badwuya.top"

        // (Top-Level Domain，TLD)
        const val CopyManga_TLD_TV = ".tv"
        const val CopyManga_TLD_SITE = ".site"

        var COPYMANGA = "https://api.copymanga$CopyManga_TLD_TV"
        const val MangaFuna = "https://hi77-overseas.mangafuna.xyz/"
        const val Login = "/api/v3/login"
        const val Reg = "/api/v3/register"

        const val ComicInfo = "/api/v3/comic2/{pathword}?platform=3&_update=true"
        const val ComicChapter = "/api/v3/comic/{pathword}/group/default/chapters?_update=true" 
        const val ComicPage = "/api/v3/comic/{pathword}/chapter2/{uuid}?platform=3&_update=true"
        const val ComicBrowserHistory = "/api/v3/comic2/{$PATH_WORD}/query?platform=3&_update=true"
        const val ComicHistory = "/api/v3/member/browse/comics?platform=3&_update=true"
        const val ComicAddToBookshelf = "/api/v3/member/collect/comic"
        const val ComicSearch = "/api/v3/search/comic"
        const val ComicTopic = "/api/v3/topic/{$PATH_WORD}/contents"
        const val ComicCommentList = "/api/v3/roasts?_update=true"
        const val ComicCommentSubmit = "/api/v3/member/roast"

        const val NovelInfo = "/api/v3/book/{pathword}?_update=true"
        const val NovelChapter = "/api/v3/book/{pathword}/volumes"
        const val NovelBrowserHistory = "/api/v3/book/{pathword}/query"
        const val NovelHistory = "/api/v3/member/browse/books?platform=3&_update=true"
        const val NovelCatelogue = "/api/v3/book/{pathword}/volume/1847?_update=true"
        const val NovelAddToBookshelf = "/api/v3/member/collect/book"
        const val NovelSearch = "/api/v3/search/books"

        const val HomePage = "/api/v3/h5/homeIndex"
        const val RefreshRec = "/api/v3/recs"
        const val UserUpdateInfo = "/api/v3/member/update/info"
        const val UserInfo = "/api/v3/member/info"
        const val BookshelfComic = "/api/v3/member/collect/comics?free_type=1&_update=true"
        const val BookshelfNovel = "/api/v3/member/collect/books?free_type=1&_update=true"

        const val DiscoverComicTag = "/api/v3/h5/filter/comic/tags"
        const val DiscoverComicHome = "/api/v3/comics?free_type=1&_update=true"
        const val DiscoverNovelTag = "/api/v3/h5/filter/book/tags"
        const val DiscoverNovelHome = "/api/v3/books?free_type=1&_update=true"


        fun setHotMangaUrl(url: String) { HotManga = "https://$url" }

        var HotManga = "https://mapi.hotmangasd.com"

        const val HotManga_Reg = "/api/v3/register"
        const val HotManga_Login = "/api/v3/login"
        const val HotManga_AnimePage = "/api/v3/cartoons?free_type=1"
        const val HotManga_AnimeInfoPage = "/api/v3/cartoon2/{$PATH_WORD}"
        const val HotManga_AnimeChapterList = "/api/v3/cartoon/{$PATH_WORD}/chapters2"
        const val HotManga_AnimeVideo = "/api/v3/cartoon/{$PATH_WORD}/chapter/{chapter_uuid}?platform=1"
        const val HotManga_Site = "/api/v3/system/network2?platform=1"
        const val HotManga_Search = "api/v3/search/cartoon?platform=1&free_type=1&_update=true"
    }

    object Repository {
        const val IMAGE_ERROR = "https://gitlab.com/crowforkotlin/copymangax/-/raw/main/Image/comic_error.png"
        const val SITE = "https://gitlab.com/crowforkotlin/copymangax/-/raw/main/site.json"
        const val UpdateInfo = "https://127.0.0.1" //**"https://gitlab.com/crowforkotlin/copymangax/-/raw/main/updateInfo.json"
        const val GROUP = "https://gitlab.com/crowforkotlin/copymangax/-/raw/main/group.json"
        const val UpdateHistory = "https://gitlab.com/crowforkotlin/copymangax/-/raw/main/updateHistory.json"
        const val NOTICE = "https://127.0.0.1" //**"https://gitlab.com/crowforkotlin/copymangax/-/raw/main/notice.json?ref_type=heads"
    }
}
