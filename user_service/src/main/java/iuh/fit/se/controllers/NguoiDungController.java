/*
 * @(#) $(NAME).java    1.0     2/13/2025
 *
 * Copyright (c) 2025 IUH. All rights reserved.
 */

package iuh.fit.se.controllers;

/*
 * @description
 * @author: Tran Tan Dat
 * @version: 1.0
 * @created: 13-February-2025 8:26 PM
 */

import iuh.fit.se.entities.NguoiDung;
import iuh.fit.se.services.NguoiDungService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RepositoryRestController
public class NguoiDungController {
    @Autowired
    private NguoiDungService nguoiDungService;

    @GetMapping("/nguoidung/{id}")
    public ResponseEntity<Map<String, Object>> getNguoiDungById(@PathVariable int id) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", HttpStatus.OK.value());
        response.put("data", nguoiDungService.findById(id));
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/nguoidung")
    public ResponseEntity<Map<String, Object>> saveNguoiDung(
            @Valid @RequestBody NguoiDung nguoiDung,
            BindingResult bindingResult) {
        Map<String, Object> response = new LinkedHashMap<>();

        if (bindingResult.hasErrors()) {
            Map<String, Object> errors = new LinkedHashMap<>();

            bindingResult.getFieldErrors().forEach(result ->
                errors.put(result.getField(), result.getDefaultMessage())
            );

            System.out.println(bindingResult);
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("errors", errors);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        else {
            response.put("status", HttpStatus.OK.value());
            response.put("data", nguoiDungService.save(nguoiDung));
            System.out.println(nguoiDung);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
    }

}
