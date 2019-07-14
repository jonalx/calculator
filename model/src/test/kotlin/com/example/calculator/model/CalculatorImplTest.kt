package com.example.calculator.model

import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import assertk.assertions.isFailure
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import kotlin.test.Test

data class Scenario(val mathExpression: String, val result: Double? = null, val errorMessage: String? = null)

@RunWith(Parameterized::class)
class CalculatorImplTest(scenarioName: String, val scenario: Scenario) {

    val calculator = CalculatorImpl()

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: {0}")
        fun data() = listOf(
                arrayOf("should remove whitespaces",
                        Scenario(mathExpression = " 0  + ( 0 )  ", result = 0.0)),

                arrayOf("should prioritize parenthesis",
                        Scenario(mathExpression = "((2 + (3)) * 4)", result = 20.0)),

                arrayOf("should solve operands in the right order",
                        Scenario(mathExpression = "2 + 3 * 4 - 10 / 5", result = 12.0)),

                arrayOf("should fail when dividing by zero",
                        Scenario(mathExpression = "4 / 0", errorMessage = "Division by zero")),

                arrayOf("should fail when invalid mathematical expression",
                        Scenario(mathExpression = "0 + 0 )", errorMessage = "Invalid mathematical expression")),

                arrayOf("should fail when invalid mathematical expression",
                        Scenario(mathExpression = "0 + ( 0", errorMessage = "Invalid mathematical expression")),

                arrayOf("should fail when invalid mathematical expression",
                        Scenario(mathExpression = "0 ( + 0 )", errorMessage = "Invalid mathematical expression")),

                arrayOf("should fail when invalid mathematical expression",
                        Scenario(mathExpression = "0 ( 0 )", errorMessage = "Invalid mathematical expression")),

                arrayOf("should fail when invalid mathematical expression",
                        Scenario(mathExpression = "0 0 )", errorMessage = "Invalid mathematical expression")),

                arrayOf("should fail when invalid mathematical expression",
                        Scenario(mathExpression = "0 ( 0", errorMessage = "Invalid mathematical expression")),

                arrayOf("should fail when invalid mathematical expression",
                        Scenario(mathExpression = ") 0  0", errorMessage = "Invalid mathematical expression")),

                arrayOf("should fail when invalid mathematical expression",
                        Scenario(mathExpression = "( 0  0", errorMessage = "Invalid mathematical expression")),

                arrayOf("should fail when invalid mathematical expression",
                        Scenario(mathExpression = "( 0  ( 0", errorMessage = "Invalid mathematical expression")),

                arrayOf("should fail when invalid mathematical expression",
                        Scenario(mathExpression = "0 + -", errorMessage = "Invalid mathematical expression")),

                arrayOf("should fail when invalid mathematical expression",
                        Scenario(mathExpression = "0 + -0", errorMessage = "Invalid mathematical expression")),

                arrayOf("should fail when invalid mathematical expression",
                        Scenario(mathExpression = "* /", errorMessage = "Invalid mathematical expression")),

                arrayOf("should fail when invalid mathematical expression",
                        Scenario(mathExpression = "*", errorMessage = "Invalid mathematical expression")),

                arrayOf("should fail when invalid mathematical expression",
                        Scenario(mathExpression = " ", errorMessage = "Invalid mathematical expression"))
        )
    }

    @Test
    fun solve() {
        if (scenario.result != null) {
            assertThat(calculator.solve(scenario.mathExpression)).isEqualTo(scenario.result)
        } else {
            assertThat { calculator.solve(scenario.mathExpression) }.isFailure().hasMessage(scenario.errorMessage)
        }
    }
}
