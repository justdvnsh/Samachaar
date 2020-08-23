package divyansh.tech.kotnewreader.models.MLModels

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