package com.scrumdapp.checkinservice.controllers

import com.scrumdapp.checkinservice.dto.CheckInDto
import com.scrumdapp.checkinservice.services.CheckInService
import org.springframework.http.ResponseEntity
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/checkins")
class CheckInController(
    private val checkInService: CheckInService
) {




    @PostMapping
    fun createCheckIn(@RequestBody checkInDto: CheckInDto): ResponseEntity<CheckInDto> {
        val created = checkInService.saveCheckIn(checkInDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(created)
    }


    @GetMapping("/{groupId}/{userId}/{date}")
    fun getById(
        @PathVariable groupId: Int,
        @PathVariable userId: Int,
        @PathVariable date: String
    ): ResponseEntity<CheckInDto> {

        val localDate = LocalDate.parse(date)
        val id = userId, groupId, localDate

        val result = checkInService.findById(id)

        return if (result != null) {
            ResponseEntity.ok(result)
        } else {
            ResponseEntity.notFound().build()
        }
    }


    @GetMapping("/group/{groupId}")
    fun getByGroup(@PathVariable groupId: Int): ResponseEntity<List<CheckInDto>> {
        return ResponseEntity.ok(checkInService.findByGroupId(groupId))
    }


    @GetMapping("/group/{groupId}/date/{date}")
    fun getByGroupAndDate(
        @PathVariable groupId: Int,
        @PathVariable date: String
    ): ResponseEntity<List<CheckInDto>> {

        val localDate = LocalDate.parse(date)
        return ResponseEntity.ok(
            checkInService.findByGroupIdAndDate(groupId, localDate)
        )
    }


    @GetMapping("/group/{groupId}/user/{userId}")
    fun getByUserAndDateRange(
        @PathVariable groupId: Int,
        @PathVariable userId: Int,
        @RequestParam start: String,
        @RequestParam end: String
    ): ResponseEntity<List<CheckInDto>> {

        val startDate = LocalDate.parse(start)
        val endDate = LocalDate.parse(end)

        return ResponseEntity.ok(
            checkInService.findByUserAndDateRange(groupId, userId, startDate, endDate)
        )
    }


    @DeleteMapping("/{groupId}/{userId}/{date}")
    fun deleteCheckIn(
        @PathVariable groupId: Int,
        @PathVariable userId: Int,
        @PathVariable date: String
    ): ResponseEntity<Void> {

        val localDate = LocalDate.parse(date)
        val id = CheckInId(userId, groupId, localDate)

        checkInService.deleteById(id)
        return ResponseEntity.noContent().build()
    }
}