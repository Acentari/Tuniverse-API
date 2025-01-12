package com.example.tuniverse.entities
import javax.persistence.*

@Entity
@Table(name = "tracks")
open class Track {
    @Id
    @Column(name = "track_id", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    open var trackId: Int? = null

    @Column(name="track_name", nullable = false)
    internal lateinit var trackName: String


    @Column(name="track_duration", nullable = false)
    open var duration: Long? = 0

    @ManyToOne
    @JoinColumn(name="user_id")
    internal lateinit var user: User
}