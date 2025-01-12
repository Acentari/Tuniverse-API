package com.example.tuniverse.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

open class TrackListDto {
    @NotNull
    @NotEmpty
    open var trackList: List<TrackDto> = listOf()

}