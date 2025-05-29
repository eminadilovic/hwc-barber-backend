package com.hwc.barber.controller

import com.hwc.barber.service.GoogleCloudStorageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/images")
@Tag(name = "Image Management", description = "Endpoints for managing images")
class ImageController(
    private val googleCloudStorageService: GoogleCloudStorageService
) {
    @PostMapping("/upload/{type}", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(
        summary = "Upload an image",
        description = "Upload an image to Google Cloud Storage",
        responses = [
            ApiResponse(responseCode = "200", description = "Image uploaded successfully"),
            ApiResponse(responseCode = "400", description = "Invalid image file"),
            ApiResponse(responseCode = "500", description = "Error uploading image")
        ]
    )
    fun uploadImage(
        @Parameter(description = "Type of image (e.g., profile, shop, service)")
        @PathVariable type: String,
        @Parameter(
            description = "Image file to upload",
            content = [Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE, schema = Schema(type = "string", format = "binary"))]
        )
        @RequestParam("file") file: MultipartFile
    ): ResponseEntity<String> {
        val imageUrl = googleCloudStorageService.uploadImage(file, type)
        return ResponseEntity.ok(imageUrl)
    }

    @DeleteMapping("/delete")
    @Operation(
        summary = "Delete an image",
        description = "Delete an image from Google Cloud Storage",
        responses = [
            ApiResponse(responseCode = "200", description = "Image deleted successfully"),
            ApiResponse(responseCode = "404", description = "Image not found"),
            ApiResponse(responseCode = "500", description = "Error deleting image")
        ]
    )
    fun deleteImage(@RequestParam("url") imageUrl: String): ResponseEntity<Unit> {
        googleCloudStorageService.deleteImage(imageUrl)
        return ResponseEntity.ok().build()
    }
} 