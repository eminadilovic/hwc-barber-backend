package com.hwc.barber.config

import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GoogleCloudConfig {
    @Bean
    fun storageClient(): Storage {
        return StorageOptions.getDefaultInstance().service
    }
} 