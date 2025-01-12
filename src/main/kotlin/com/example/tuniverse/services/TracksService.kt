package com.example.tuniverse.services

import com.example.tuniverse.dto.TrackListDto
import com.example.tuniverse.entities.Track
import com.example.tuniverse.exceptions.Mp3DataException
import com.example.tuniverse.repos.TracksRepo
import com.example.tuniverse.repos.UsersRepo
import com.mpatric.mp3agic.Mp3File
import org.jaudiotagger.audio.AudioFileIO
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.nio.file.Files
import javax.servlet.http.HttpServletResponse


@Service
class TracksService(
    val tracksRepo: TracksRepo,
    val mapper: MapperUserService,
    val usersRepo: UsersRepo,
) {
    @Value("\${app.path}")
    private val appPath: String? = null


    fun listByUsername(username: String): TrackListDto {
        val trackList = tracksRepo.findAllByUser_Username(username)
        return mapper.trackToTrackDto(trackList)
    }

    fun listRandom(): TrackListDto {
        var trackList = tracksRepo.findAll()
        var returnList = trackList

        if (trackList.count() > 6) {

            trackList = trackList.toMutableList().apply {
                shuffle()
            }

            returnList = trackList.take(6)
        }
        return mapper.trackToTrackDto(returnList)
    }

    fun stream(
        trackId: String,
        response: HttpServletResponse
    ) {

        val username: String?
        val track = tracksRepo.findTrackByTrackId(trackId.toInt())
        username = track.user.username

        println(username)

        val file = File("$appPath/$username/$trackId.mp3")

        try {
            Files.copy(file.toPath(), response.outputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun upload(multipartTune: MultipartFile, username: String) {
        val track = Track()
        val user = usersRepo.findByUsername(username)

        // Extract track name
        val trackName = multipartTune.originalFilename?.dropLast(4)
        if (trackName != null) {
            track.trackName = trackName
            track.user = user
        }

        // Save the track details to the database
        val savedTrack = tracksRepo.save(track)
        val path = "$appPath/$username/${savedTrack.trackId}.mp3"

        // Move the final MP3 to the permanent location
        multipartTune.transferTo(File(path))
        setAlbumCover(File(path))

        if (validateMp3(File(path), track)) {
            // Get MP3 duration and update the track
            val duration = getMp3Duration(path)
            savedTrack.duration = duration
            tracksRepo.save(savedTrack)
        } else {
            // Delete the temporary file after all operations
            File(path).delete()
            throw Mp3DataException("file is not mp3")
        }
    }

    fun saveByTrackNameAndUsername(trackName: String, username: String){
        var track = Track()
        track.trackName = username
        track.user.username = trackName
        track = tracksRepo.save(track)
    }

    fun getMp3Duration(filePath: String): Long {
        val mp3File = Mp3File(filePath)
        return if (mp3File.hasId3v2Tag()) {
            mp3File.lengthInSeconds * 1000 // Convert to milliseconds
        } else 0L
    }



    fun validateMp3(mp3File: File?, track: Track): Boolean {
        if (mp3File == null || !mp3File.exists()) {
            mp3File?.delete()
            return false
        }

        return try {
            // Read the MP3 file using JAudioTagger
            val audioFile = AudioFileIO.read(mp3File)

            // Check if the file has a valid bitrate
            val bitRate = audioFile.audioHeader.bitRate.toIntOrNull() // Safely convert to an integer
            if (bitRate == null || bitRate <= 0) {
                println("Invalid bitrate!")
                return false // Invalid file
            }


            // Check for valid audio length
            if (audioFile.audioHeader.trackLength <= 0) {

                return false // Invalid file
            }

            // Check for missing tags
            if (audioFile.tag == null) {

                return false // Invalid file
            }

            true // Valid MP3 file

        } catch (e: Exception) {
            mp3File.delete()
            tracksRepo.delete(track)
            throw Mp3DataException("Not a valid mp3 File")
        }
    }

    fun setAlbumCover(mp3File: File) {
        try {

            val audioFile = AudioFileIO.read(mp3File)
            val tag = audioFile.tag
            tag.deleteArtworkField()
            audioFile.commit()
        } catch (e: Exception) {
            println("Error removing album cover: ${e.message}")
            e.printStackTrace()
        }
    }

}