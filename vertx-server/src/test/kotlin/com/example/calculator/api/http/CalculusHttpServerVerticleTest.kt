package com.example.calculator.api.http

import io.vertx.core.json.JsonObject
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.RunTestOnContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.Rule
import org.junit.runner.RunWith
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@RunWith(VertxUnitRunner::class)
class CalculusHttpServerVerticleTest {

    @Rule
    @JvmField
    val rule = RunTestOnContext()

    @BeforeTest
    fun setup(context: TestContext) {
        rule.vertx().deployVerticle(CalculusHttpServerVerticle::class.qualifiedName, context.asyncAssertSuccess())
    }

    @AfterTest
    fun teardown(context: TestContext) {
        rule.vertx().close(context.asyncAssertSuccess())
    }

    @Test
    fun calculusShouldReturnErrorResponseWhenCalculatorThrowsException(context: TestContext) {
        // given
        val mathExpression = "1 + ( 2"
        val encodedMathExpression = Base64.getEncoder().encodeToString(mathExpression.toByteArray(Charsets.UTF_8))

        val expectedCalculusResponse = CalculusResponse(error = true, message = "Invalid mathematical expression")

        // when
        val async = context.async()
        rule.vertx().createHttpClient().get(8080, "localhost", "/calculus?query=$encodedMathExpression") { response ->
            // then
            context.assertEquals(400, response.statusCode())
            context.assertEquals(expectedCalculusResponse.message, response.statusMessage())
            response.bodyHandler { body ->
                context.assertEquals(JsonObject.mapFrom(expectedCalculusResponse), body.toJsonObject())
                async.complete()
            }
        }.end()
    }

    @Test
    fun calculusShouldReturnComputedSuccessfulResponseWhenCalculatorComputesMathExpressionSuccessfully(context: TestContext) {
        // given
        val mathExpression = "1 + 2"
        val encodedMathExpression = Base64.getEncoder().encodeToString(mathExpression.toByteArray(Charsets.UTF_8))

        val expectedResult = 3.0
        val expectedCalculusResponse = CalculusResponse(error = false, result = expectedResult)

        // when
        val async = context.async()
        rule.vertx().createHttpClient().get(8080, "localhost", "/calculus?query=$encodedMathExpression") { response ->
            // then
            context.assertEquals(200, response.statusCode())
            context.assertEquals("OK", response.statusMessage())
            response.bodyHandler { body ->
                context.assertEquals(JsonObject.mapFrom(expectedCalculusResponse), body.toJsonObject())
                async.complete()
            }
        }.end()
    }

    @Test
    fun calculusShouldReturnErrorResponseWhenQueryParameterIsNotProvided(context: TestContext) {
        // given
        val expectedCalculusResponse = CalculusResponse(error = true, message = "Expected one query parameter 'query'")

        // when
        val async = context.async()
        rule.vertx().createHttpClient().get(8080, "localhost", "/calculus") { response ->
            // then
            context.assertEquals(400, response.statusCode())
            context.assertEquals(expectedCalculusResponse.message, response.statusMessage())
            response.bodyHandler { body ->
                context.assertEquals(JsonObject.mapFrom(expectedCalculusResponse), body.toJsonObject())
                async.complete()
            }
        }.end()
    }

    @Test
    fun calculusShouldReturnErrorResponseWhenMathExpressionCannotBeDecoded(context: TestContext) {
        // given
        val encodedMathExpression = "invalid-base64-string"

        val errorMessage = try {
            Base64.getDecoder().decode(encodedMathExpression).toString()
        } catch (ex: Exception) {
            ex.message
        }
        val expectedCalculusResponse = CalculusResponse(error = true, message = errorMessage)

        // when
        val async = context.async()
        rule.vertx().createHttpClient().get(8080, "localhost", "/calculus?query=$encodedMathExpression") { response ->
            // then
            context.assertEquals(400, response.statusCode())
            context.assertEquals(expectedCalculusResponse.message, response.statusMessage())
            response.bodyHandler { body ->
                context.assertEquals(JsonObject.mapFrom(expectedCalculusResponse), body.toJsonObject())
                async.complete()
            }
        }.end()
    }
}
