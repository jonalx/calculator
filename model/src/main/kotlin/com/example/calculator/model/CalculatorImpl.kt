package com.example.calculator.model

import java.util.*

const val DIVISION_BY_ZERO = "Division by zero"
const val UNSUPPORTED_OPERATOR = "Unsupported operator"
const val INVALID_MATHEMATICAL_EXPRESSION = "Invalid mathematical expression"

fun Double.compute(operator: String, other: Double) =
        when (operator) {
            "+" -> this + other
            "-" -> this - other
            "*" -> this * other
            "/" -> if (other != 0.0) this / other else throw ArithmeticException(DIVISION_BY_ZERO)
            else -> throw IllegalArgumentException("$UNSUPPORTED_OPERATOR $operator")
        }

class CalculatorImpl : Calculator {

    val MINOR_ORDER_OPERATORS = listOf("+", "-")
    val MAJOR_ORDER_OPERATORS = listOf("*", "/")

    override fun solve(mathExpression: String) =
            try {
                solve(StringTokenizer(mathExpression.replace(" ", ""), "(+-*/)", true).toList().map(Any::toString))
            } catch (ex: ArithmeticException) {
                throw ex
            } catch (ex: Exception) {
                throw IllegalArgumentException(INVALID_MATHEMATICAL_EXPRESSION, ex)
            }

    private fun solve(tokens: List<String>): Double =
            when {
                tokens.size == 1 -> tokens[0].toDouble()
                tokens.first() == "(" && tokens.last() == ")" -> solve(tokens.subList(1, tokens.size - 1))
                else -> findNextOperatorIndex(tokens).let {
                    solve(tokens.subList(0, it)).compute(tokens[it], solve(tokens.subList(it + 1, tokens.size)))
                }
            }

    private fun findNextOperatorIndex(tokens: List<String>): Int {
        var parenthesisGroupCount = 0
        var majorOrderOperatorIndex: Int? = null

        tokens.forEachIndexed { i, token ->
            when {
                token == "(" -> parenthesisGroupCount++
                token == ")" -> parenthesisGroupCount--
                majorOrderOperatorIndex == null && token in MAJOR_ORDER_OPERATORS -> majorOrderOperatorIndex = i
                token in MINOR_ORDER_OPERATORS && parenthesisGroupCount == 0 -> return i
            }
        }

        return majorOrderOperatorIndex!!
    }
}
