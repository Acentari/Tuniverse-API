package com.example.tuniverse

import com.example.tuniverse.exceptions.DateException
import com.example.tuniverse.exceptions.Mp3DataException
import com.example.tuniverse.exceptions.ValidationErrorException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler(ValidationErrorException::class)
    fun handleValidationError(ex: ValidationErrorException): ResponseEntity<ErrorResponse> {
        val error: String = ex.error
        val response = ErrorResponse("Validation Error", error)
        return ResponseEntity.badRequest().body(response)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleArgumentExceptions(ex :MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val error = "invalid input"
        val response = ErrorResponse("MethodArgumentNotValidException", error)
        return ResponseEntity.badRequest().body(response)
    }

    @ExceptionHandler(DateException::class)
    fun handleArgumentExceptions(ex :DateException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse("DateException", ex.error)
        return ResponseEntity.badRequest().body(response)
    }
    @ExceptionHandler(Mp3DataException::class)
    fun handleMp3DataException(ex :Mp3DataException): ResponseEntity<ErrorResponse> {
        val response = ErrorResponse("Mp3DataException", ex.error)
        return ResponseEntity.badRequest().body(response)
    }

}

class ErrorResponse(val error: String, val message: String)