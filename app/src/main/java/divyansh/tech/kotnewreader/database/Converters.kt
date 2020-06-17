package divyansh.tech.kotnewreader.database

import androidx.room.TypeConverter
import divyansh.tech.kotnewreader.network.models.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}