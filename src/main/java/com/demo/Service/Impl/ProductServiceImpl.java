package com.demo.Service.Impl;

import com.demo.Entity.Product;
import com.demo.Service.ProductService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static ArrayList<Product> productList = new ArrayList<>();
    private static int lastId = 100; // starting from the highest ID already used

    static {

    }

    @Override
    public List<Product> getAllProduct() {
        return productList;
    }

    @Override
    public Product getProductById(Integer id) {
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Product saveProduct(Product product) {
        product.setId(++lastId);
        productList.add(product);
        return product;
    }

    @Override
    public Product updateProductById(Product updatedProduct, Integer id) {
        productList = (ArrayList<Product>) productList.stream()
                .map(product -> {
                    if (product.getId().equals(id)) {
                        product.setName(updatedProduct.getName());
                        product.setCategory(updatedProduct.getCategory());
                        product.setQuantity(updatedProduct.getQuantity());
                        product.setPrice(updatedProduct.getPrice());
                    }
                    return product;
                })
                .collect(Collectors.toList());
        return getProductById(id);
    }

    @Override
    public void deleteProductById(Integer id) {
        productList = (ArrayList<Product>) productList.stream()
                .filter(p -> !p.getId().equals(id))
                .collect(Collectors.toList());
    }

}
