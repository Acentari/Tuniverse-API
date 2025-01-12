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
class RequestFilter: OncePerRequestFilter() {
    var token: String? = null
    @Autowired
    lateinit var usersRepo: UsersRepo
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.requestURI == "/api/users/login" || request.requestURI == "/api/users/register") {
            filterChain.doFilter(request, response)
        } else {
            token = getTokenFromRequest(request)

            if (token.isNullOrEmpty()) {
                token = request.getParameter("access_token")
                println(token)
            } 

            if (token.isNullOrEmpty()) {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed")
            } else {
                
                val username = Jwts.parser().parse(token).body.toString()
                println(username)
                if (!usersRepo.existsByUsername(username)) {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed")
                } else {
                    request.setAttribute("username",username)
                    filterChain.doFilter(request, response)
                }
            }
        }
    }

    fun getTokenFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        return if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else {
            null
        }
    }
}