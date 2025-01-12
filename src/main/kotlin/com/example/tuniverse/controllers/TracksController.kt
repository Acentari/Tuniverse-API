package com.example.tuniverse.controllers

import com.example.tuniverse.dto.TrackListDto
import com.example.tuniverse.services.TracksService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api/tracks")
class TracksController(val tracksService: TracksService) {
    @ResponseStatus(HttpStatus.OK)
    @GetMapping()
    fun list(
        @RequestAttribute
        username: String
    ): Any? {
        return tracksService.listRandom()
    }

    @GetMapping("/mytracks")
    fun listByUsername(
        @RequestAttribute
        username: String
    ): TrackListDto {
        return tracksService.listByUsername(username)
    }

    @PostMapping("/upload")
    fun handleFileUpload(
        @RequestAttribute
        username: String,
        @RequestPart("file") file: MultipartFile
    ): Any {

        tracksService.upload(file, username)

        return "File uploaded successfully"
    }

    @GetMapping("/stream")
    fun stream(
        @RequestParam
        trackId: String,
        response: HttpServletResponse
    ) {
        return tracksService.stream(trackId, response)
    }

}

