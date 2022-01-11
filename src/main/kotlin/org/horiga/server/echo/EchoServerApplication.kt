package org.horiga.server.echo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.UUID
import java.util.concurrent.Callable
import kotlin.random.Random

@SpringBootApplication
class EchoServerApplication

fun main(args: Array<String>) {
	runApplication<EchoServerApplication>(*args)
}

data class EchoResponseMessage(
	val message: String
)

data class VerifyResponseMessage(
	val userId: String
)

@RestController
class EchoRestController {

	@GetMapping("echo")
	fun echo(
		@RequestParam(name="message", required = false, defaultValue = "echo") message: String?,
		@RequestParam(name="delay", required = false, defaultValue = "10") delay: Long?
	) = Callable {
		Thread.sleep(delay!!)
		when (message) {
			"nf" -> throw ResponseStatusException(HttpStatus.NOT_FOUND)
			"er" -> throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
			else -> EchoResponseMessage(message!!)
		}
	}

	@GetMapping("verify")
	fun verify(
		@RequestParam(name="token") token: String
	) = Callable {
		when (token) {
			"error" -> throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
			"timeout" -> {
				Thread.sleep(20000)
				throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR)
			}
			else -> VerifyResponseMessage(UUID.randomUUID().toString())
		}
	}
}