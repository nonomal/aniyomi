package eu.kanade.tachiyomi.util.storage

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

val Context.cacheImageDir: File
    get() = File(cacheDir, "shared_image")

/**
 * Returns the uri of a file
 *
 * @param context context of application
 */
fun File.getUriCompat(context: Context): Uri {
    return FileProvider.getUriForFile(context, context.packageName + ".provider", this)
}

/**
 * Copies this file to the given [target] file while marking the file as read-only.
 *
 * @see File.copyTo
 */
fun File.copyAndSetReadOnlyTo(target: File, overwrite: Boolean = false, bufferSize: Int = DEFAULT_BUFFER_SIZE): File {
    if (!this.exists()) {
        throw NoSuchFileException(file = this, reason = "The source file doesn't exist.")
    }

    if (target.exists()) {
        if (!overwrite) {
            throw FileAlreadyExistsException(
                file = this,
                other = target,
                reason = "The destination file already exists.",
            )
        } else if (!target.delete()) {
            throw FileAlreadyExistsException(
                file = this,
                other = target,
                reason = "Tried to overwrite the destination, but failed to delete it.",
            )
        }
    }

    if (this.isDirectory) {
        if (!target.mkdirs()) {
            throw FileSystemException(file = this, other = target, reason = "Failed to create target directory.")
        }
    } else {
        target.parentFile?.mkdirs()

        this.inputStream().use { input ->
            target.outputStream().use { output ->
                // Set read-only
                target.setReadOnly()

                input.copyTo(output, bufferSize)
            }
        }
    }

    return target
}
