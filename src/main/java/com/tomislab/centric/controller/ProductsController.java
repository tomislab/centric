package com.tomislab.centric.controller;

import com.tomislab.centric.repository.ProductRepository;
import com.tomislab.centric.model.v1.Product;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping()
public class ProductsController {
    @Autowired
    ProductRepository productRepository;

    @GetMapping("/v1/products")
    public ResponseEntity<List<Product>> findAll() {
        final List<Product> all = new ArrayList<>();
        for (Product p : productRepository.findAll()) {
            all.add(p);
        }
        return new ResponseEntity(all, HttpStatus.OK);
    }

    @GetMapping("/v1/products/{id}")
    public ResponseEntity<Product> findOne(@PathVariable UUID id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            return new ResponseEntity<Product>(product.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<Product>(product.get(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/v1/products")
    public ResponseEntity<Product> create(@RequestBody Product newProduct) {
        return new ResponseEntity(productRepository.save(newProduct), HttpStatus.CREATED);
    }

    @GetMapping("/v1/products/findByCategory")
    public ResponseEntity<List<Product>> findByCategory(@RequestParam(value = "category") String category,
                                        @RequestParam(value = "page", defaultValue = "0") Integer page,
                                        @RequestParam(value = "size", required = false) Integer size) {
        if (page == null) {
            page = 0;
        }
        if (size == null) {
            size = Integer.MAX_VALUE;
        }

        if (page < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (size < 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Pageable paged_and_sorted_by_category = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return new ResponseEntity(productRepository.findByCategory(category, paged_and_sorted_by_category), HttpStatus.OK);
    }
}
