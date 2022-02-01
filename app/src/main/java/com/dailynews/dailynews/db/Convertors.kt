package com.dailynews.dailynews.db

import androidx.room.TypeConverter
import com.dailynews.dailynews.modals.Source

class Convertors {

    @TypeConverter
    fun fromSource(source : Source) : String{
        return source.name
    }

    @TypeConverter
    fun toSource(name : String) : Source {
        return Source(name, name)
    }

}