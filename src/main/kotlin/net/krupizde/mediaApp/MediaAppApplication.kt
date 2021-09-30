package net.krupizde.mediaApp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MediaAppApplication

fun main(args: Array<String>) {
    runApplication<MediaAppApplication>(*args)
}
