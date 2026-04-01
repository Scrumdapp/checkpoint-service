package com.scrumdapp.checkinservice.entities

import jakarta.persistence.*

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
     var id: Int = 0;

     var name: String? = null

    @Column(nullable = true)
     var background_preference: Int? = null

    @Column(nullable = true)
     var icon_preference: Int? = null

    @ManyToMany
    @JoinTable(
        name = "group_feature",
        joinColumns = [JoinColumn(name = "group_id")],
        inverseJoinColumns = [JoinColumn(name = "group_feature_key")]
    )
    @Column(nullable = true)
     var features: MutableSet<GroupFeature> = mutableSetOf()
}