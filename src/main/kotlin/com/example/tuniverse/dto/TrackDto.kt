package com.example.tuniverse.dto

import com.example.tuniverse.entities.Track
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

open class TrackDto {
    @NotNull
    @NotEmpty
    open lateinit var trackList: MutableIterable<Track>

}