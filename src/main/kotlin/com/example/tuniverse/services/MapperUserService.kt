package com.example.tuniverse.services

import com.example.tuniverse.dto.TrackDto
import com.example.tuniverse.dto.TrackListDto
import com.example.tuniverse.dto.UserDto
import com.example.tuniverse.entities.Track
import com.example.tuniverse.entities.User
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class MapperUserService{

    fun userDtoToUser(userDto: UserDto): User {
        val user = User()
        val bCryptPasswordEncoder = BCryptPasswordEncoder()
        user.username = userDto.username
        user.password = bCryptPasswordEncoder.encode(userDto.password)
        user.fname = userDto.fname
        user.lname = userDto.lname
        user.email = userDto.email
        return user
    }

    @Value("\${app.path}")
    private val appPath: String? = null

    fun trackToTrackDto(list: MutableIterable<Track>): TrackListDto {
        val trackList = mutableListOf<TrackDto>()
        val trackListDto = TrackListDto()

        for (track: Track in list) {
            val trackDto = TrackDto()
            trackDto.trackName = track.trackName
            trackDto.username = track.user.username
            trackDto.id = track.trackId.toString()
            trackDto.duration = track.duration!!

            try {

//                val audioFile = AudioFileIO.read(File("$appPath/${track.user.username}/${track.trackId}.mp3"))
//                val tag = audioFile.tag
//                val artwork: Artwork? = tag.firstArtwork
//                if (artwork != null) {
//                    val imageBytes = artwork.binaryData
//                    trackDto.albumCover = imageBytes
//                }


            } catch (e: Exception) {
                e.printStackTrace()
            }

            trackList.add(trackDto)
        }
        trackListDto.trackList = trackList
        return trackListDto
    }
}