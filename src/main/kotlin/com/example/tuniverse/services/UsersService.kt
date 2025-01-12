package com.example.tuniverse.services

import com.example.tuniverse.dto.AuthenticationDto
import com.example.tuniverse.dto.LoginDto
import com.example.tuniverse.dto.UserDto
import com.example.tuniverse.exceptions.ValidationErrorException
import com.example.tuniverse.repos.UsersRepo
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.io.File

@Service
class UsersService(
    val usersRepo: UsersRepo,
    val mapperUserService: MapperUserService,
) {

    @Value("\${app.path}")
    private val appPath: String? = null

    fun createUser(
        userDto: UserDto
    ) {
        if (usersRepo.existsByEmail(userDto.email)){
            throw ValidationErrorException("This email already exists")
        }
        if (usersRepo.existsByUsername(userDto.username)){
            throw ValidationErrorException("This username already exists")
        }
        try {
            val folderPath = "$appPath/${userDto.username}"
            val folder = File(folderPath)
            val created = folder.mkdirs()
            usersRepo.save(mapperUserService.userDtoToUser(userDto))
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun login(loginDto: LoginDto): Any? {
        val authDto = AuthenticationDto()
        var token = ""
        println(loginDto.username)
        if (usersRepo.existsByUsername(loginDto.username)) {
            val user = usersRepo.findByUsername(loginDto.username)
            val encrypt = BCryptPasswordEncoder()
            if(encrypt.matches(loginDto.password, user.password)){
                token = Jwts.builder().setPayload(loginDto.username).compact()
                authDto.token = token
            }
            else {
                throw ValidationErrorException("Authentication failed")
            }
        }
        else {
            throw ValidationErrorException("Authentication failed")
        }

        println(Jwts.parser().parse(token).body)
        return authDto
    }


    fun auth(token: String) {
        val username = Jwts.parser().parse(token).body.toString()
        if (!usersRepo.existsByUsername(username)) {
            throw ValidationErrorException("Authentication failed")
        }
    }
}






