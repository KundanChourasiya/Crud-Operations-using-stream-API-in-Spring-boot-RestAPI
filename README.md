# Crud-Operations-using-stream-API-in-Spring-boot-RestAPI.

> [!NOTE]
> ### In this Api we Create Crud operations using stream api in spring boot RestApi.

## Tech Stack
- Java-17
- Spring Boot-3x
- lombok
- Hoppscotch
- Swagger UI

## Modules
* Product Module

## Documentation
Swagger UI Documentation - http://localhost:8080/swagger-ui/

## API Root Endpoint
```
https://localhost:8080/
http://localhost:8080/swagger-ui/
user this data for checking purpose.
```
## Step To Be Followed
> 1. Create Rest Api will return to Product Details.
>    
>    **Project Documentation**
>    - **Entity** - Product (class)
>    - **Payload** - ApiResponceDto (class)
>    - **Service** - ProductService (interface), ProductServiceImpl (class)
>    - **Controller** - ProductController (Class)
>      
> 2. we create api for crud operation.

## Important Dependency to be used
```xml
 <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-web</artifactId>
 </dependency>

 <dependency>
     <groupId>org.projectlombok</groupId>
     <artifactId>lombok</artifactId>
     <optional>true</optional>
 </dependency>

<dependency>
	<groupId>org.springdoc</groupId>
	<artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
	<version>2.3.0</version>
</dependency>
```

## Create Product class in Entity Package.
```java
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    private Integer id;
    private String category;
    private String name;
    private Integer quantity;
    private double price;

}
```

## Create ProductService interface and ProductServiceImpl class in Service package.

### *ProductService*
```java
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
```

### *ProductServiceImpl*
```java
@Service
public class ProductServiceImpl implements ProductService {

    private static ArrayList<Product> productList = new ArrayList<>();
    private static int lastId = 100; // starting from the highest ID already used

    static {
        // static block to store the data 
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
```

##  Create ApiResponse inside the Payload Package.
### *ApiResponseDto* 
```java
@Setter
@Getter
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean status;
    private String message;
    private T data;
    public ApiResponse(boolean status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
```

### *Create ProductController class inside the Controller Package.* 

```java
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
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            e.printStackTrace();
            ApiResponse<Product> response = new ApiResponse<>(false, "An error occurred internal server error", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}

```

### Following pictures will help to understand flow of API

### *Swagger*

![image](https://github.com/user-attachments/assets/60ad3798-36e9-40d5-878a-f94160f1e250)

### *Hoppscotch Test Cases*

Url - http://localhost:8080/api/v1/product
![image](https://github.com/user-attachments/assets/ac60d3b3-755c-467b-bcec-67014f631b16)

Url - http://localhost:8080/api/v1/product
![image](https://github.com/user-attachments/assets/338855e5-4dff-40e6-8a95-c5c098cd970d)

Url - http://localhost:8080/api/v1/product/101
![image](https://github.com/user-attachments/assets/f1788155-42d6-46b2-a31c-d8a51955d7c3)

Url - http://localhost:8080/api/v1/product/101
![image](https://github.com/user-attachments/assets/2d5848e6-f8b3-454d-805f-264f6c602b88)

Url - http://localhost:8080/api/v1/product/102
![image](https://github.com/user-attachments/assets/ae97d4a9-b244-4339-be10-e11f9a0be122)
