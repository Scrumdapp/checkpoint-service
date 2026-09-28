package com.scrumdapp.checkpointservice.groups

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.scrumdapp.checkpointservice.errors.BadRequestException
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient.builder
import org.springframework.web.client.toEntity
import tools.jackson.core.type.TypeReference
import tools.jackson.databind.ObjectMapper

@JsonIgnoreProperties(ignoreUnknown = true)
data class GroupUserResponse(
    val user_id: Long,
    val group_id: Long,
    val first_name: String,
    val last_name: String,
    var is_ghost: Boolean
)

@Service
class GroupRequestService (
    @Value($$"${GROUP_SERVICE_URL}") private val baseUrl: String,
    @Value($$"${GROUP_FETCH_ENDPOINT:/groups/{groupId}/users}") private val fetchEndpoint: String,
    @Value($$"${spring.application.name}") private val appName: String
) {

    private val reqBuilder = builder().baseUrl(baseUrl).build()

    private val mapper = ObjectMapper()

    fun getGroupUsers(jwt: Jwt, groupId: Long): List<GroupUserResponse> {
        val uri = fetchEndpoint.replace("{groupId}", groupId.toString())
        try {
            val res = reqBuilder.get()
                .uri(uri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer ${jwt.tokenValue}")
                .header(HttpHeaders.VIA, appName)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity<String>()

            if (res.statusCode != HttpStatus.OK) {
                throw Exception("Unexpected response from user request")
            } else {
                val body = res.body ?: throw Exception("Unexpected response from user request")
                return mapper.readValue(body, object : TypeReference<List<GroupUserResponse>>() {})
            }
        } catch (e: Exception) {
            throw BadRequestException(message = "Downstream service is unreachable")
        }
    }
}