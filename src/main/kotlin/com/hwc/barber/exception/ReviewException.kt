package com.hwc.barber.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class ReviewNotFoundException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidReviewRatingException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.FORBIDDEN)
class ReviewAccessDeniedException(message: String) : RuntimeException(message)

@ResponseStatus(HttpStatus.CONFLICT)
class DuplicateReviewException(message: String) : RuntimeException(message) 