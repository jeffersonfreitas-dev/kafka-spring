package com.appsdeveloperblog.ws.products.service;

import com.appsdeveloperblog.ws.products.dto.CreatedProductRequest;

public interface ProductService {

    String create(CreatedProductRequest productRequest);
}
