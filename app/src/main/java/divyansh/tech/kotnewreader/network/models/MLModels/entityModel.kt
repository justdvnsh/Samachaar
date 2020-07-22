package divyansh.tech.kotnewreader.network.models.MLModels

data class Entities(
    val text: String,
    val type: String? = null
)

data class EntitiesModels(
    val entities: MutableList<Entities>
)

data class entityModel(
    val documents: MutableList<EntitiesModels>
)