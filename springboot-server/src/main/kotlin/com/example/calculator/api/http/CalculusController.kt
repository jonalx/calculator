package com.example.calculator.api.http

import com.example.calculator.model.Calculator
import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CalculusResponse(val error: Boolean, val result: Double? = null, val message: String? = null)

@RestController
class CalculusController(val calculator: Calculator) {

    @GetMapping("/calculus")
    fun calculus(@RequestParam query: String): ResponseEntity<CalculusResponse> =
            try {
                CalculusResponse(error = false, result = calculator.solve(decodeQueryParam(query)))
            } catch (ex: Exception) {
                CalculusResponse(error = true, message = ex.localizedMessage)
            }.let { calculusResponse ->
                ResponseEntity(calculusResponse, if (calculusResponse.error) HttpStatus.BAD_REQUEST else HttpStatus.OK)
            }

    private fun decodeQueryParam(query: String) = Base64.getDecoder().decode(query).toString(Charsets.UTF_8)
}
