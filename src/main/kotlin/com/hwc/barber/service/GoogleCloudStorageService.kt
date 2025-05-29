package com.hwc.barber.service

import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class GoogleCloudStorageService(
    private val storage: Storage
) {
    @Value("\${spring.cloud.gcp.storage.bucket}")
    private lateinit var bucketName: String

    fun uploadImage(file: MultipartFile, folder: String): String {
        val fileName = "${folder}/${UUID.randomUUID()}-${file.originalFilename}"
        val blobId = BlobId.of(bucketName, fileName)
        val blobInfo = BlobInfo.newBuilder(blobId)
            .setContentType(file.contentType)
            .build()

        storage.create(blobInfo, file.bytes)

        return "https://storage.googleapis.com/$bucketName/$fileName"
    }

    fun deleteImage(imageUrl: String) {
        val fileName = imageUrl.substringAfter("$bucketName/")
        val blobId = BlobId.of(bucketName, fileName)
        storage.delete(blobId)
    }
} 