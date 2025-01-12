package com.example.tuniverse.dto

class TrackDto {
    lateinit var trackName: String
    lateinit var username: String
    var duration: Long = 0
    lateinit var id: String
    var albumCover: ByteArray? = null
}