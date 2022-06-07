package com.example.tuniverse

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import com.example.tuniverse.repos.UsersRepo
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain

@Component
class RequestFilter : OncePerRequestFilter() {
    @Autowired
    lateinit var usersRepo: UsersRepo
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        if (request.requestURI == "/api/users/login" || request.requestURI == "/api/users") {
            filterChain.doFilter(request, response)
        } else {
            if (request.getHeader("Authorization") == null) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed")
            } else {
                val token = request.getHeader("Authorization").substring(7)
                val username = Jwts.parser().parse(token).body.toString()
                if (!usersRepo.existsByUsername(username)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed")
                } else {
                    request.setAttribute("username",username)
                    filterChain.doFilter(request, response)
                }
            }
        }
    }
}