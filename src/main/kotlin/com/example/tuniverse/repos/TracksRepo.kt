package com.example.tuniverse.repos

import com.example.tuniverse.entities.Track
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface TracksRepo: CrudRepository<Track, Long> {
    fun findAllByUser_Username(username: String): MutableIterable<Track>
    fun findTrackByTrackId(trackId: Int): Track
    fun findFirstByOrderByTrackIdDesc(): Track
}