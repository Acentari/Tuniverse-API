package com.example.tuniverse.services

import com.example.tuniverse.dto.AuthenticationDto
import com.example.tuniverse.dto.LoginDto
import com.example.tuniverse.dto.UserDto
import com.example.tuniverse.repos.UsersRepo
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import io.jsonwebtoken.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.security.auth.login.*
import java.io.*

@Service
class UsersService(
    val usersRepo: UsersRepo,
    val mapperUserService: MapperUserService,
) {
    fun createUser(
        userDto: UserDto
    ) {
        if (usersRepo.existsByEmail(userDto.email)){
            throw DataIntegrityViolationException("This email already exists")
        }
        if (usersRepo.existsByUsername(userDto.username)){
            throw DataIntegrityViolationException("This username already exists")
        }
        try {
            val folder = File("/var/www/html/"+userDto.username)
            folder.mkdir()
            usersRepo.save(mapperUserService.userDtoToUser(userDto))
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun login(loginDto: LoginDto): Any? {
        val authDto = AuthenticationDto()
        var token = ""
        if (usersRepo.existsByUsername(loginDto.username)) {
            val user = usersRepo.findByUsername(loginDto.username)
            val encrypt = BCryptPasswordEncoder()
            if(encrypt.matches(loginDto.password, user.password)){
                token = Jwts.builder()
                    .setPayload(loginDto.username)
                    .compact()
                authDto.token = token
            }
            else {
                throw LoginException("Authentication failed")
            }
        }
        else {
            throw LoginException("Authentication failed")
        }
        println(Jwts.parser().parse(token).body)
        return authDto
    }

    fun auth(token: String) {
        val username = Jwts.parser().parse(token).body.toString()
        if (!usersRepo.existsByUsername(username)) {
            throw LoginException("Authentication failed")
        }
    }
}