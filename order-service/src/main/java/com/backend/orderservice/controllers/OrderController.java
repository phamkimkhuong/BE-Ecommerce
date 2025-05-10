package com.backend.orderservice.controllers;


/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2/23/2025 8:26 AM
 */

import com.backend.commonservice.service.KafkaService;
import com.backend.orderservice.dtos.OrderDTO;
import com.backend.orderservice.dtos.request.CartOrderRequest;
import com.backend.orderservice.dtos.response.OrderResponse;
import com.backend.orderservice.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/orders")
@Tag(name = "Order Query", description = "Order API")
@Slf4j
public class OrderController {
    private final OrderService orderService;
    private final KafkaService kafkaService;

    public OrderController(OrderService orderService
            , KafkaService kafkaService
    ) {
        this.orderService = orderService;
        this.kafkaService = kafkaService;
    }

    @Operation(
            description = "Get all orders",
            summary = "Get all orders",
            tags = {"Order Query"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
                    )

            }
    )
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllOrders(@RequestParam(required = false) String keyword) {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        if (keyword == null || keyword.isEmpty()) {
            response.put("data", orderService.getAll());
        } else {
//            response.put("data", productService.search(keyword));
        }
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            description = "Save order",
            summary = "Save order",
            tags = {"Order Query"},
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Successful operation",
                            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
                    )

            }
    )
    @PostMapping
    public ResponseEntity<OrderResponse> save(@Valid @RequestBody
                                              @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                      description = "Order object that needs to be added to the store",
                                                      required = true,
                                                      content = @io.swagger.v3.oas.annotations.media.Content(
                                                              mediaType = "application/json",
                                                              schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = CartOrderRequest.class)
                                                      )
                                              ) CartOrderRequest cartOrderRequest
    ) {
        OrderResponse response = orderService.save(cartOrderRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(
            description = "Delete order",
            summary = "Delete order",
            tags = {"Order Query"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Order not found",
                            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
                    )

            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", orderService.delete(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(
            description = "Update order",
            summary = "Update order",
            tags = {"Order Query"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Order not found",
                            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
                    )

            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> update(@PathVariable Long id, @Valid @RequestBody
                                           @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                   description = "Order object that needs to be updated to the store",
                                                   required = true,
                                                   content = @io.swagger.v3.oas.annotations.media.Content(
                                                           mediaType = "application/json",
                                                           schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrderDTO.class)
                                                   )
                                           ) OrderDTO orderDTO
    ) {
        orderService.update(id, orderDTO);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);

    }

    @Operation(
            description = "Get order by ID",
            summary = "Get order by ID",
            tags = {"Order Query"},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Order not found",
                            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = {@io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json")}
                    )

            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getById(@PathVariable
                                                 @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                         description = "ID of order to return",
                                                         required = true,
                                                         content = @io.swagger.v3.oas.annotations.media.Content(
                                                                 mediaType = "application/json",
                                                                 schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = Long.class)
                                                         )
                                                 )
                                                 Long id
    ) {
        return new ResponseEntity<>(orderService.getById(id), HttpStatus.OK);
    }

    @PostMapping("/sendMessage")
    public void sendMessage(@RequestBody String message) {
        kafkaService.sendMessage("test", message);
    }

}
