package com.example.tuniverse.controllers

import com.example.tuniverse.dto.LoginDto
import com.example.tuniverse.dto.UserDto
import com.example.tuniverse.services.UsersService
import org.apache.catalina.connector.Request
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/users")
class UsersController(val usersService: UsersService) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping()
    fun createUser(
        @RequestBody
        @Valid
        @NotNull
        user: UserDto
    ): Any {
        return usersService.createUser(user)
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    fun login(
        @RequestBody
        @NotNull
        loginDto: LoginDto
    ): Any? {
        return usersService.login(loginDto)
    }
}