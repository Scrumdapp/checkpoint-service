package com.scrumdapp.checkinservice.entities

import jakarta.persistence.*

@Entity
@Table(name = "features")
class GroupFeature {

    @Id
    @Column(length = 200)
     var key: String = ""

    @Column(nullable = true)
     var description: String? = null
}