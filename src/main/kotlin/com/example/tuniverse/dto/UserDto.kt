package com.example.tuniverse.dto

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

open class UserDto {
    @NotNull
    @NotEmpty
    @Pattern(regexp="[A-Za-z]*",message = "username.invalid")
    open lateinit var username: String

    @NotNull
    @NotEmpty
    @Pattern(regexp="^[A-Za-z\\d@\$!%*?&#]*\$",message = "username.invalid")
    open lateinit var password: String

    @NotNull
    @NotEmpty
    @Pattern(regexp="[A-Za-z]*",message = "username.invalid")
    open lateinit var fname: String

    @NotNull
    @NotEmpty
    @Pattern(regexp="[A-Za-z]*",message = "username.invalid")
    open lateinit var lname: String

    @NotNull
    @NotEmpty
    @Email
    open lateinit var email: String

}