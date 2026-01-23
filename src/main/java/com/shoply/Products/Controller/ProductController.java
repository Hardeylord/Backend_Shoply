package com.shoply.Products.Controller;

import com.shoply.Products.Model.Product;
import com.shoply.Products.interfaces.LatestProductInterface;
import com.shoply.Products.repository.ProductRepository;
import com.shoply.Products.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class ProductController {

    @Autowired
    ProductServices services;

    @GetMapping("/")
    public ResponseEntity<Page<Product>> fetchProducts(@RequestParam(defaultValue = "1") int pageNo,
                                                       @RequestParam(defaultValue = "10") int pageSize){
        Pageable pageable = PageRequest.of(pageNo-1, pageSize);
        return ResponseEntity.ok(services.fetchProd(pageable));
    }

//    @GetMapping("/sortedproducts")
//    public ResponseEntity<List<Product>> fetchProductsSorted(@RequestParam String field){
//
//        return services.fetchProdSorted(field);
//    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @GetMapping("/allproducts")
    public ResponseEntity<Page<Product>> fetchAllProducts(@RequestParam(defaultValue = "1") int pageNo,
                                                          @RequestParam(defaultValue = "10") int pageSize,
                                                          @RequestParam(required = false) String sortBy){
        return ResponseEntity.ok(services.fetchProdSorted(pageNo, pageSize, sortBy));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @PostMapping("/addproduct")
    public void addProduct(@RequestParam("id") String id,
                           @RequestParam("prodId") int prodId,
                           @RequestParam("name")  String name,
                           @RequestParam("desc") String desc,
                           @RequestParam("price") Double price,
                           @RequestParam("rating") Double rating,
                           @RequestParam("negotiable") boolean negotiable,
                           @RequestParam("image") List<MultipartFile> image ) throws IOException {
        services.addProd(id, prodId, name, desc, price, rating,negotiable, image);
    }

    @GetMapping("/latestproducts")
    public List<Product> latestProducts() {
      return services.latestProducts();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{name}")
    public void deleteProduct(@PathVariable String name,
                              @RequestParam(value = "ids" , required = false) List<String> imageId) throws IOException {
        services.delProduct(name, imageId);
    }

    @GetMapping("/product/{name}")
    public ResponseEntity<?> getProductByName(@PathVariable String name){
        return services.getProdByName(name);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'EDITOR')")
    @PutMapping("/product/update/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") String id,
                              @RequestParam("name")  String name,
                              @RequestParam("desc") String desc,
                              @RequestParam("price") Double price,
                              @RequestParam("rating") Double rating,
                              @RequestParam(value = "imageId", required = false) List<String> imageId,
                              @RequestParam(value = "image", required = false) List<MultipartFile> image ) throws IOException {

        return services.updateProd(id, name, desc, price, rating, imageId, image);
    }

//    search products
    @GetMapping("/search/{productName}")
    public List<Product> searchProduct(@PathVariable String productName) {
        return services.searchProducts(productName);
    }

}
