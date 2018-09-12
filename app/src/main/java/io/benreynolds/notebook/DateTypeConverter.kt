package io.benreynolds.notebook

import androidx.room.TypeConverter
import java.util.Date

/**
 * [TypeConverter] that handles the conversion of [Date]s to and from [Long]s.
 */
object DateTypeConverter {
    @JvmStatic
    @TypeConverter
    fun toDate(value: Long) = Date(value)
    @JvmStatic
    @TypeConverter
    fun toLong(value: Date) = value.time
}
