package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.database.entity.Product;
import org.example.database.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product updateProduct(Long id, Product updateProduct) {
        var existingProduct = productRepository.findById(id);
        return existingProduct.map(
                product -> {
                    product.setName(updateProduct.getName());
                    product.setPrice(updateProduct.getPrice());
                    product.setQuantity(updateProduct.getQuantity());
                    return productRepository.save(product);
                }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}
