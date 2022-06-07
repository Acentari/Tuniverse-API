package com.example.tuniverse.services

import com.example.tuniverse.dto.TrackDto
import com.example.tuniverse.entities.Track
import com.example.tuniverse.repos.TracksRepo
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.nio.file.*
import javax.servlet.http.HttpServletResponse
import kotlin.io.path.Path

@Service
class TracksService(
    val tracksRepo: TracksRepo,
    val mapper: MapperUserService
) {
   fun listByUsername(username: String): TrackDto {
       val trackList =  tracksRepo.findAllByUsername(username)
       val trackDto = mapper.trackToTrackDto(trackList)
       return trackDto
   }

    fun stream(
        trackName: String,
        response: HttpServletResponse
    ) {
        val file = File("C:/wamp64/www/user12/"+trackName)
        try {
            Files.copy(file.toPath(), response.outputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun upload(multipartTune: MultipartFile, username: String) {
        val trackName = multipartTune.originalFilename
        val path = Path("/var/www/html/$username/$trackName")

        Files.copy(multipartTune.inputStream, path,
            StandardCopyOption.REPLACE_EXISTING)
        val track = Track()
        if (trackName != null) {
            track.trackname = trackName
            track.username = username
        }
        tracksRepo.save(track)
    }

    fun saveByTrackNameAndUsername(trackName: String, username: String){
         val track = Track()
        track.username = username
        track.trackname = trackName
        tracksRepo.save(track)
    }

    fun getFileExtension(fullName: String): String {
        val fileName = File(fullName).name
        val dotIndex = fileName.lastIndexOf('.')
        return if (dotIndex == -1) "" else fileName.substring(dotIndex + 1)
    }
}