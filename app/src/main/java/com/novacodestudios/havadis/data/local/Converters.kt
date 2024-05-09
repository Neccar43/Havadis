package com.novacodestudios.havadis.data.local

import androidx.room.TypeConverter
import com.novacodestudios.havadis.data.remote.dto.Source

// Room sadece ilkel veri türlerini kaydedebilir bu yüzden dönüştürücüleri kullandık
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