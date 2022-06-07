package com.example.tuniverse.dto

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

open class LoginDto {
    @NotNull
    @NotEmpty
    @Pattern(regexp="^[A-Za-z]*$*[0-9]*",message = "Invalid Input")
    open lateinit var username: String

    @NotNull
    @NotEmpty
    @Pattern(regexp="[A-Za-z]*\$*[0-9]*",message = "Invalid Input")
    open lateinit var password: String
}