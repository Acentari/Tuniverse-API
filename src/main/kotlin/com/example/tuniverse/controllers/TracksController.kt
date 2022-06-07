package com.example.tuniverse.controllers

import com.example.tuniverse.services.TracksService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.lang.StringBuilder
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
        return tracksService.listByUsername(username)
    }

    @PostMapping()
    fun upload(
        @RequestAttribute
        username: String,
        @RequestParam("file")
        multipartTune: MultipartFile
    ) {
        tracksService.upload(multipartTune, username)
    }

    @GetMapping("/stream")
    fun stream(
        @RequestParam
        trackName: String,
        response: HttpServletResponse
    ) {
        return tracksService.stream(trackName, response)
    }

    @PostMapping("link")
    fun uploadByLink(
        @RequestParam
        link: String,
        @RequestAttribute
        username: String
    ) {
        try {
            println(link)
            val sb = StringBuilder(link)
            val reverseLink = sb.reverse()
            val formattedLink = reverseLink.substring(14).reversed()
            println(formattedLink)

            val p = Runtime.getRuntime().exec("./youtube-dl.exe --extract-audio -o ~/Desktop/$username/%(title)s.mp3 $formattedLink")
            p.waitFor()

            val ins = p.inputStream
            val reader = InputStreamReader(ins)
            val lines = reader.readLines().toString()
            println(lines)
            val index: Int = lines.indexOf(username) + username.length + 1
            val mp3Index = lines.indexOf(".mp3")
            val trackName = lines.substring(index,mp3Index) + ".mp3"
            println(trackName)

            tracksService.saveByTrackNameAndUsername(trackName,username)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}