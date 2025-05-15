package com.backend.orderservice.controllers;


/*
 * @description
 * @author: Pham Kim Khuong
 * @version: 1.0
 * @created: 2/23/2025 8:26 AM
 */

import com.backend.orderservice.dtos.OrderDetailDTO;
import com.backend.orderservice.dtos.request.CreateOrderDetail;
import com.backend.orderservice.dtos.response.OrderDetailResponse;
import com.backend.orderservice.service.OrderDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/order-details")
@Tag(name = "Order Detail Query", description = "Order Detail API")
public class OrderDetailController {
    private final OrderDetailService orderService;

    public OrderDetailController(OrderDetailService orderService) {
        this.orderService = orderService;
    }

    @Operation(
            description = "Get all order details",
            summary = "Get all order details",
            tags = {"Order Detail Query"},
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
            description = "Save order detail",
            summary = "Save order detail",
            tags = {"Order Detail Query"},
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
    public ResponseEntity<OrderDetailResponse> save(@Valid @RequestBody
                                                    @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                            description = "Order object that needs to be added to the store",
                                                            required = true,
                                                            content = @io.swagger.v3.oas.annotations.media.Content(
                                                                    mediaType = "application/json",
                                                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrderDetailDTO.class)
                                                            )
                                                    )

                                                    CreateOrderDetail orderDTO


    ) {
        OrderDetailResponse c = orderService.save(orderDTO);
        return new ResponseEntity<>(c, HttpStatus.CREATED);
    }

    @Operation(
            description = "Delete order detail",
            summary = "Delete order detail",
            tags = {"Order Detail Query"},
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
            description = "Update order detail",
            summary = "Update order detail",
            tags = {"Order Detail Query"},
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
    public ResponseEntity<OrderDetailResponse> update(@PathVariable Long id, @Valid @RequestBody
                                                      @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                              description = "Order object that needs to be updated to the store",
                                                              required = true,
                                                              content = @io.swagger.v3.oas.annotations.media.Content(
                                                                      mediaType = "application/json",
                                                                      schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrderDetailDTO.class)
                                                              )
                                                      ) CreateOrderDetail orderDTO
    ) {
        OrderDetailResponse c = orderService.update(id, orderDTO);
        return new ResponseEntity<>(c, HttpStatus.OK);

    }

    @Operation(
            description = "Get order detail by ID",
            summary = "Get order detail by ID",
            tags = {"Order Detail Query"},
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
    public ResponseEntity<OrderDetailResponse> getById(@PathVariable
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

}
