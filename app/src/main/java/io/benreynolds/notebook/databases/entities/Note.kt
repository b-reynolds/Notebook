package io.benreynolds.notebook.databases.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

/**
 * A simple class that represents a note that has a [title] and a [body].
 *
 * @param title The title of the [Note].
 * @param body The body of the [Note].
 */
@Entity
class Note(
    @PrimaryKey(autoGenerate = true) var uid: Long? = null,
    title: String = "",
    body: String = ""
) {
    @ColumnInfo(name = "title")
    var title: String = title
        set(value) {
            if (field != value) {
                field = value
                lastModified = Date()
            }
        }

    @ColumnInfo(name = "body")
    var body: String = body
        set(value) {
            if (field != value) {
                field = value
                lastModified = Date()
            }
        }

    @ColumnInfo(name = "date_created")
    var dateCreated = Date()

    @ColumnInfo(name = "last_modified")
    var lastModified = Date(dateCreated.time)

    override fun equals(other: Any?): Boolean {
        if (other is Note) {
            return other.uid == uid && other.title == title && other.body == body
        }

        return super.equals(other)
    }

    override fun hashCode(): Int {
        var result = uid?.hashCode() ?: 0
        result = 31 * result + title.hashCode()
        result = 31 * result + body.hashCode()
        result = 31 * result + lastModified.hashCode()
        result = 31 * result + dateCreated.hashCode()
        return result
    }
}
