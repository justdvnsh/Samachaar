package divyansh.tech.kotnewreader.network.models

import java.io.Serializable

data class User(
    var uid: String,
    var name: String,
    @SuppressWarnings
    var email: String,
    var isAuthenticated: Boolean? = null,
    var isNew: Boolean? = null,
    var isCreated: Boolean? = null
) : Serializable