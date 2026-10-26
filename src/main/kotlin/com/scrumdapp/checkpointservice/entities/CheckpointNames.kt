package com.scrumdapp.checkpointservice.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.SequenceGenerator
import jakarta.persistence.Table

@Entity
@Table(name = "check_point_names")
class CheckpointNames(
        group_id: Int,
        checkpointNames: List<String>
) {

        @Id
        @GeneratedValue(
                strategy = GenerationType.SEQUENCE,
                generator = "checkpoint_names_id_generator"
        )
        @SequenceGenerator(
                name = "checkpoint_names_id_generator",
                sequenceName = "check_point_names_seq",
                allocationSize = 1
        )
        @Column(nullable = false)
        val id: Long = 0

        @Column(name = "group_id", nullable = false, unique = true)
        var groupId = group_id

        @Column(name = "checkpoint_names", columnDefinition = "TEXT[]")
        var checkpoint_names: List<String> = checkpointNames
}
