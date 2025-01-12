package com.example.tuniverse.entities

import javax.persistence.*

@Entity
@Table(name = "users")
open class User{
    @Id
    @Column(name = "user_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    open var userId: Int? = null

    @Column(name = "username", nullable = false)
    open lateinit var username: String

    @Column(name = "password", nullable = false)
    open lateinit var password: String

    @Column(name = "fname", nullable = false)
    open lateinit var fname: String

    @Column(name = "lname", nullable = false)
    open lateinit var lname: String

    @Column(name = "email", nullable = false)
    open lateinit var email: String

    @OneToMany(mappedBy = "user")
    open var tracks: MutableList<Track> = mutableListOf()

}