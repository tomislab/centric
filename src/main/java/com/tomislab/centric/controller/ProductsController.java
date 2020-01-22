package com.tomislab.centric.controller;

import com.tomislab.centric.repository.ProductRepository;
import com.tomislab.centric.model.v1.Product;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping()
public class ProductsController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/v1/products")
    public List<Product> findAll() {
        final List<Product> all = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            all.add(p);
        }
        return all;
    }

    @GetMapping("/v1/products/{id}")
    public Product findOne(@PathVariable UUID id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Couldn't find Product with id: " + id));
    }

    @PostMapping("/v1/products")
    public Product create(@RequestBody Product newProduct) {
        return productRepository.save(newProduct);
    }

    @GetMapping("/v1/products/findByCategory")
    public List<Product> findByCategory(@RequestParam(value = "category") String category,
                                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                                        @RequestParam(value = "size", required = false) Integer size) {
        if (size == null) {
            size = Integer.MAX_VALUE;
        }
        Pageable paged_and_sorted_by_category = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return productRepository.findByCategory(category, paged_and_sorted_by_category);
    }
}
