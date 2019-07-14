package com.example.calculator.api.http

import com.example.calculator.model.Calculator
import com.example.calculator.model.CalculatorImpl
import com.fasterxml.jackson.annotation.JsonInclude
import io.vertx.config.ConfigRetriever
import io.vertx.core.AbstractVerticle
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CalculusResponse(val error: Boolean, val result: Double? = null, val message: String? = null)

class CalculusHttpServerVerticle : AbstractVerticle() {

    val calculator: Calculator = CalculatorImpl()

    override fun start() {
        val router = Router.router(vertx)
        router.get("/calculus").handler(this::calculus)

        ConfigRetriever.create(vertx).getConfig { json ->
            val config = json.result()
            val serverPort = config.getInteger("server.port", 8080)

            vertx.createHttpServer().requestHandler(router).listen(serverPort) { result ->
                if (result.succeeded()) {
                    println("HTTP server started on port: $serverPort")
                } else {
                    println("HTTP server failed to bind port: $serverPort")
                }
            }
        }
    }

    private fun calculus(routingContext: RoutingContext) {
        try {
            CalculusResponse(error = false, result = calculator.solve(decodeQueryParam(getQueryParam(routingContext))))
        } catch (ex: Exception) {
            CalculusResponse(error = true, message = ex.localizedMessage)
        }.let { calculusResponse ->
            routingContext.response()
                    .putHeader("content-type", "application/json; charset=utf-8")
                    .setStatusCode(if (calculusResponse.error) 400 else 200)
                    .setStatusMessage(calculusResponse.message ?: "OK")
                    .end(Json.encode(calculusResponse))
        }
    }

    private fun decodeQueryParam(query: String) = Base64.getDecoder().decode(query).toString(Charsets.UTF_8)

    private fun getQueryParam(routingContext: RoutingContext): String {
        require(routingContext.queryParam("query").size == 1) { "Expected one query parameter 'query'" }
        return routingContext.queryParam("query").first()!!
    }
}
