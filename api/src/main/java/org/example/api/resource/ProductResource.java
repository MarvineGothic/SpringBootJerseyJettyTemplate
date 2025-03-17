package org.example.api.resource;

import jakarta.ws.rs.*;
import lombok.RequiredArgsConstructor;
import org.example.database.entity.Product;
import org.example.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Path("/v1/product")
@RequiredArgsConstructor
public class ProductResource {
    private final ProductService productService;

    @POST
    public ResponseEntity<Product> saveProduct(Product product) {
        return ResponseEntity.ok(productService.saveProduct(product));
    }

    @GET
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GET
    @Path("/{id}")
    public ResponseEntity<Product> getProductById(@PathParam("id") Long id) {
        var product = productService.getProductById(id);
        return product.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PUT
    @Path("/{id}")
    public ResponseEntity<Product> updateProduct(@PathParam("id") Long id, Product product) {
        return ResponseEntity.ok(productService.updateProduct(id, product));
    }

    @DELETE
    @Path("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathParam("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}

