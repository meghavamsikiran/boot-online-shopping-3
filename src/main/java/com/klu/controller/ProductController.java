package com.klu.controller;

import com.klu.entity.Product;
import com.klu.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/shop/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/findByProdName")
    public List<Product> getProductsByProdName(@RequestParam String prodName) {
        return productService.getProductsByProdName(prodName);
    }

    @GetMapping("/findByCategory")
    public List<Product> getProductsByCategory(@RequestParam String category) {
        return productService.getProductsByCategory(category);
    }

    @GetMapping("/findByPriceGreaterThan")
    public List<Product> getProductsByPriceGreaterThan(@RequestParam Double price) {
        return productService.getProductsByPriceGreaterThan(price);
    }

    @PostMapping
    public Product saveProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product updatedProduct) {
        Optional<Product> existingProductOptional = productService.getProductById(id);

        if (!existingProductOptional.isPresent()) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if product does not exist
        }

        Product existingProduct = existingProductOptional.get();
        
        // Update the existing product's details with the new data
        existingProduct.setProdName(updatedProduct.getProdName());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setPrice(updatedProduct.getPrice());
        
        // Save the updated product
        Product savedProduct = productService.saveProduct(existingProduct);
        
        return ResponseEntity.ok(savedProduct); // Return 200 OK with the updated product details
    }
}
