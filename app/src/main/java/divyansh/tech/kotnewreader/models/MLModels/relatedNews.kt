package divyansh.tech.kotnewreader.models.MLModels

import java.io.Serializable

data class Source(
    val title: String? = null
)

data class SubArticle(
    val url: String? = null,
    val title: String? = null,
    val publisher: String? = null
)

data class RelatedNews(
    val title: String? = null,
    val link: String? = null,
    val published: String? = null,
    val source: Source,
    val sub_articles: MutableList<SubArticle>
) : Serializable

data class relatedNews(
    val entries: MutableList<RelatedNews>
)