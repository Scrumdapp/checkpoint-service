package com.scrumdapp.checkinservice.controllers

import com.scrumdapp.checkinservice.entities.GroupFeature
import com.scrumdapp.checkinservice.services.GroupFeatureService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/features")
class GroupFeatureController(
    private val groupFeatureService: GroupFeatureService
) {

    @GetMapping
    fun getAllFeatures(): ResponseEntity<List<GroupFeature>> {
        return ResponseEntity.ok().body(groupFeatureService.getFeatures())
    }

    @GetMapping("/{key}")
    fun getByKey(@PathVariable key: String): ResponseEntity<List<GroupFeature>> {
        return ResponseEntity.ok().body(groupFeatureService.getFeature(key))
    }

    @PostMapping
    fun addGroupFeature(@RequestBody groupFeature: GroupFeature): ResponseEntity<GroupFeature> {
        return ResponseEntity.ok().body(groupFeatureService.createFeature(groupFeature))
    }
}