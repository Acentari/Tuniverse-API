package com.example.tuniverse.controllers

import com.example.tuniverse.dto.LoginDto
import com.example.tuniverse.dto.UserDto
import com.example.tuniverse.services.UsersService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.NotNull

@RestController
@RequestMapping("/api/users")
class UsersController(val usersService: UsersService) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    fun createUser(
        @Valid
        @RequestBody
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