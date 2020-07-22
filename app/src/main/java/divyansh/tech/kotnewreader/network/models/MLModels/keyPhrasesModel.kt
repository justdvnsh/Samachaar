package divyansh.tech.kotnewreader.network.models.MLModels

data class KeyPhrases(
    val keyPhrases: MutableList<String>
)

data class keyPhrasesModel(
    val documents: MutableList<KeyPhrases>
)