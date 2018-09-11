package io.benreynolds.notebook

import android.arch.persistence.room.TypeConverter
import java.util.Date

/**
 * [TypeConverter] that handles the conversion of [Date]s to and from [Long]s.
 */
object DateTypeConverter {
    @TypeConverter
    fun toDate(value: Long) = Date(value)
    @TypeConverter
    fun toLong(value: Date) = value.time
}
