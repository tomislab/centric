package com.tomislab.centric.repository;

import java.util.List;
import java.util.UUID;

import com.tomislab.centric.model.v1.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends PagingAndSortingRepository<Product, UUID> {
    List<Product> findByCategory(String category, Pageable pageable);
}
