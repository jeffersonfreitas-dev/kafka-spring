package com.appsdeveloperblog.ws.products.controller;

import com.appsdeveloperblog.ws.products.dto.CreatedProductRequest;
import com.appsdeveloperblog.ws.products.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }


    @PostMapping
    public ResponseEntity<String> crate(@RequestBody CreatedProductRequest product){
        final var result = service.create(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
