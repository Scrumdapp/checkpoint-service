package com.scrumdapp.checkinservice.controllers

import com.scrumdapp.checkinservice.dto.CheckInDto
import com.scrumdapp.checkinservice.dto.GroupCreateDto
import com.scrumdapp.checkinservice.dto.GroupPatchDto
import com.scrumdapp.checkinservice.dto.GroupResponseDto
import com.scrumdapp.checkinservice.entities.CheckIn
import com.scrumdapp.checkinservice.entities.Group
import com.scrumdapp.checkinservice.services.CheckInService
import com.scrumdapp.checkinservice.services.GroupService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.logging.Logger
import java.time.LocalDate

@RestController
@RequestMapping("/groups")
class GroupController(
    private val groupService: GroupService,
    private val checkInService: CheckInService
) {

    @GetMapping
    fun getAllGroups(): ResponseEntity<List<Group>> {
        return ResponseEntity.ok(groupService.getAllGroups())
    }

    @GetMapping("/{id}")
    fun getGroup(@PathVariable id: Int): ResponseEntity<Group> {
        val group = groupService.getGroupById(id)
        println("Group: ${group?.id}")
        return ResponseEntity.ok(group);
    }

    @GetMapping("/{id}/checkins")
    fun getGroupCheckIns(
        @PathVariable id: Int,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) dateTime: LocalDate
    ): List<CheckInDto> {

        return checkInService.findByGroupIdAndDate(id, dateTime)
    }

    @GetMapping("/{groupId}/users/{userId}/checkins")
    fun getUserCheckInsBetweenDates(
        @PathVariable groupId: Int,
        @PathVariable userId: Int,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) startdate: LocalDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) enddate: LocalDate?
    ): List<CheckInDto> {

        return checkInService.findByUserAndDateRange(
            groupId,
            userId,
            startdate,
            enddate ?: startdate
        )
    }

    @PostMapping
    fun createGroup(@Valid @RequestBody group: GroupCreateDto): ResponseEntity<GroupResponseDto> {
        println("Group: ${group.name} + Features: ${group.features}")
        val group = groupService.createGroup(group)
        return ResponseEntity.status(HttpStatus.CREATED).body(group)
    }

    @PatchMapping("/{id}")
    fun updateGroup(
        @PathVariable id: Int,
        @Valid @RequestBody group: GroupPatchDto): ResponseEntity<GroupResponseDto> {
        return ResponseEntity.ok(groupService.updateGroup(id, group))
    }

    @PatchMapping("/{id}/checkins")
    fun updateCheckIn(@RequestBody checkIn: CheckInDto): ResponseEntity<CheckInDto> {
        return ResponseEntity.ok(checkInService.saveCheckIn(checkIn))
    }

    @PatchMapping("/{groupId}/users/{userId}/checkins")
    fun updateUserCheckIn(@RequestBody checkIn: CheckInDto): ResponseEntity<CheckInDto> {
        return ResponseEntity.ok(checkInService.saveCheckIn(checkIn))
    }

    @DeleteMapping
    fun deleteGroup(@RequestBody id: Int): ResponseEntity<Group> {
        groupService.deleteGroup(id)
        return ResponseEntity.ok().build()
    }
}