package io.benreynolds.notebook

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
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
    title: String,
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

    @ColumnInfo(name = "last_modified")
    var lastModified = Date()
}
