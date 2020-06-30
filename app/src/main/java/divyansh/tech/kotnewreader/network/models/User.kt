package divyansh.tech.kotnewreader.network.models

import java.io.Serializable

data class User(
    var uid: String? = null,
    var name: String? = null,
    @SuppressWarnings
    var email: String? = null,
    var isAuthenticated: Boolean? = null,
    var isNew: Boolean? = null,
    var isCreated: Boolean? = null,
    var articles: List<Article>? = null,
    var synced: Boolean? = null
) : Serializable