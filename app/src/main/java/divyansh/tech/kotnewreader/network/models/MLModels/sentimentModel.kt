package divyansh.tech.kotnewreader.network.models.MLModels

data class sentimentModel(
    val pos: Int,
    val neg: Int,
    val mid: Int,
    val pos_percent: String,
    val neg_percent: String,
    val mid_percent: String
)