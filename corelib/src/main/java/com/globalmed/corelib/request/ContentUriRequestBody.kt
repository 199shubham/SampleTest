package com.globalmed.corelib.request

import android.content.ContentResolver
import android.net.Uri
import com.globalmed.corelib.base.CoreExtension.length
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.IOException

class ContentUriRequestBody(
    private val contentResolver: ContentResolver,
    private val contentUri: Uri
) : RequestBody() {

    override fun contentType(): MediaType? {
        val contentType = contentResolver.getType(contentUri)
        return MediaType.parse(contentType)
    }

    override fun contentLength(): Long {
        val size = contentUri.length(contentResolver)
        return size
    }

    override fun writeTo(bufferedSink: BufferedSink) {
        val inputStream = contentResolver.openInputStream(contentUri)
            ?: throw IOException("Couldn't open content URI for reading")
        bufferedSink.write(inputStream.readBytes())

    }
}