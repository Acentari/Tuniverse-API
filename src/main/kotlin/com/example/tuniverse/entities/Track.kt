package com.example.tuniverse.entities
import javax.persistence.*

@Entity
@Table(name = "tracks")
open class Track{
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    open var id: String? = null

    @Column(name = "username", nullable = false)
    open lateinit var username: String

    @Column(name = "trackname", nullable = false)
    open lateinit var trackname: String
}