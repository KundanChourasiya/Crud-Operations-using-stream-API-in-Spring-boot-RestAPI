package com.demo.Service;

import com.demo.Entity.Product;

import java.util.List;

public interface ProductService {

    // GET: Retrieve all Product
    public List<Product> getAllProduct();

    // GET: Retrieve a single Product by ID
    public Product getProductById(Integer id);

    // POST: Save a new Product
    public Product saveProduct(Product product);

    // PUT: Update a product by ID
    public Product updateProductById(Product updatedProduct, Integer id);

    // DELETE: Delete a Product by ID
    public void deleteProductById(Integer id);
}
