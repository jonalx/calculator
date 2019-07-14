package com.example.calculator.api.http

import com.example.calculator.model.Calculator
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@RunWith(SpringRunner::class)
@WebMvcTest(CalculusController::class)
class CalculusControllerTest() {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var mvc: MockMvc

    @MockBean
    lateinit var calculator: Calculator

    @Test
    fun calculusShouldReturnErrorResponseWhenCalculatorThrowsException() {
        // given
        val mathExpression = "1 + ( 2"
        val encodedMathExpression = Base64.getEncoder().encodeToString(mathExpression.toByteArray(Charsets.UTF_8))

        val errorMessage = "invalid math expression"
        val expectedCalculusResponse = CalculusResponse(error = true, message = errorMessage)

        given(calculator.solve(mathExpression)).willThrow(IllegalArgumentException(errorMessage))

        // when + then
        mvc.perform(get("/calculus?query=$encodedMathExpression"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedCalculusResponse)))
    }

    @Test
    fun calculusShouldReturnComputedSuccessfulResponseWhenCalculatorComputesMathExpressionSuccessfully() {
        // given
        val mathExpression = "1 + 2"
        val encodedMathExpression = Base64.getEncoder().encodeToString(mathExpression.toByteArray(Charsets.UTF_8))

        val expectedResult = 3.0
        val expectedCalculusResponse = CalculusResponse(error = false, result = expectedResult)

        given(calculator.solve(mathExpression)).willReturn(expectedResult)

        // when + then
        mvc.perform(get("/calculus?query=$encodedMathExpression"))
                .andExpect(status().isOk)
                .andExpect(content().json(objectMapper.writeValueAsString(expectedCalculusResponse)))
    }

    @Test
    fun calculusShouldReturnErrorResponseWhenQueryParameterIsNotProvided() {
        // when + then
        mvc.perform(get("/calculus"))
                .andExpect(status().isBadRequest())
                .andExpect(status().reason("Required String parameter 'query' is not present"))
    }

    @Test
    fun calculusShouldReturnErrorResponseWhenMathExpressionCannotBeDecoded() {
        // given
        val encodedMathExpression = "invalid-base64-string"

        val errorMessage = try {
            Base64.getDecoder().decode(encodedMathExpression).toString()
        } catch (ex: Exception) {
            ex.message
        }
        val expectedCalculusResponse = CalculusResponse(error = true, message = errorMessage)

        // when + then
        mvc.perform(get("/calculus?query=$encodedMathExpression"))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedCalculusResponse)))
    }
}
