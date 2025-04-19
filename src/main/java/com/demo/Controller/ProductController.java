package com.demo.Controller;

import com.demo.Entity.Product;
import com.demo.Payload.ApiResponse;
import com.demo.Service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    // GET: Retrieve all Product
    // URL: http://localhost:8080/api/v1/product
    @GetMapping("/product")
    public ResponseEntity<ApiResponse<?>> getAllProducts() {
        List<Product> allProduct = productService.getAllProduct();
        if (allProduct.isEmpty()) {
            ApiResponse<Object> response = new ApiResponse<>(false, "Product List Empty!!!", null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        ApiResponse<List<Product>> response = new ApiResponse<>(true, "Product List Found!!!", allProduct);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // GET: Retrieve a single Product by ID
    // URL: http://localhost:8080/api/v1/product/{pid}
    @GetMapping("/product/{pid}")
    public ResponseEntity<ApiResponse<?>> getProductById(@PathVariable("pid") Integer id) {
        Product productById = productService.getProductById(id);
        if (productById != null) {
            ApiResponse<Product> response = new ApiResponse<>(true, "Product Found By Id!!!", productById);
            return ResponseEntity.status(HttpStatus.FOUND).body(response);
        }
        ApiResponse<List<Product>> response = new ApiResponse<>(false, "Product Not Found By Id!!!", null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // POST: Save a new Product
    // URL: http://localhost:8080/api/v1/product
    @PostMapping(value = "/product")
    public ResponseEntity<ApiResponse<?>> saveProduct(@RequestBody Product product) {
        try {
            Product saveProduct = productService.saveProduct(product);
            ApiResponse<Product> response = new ApiResponse<>(true, "Product saved successfully!!!", saveProduct);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<Product> response = new ApiResponse<>(false, "Product Not Saved!!!", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // PUT: Update a product by ID
    // URL: http://localhost:8080/api/v1/product
    @PutMapping(value = "/product/{pid}")
    public ResponseEntity<ApiResponse<?>> updateProduct(@RequestBody Product updateProduct, @PathVariable("pid") Integer id) {
        try {
            Product updatedProduct = productService.updateProductById(updateProduct, id);
            if (updatedProduct != null) {
                ApiResponse<Product> response = new ApiResponse<>(true, "Product updated successfully!!!", updatedProduct);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            ApiResponse<Product> response = new ApiResponse<>(false, "Product Not Found!!!", null);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<Product> response = new ApiResponse<>(false, "An error occurred internal server error!!!", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // DELETE: Delete a Product by ID
    @DeleteMapping("/product/{pid}")
    public ResponseEntity<ApiResponse<?>> deleteProductById(@PathVariable("pid") Integer id) {
        try {
            Product productById = productService.getProductById(id);
            if (productById == null) {
                ApiResponse<Object> response = new ApiResponse<>(false, "Product Not Found!!!", null);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            productService.deleteProductById(id);
            ApiResponse<Object> response = new ApiResponse<>(true, "Product Delete Successfully!!!", null);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<Product> response = new ApiResponse<>(false, "An error occurred internal server error", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}
