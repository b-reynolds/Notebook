package io.benreynolds.notebook

import java.util.Date

/**
 * A simple class that represents a note that has a [title] and a [body].
 *
 * @param title The title of the [Note].
 * @param body The body of the [Note].
 */
class Note(title: String, body: String = "") {
    var title: String = title
        set(value) {
            if (field != value) {
                field = value
                lastModified = Date()
            }
        }

    var body: String = body
        set(value) {
            if (field != value) {
                field = value
                lastModified = Date()
            }
        }

    var lastModified = Date()
}
