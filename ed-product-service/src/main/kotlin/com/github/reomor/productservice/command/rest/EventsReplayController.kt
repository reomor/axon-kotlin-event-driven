package com.github.reomor.productservice.command.rest

import com.github.reomor.productservice.command.rest.dto.EventProcessorResetResponse
import org.axonframework.config.EventProcessingConfiguration
import org.axonframework.eventhandling.TrackingEventProcessor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/management")
class EventsReplayController(
  private val eventProcessingConfiguration: EventProcessingConfiguration
) {

  @PostMapping("/eventProcessor/{processorName}/reset")
  fun replayEvents(@PathVariable processorName: String): ResponseEntity<EventProcessorResetResponse> {

    val eventProcessorOpt = eventProcessingConfiguration.eventProcessor<TrackingEventProcessor>(
      processorName,
      TrackingEventProcessor::class.java
    )

    return eventProcessorOpt.map { eventProcessor ->
      eventProcessor.shutDown()
      eventProcessor.resetTokens()
      eventProcessor.start()
      ResponseEntity.ok()
        .body(
          EventProcessorResetResponse(
            message = "The event processor $processorName has been reset"
          )
        )
    }.orElseGet {
      ResponseEntity.badRequest()
        .body(
          EventProcessorResetResponse(
            message = "The event processor $processorName is not found or is not a tracking"
          )
        )
    }
  }
}
