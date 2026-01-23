package com.shoply.Products.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.shoply.Products.Error.ProductNotFoundError;
import com.shoply.Products.Model.Product;
import com.shoply.Products.interfaces.LatestProductInterface;
import com.shoply.Products.repository.ProductRepository;
import com.shoply.Products.repository.SearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ProductServices {

    @Autowired
    ProductRepository pRepo;
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    SearchRepository searchRepository;


//    fetching products

    @Cacheable(value = "products",
            key = "'page=' + #pageable.pageNumber + ',size=' + #pageable.pageSize +',sort=' + #pageable.sort.toString()"
    )
    public Page<Product> fetchProd(Pageable pageable) {
        System.out.println("FETCH FROM DB");
        return pRepo.findAll(pageable);
    }

    @Cacheable(value = "products", key = "'page=' + #pageNo + ',size=' + #pageSize + ',filter' + #field")
    public Page<Product> fetchProdSorted (int pageNo, int pageSize, String field) {
        List<String> sortBys= List.of("name", "price", "rating");

        if (field == null || !sortBys.contains(field)){
            Pageable pageable = PageRequest.of(pageNo-1,pageSize);
            return pRepo.findAll(pageable);
        } else {
            return pRepo.findAll(PageRequest.of(pageNo-1, pageSize, Sort.by(Sort.Direction.ASC,field)));
        }
    }

//    adding products

    @CacheEvict(value = "products", allEntries = true)
    public void addProd(String id, int prodId, String name, String desc, Double price, Double rating, boolean negotiable, List<MultipartFile> image) throws IOException {
        List <Map<String, String>> imageUrls = new java.util.ArrayList<>();

        for(MultipartFile images: image){
            Map uploadResult = cloudinary.uploader().upload(images.getBytes(),
                    ObjectUtils.asMap("folder", "products/" + name));
            Map<String, String> details = new HashMap<>();

            details.put("secure_url", uploadResult.get("secure_url").toString());
            details.put("public_id", uploadResult.get("public_id").toString());

            imageUrls.add(details);
        }


        Product prod=new Product();
        prod.setId(id);
        prod.setProdId(prodId);
        prod.setName(name);
        prod.setDesc(desc);
        prod.setPrice(price);
        prod.setRating(rating);
        prod.setNegotiable(negotiable);
        prod.setImage(imageUrls);

        pRepo.save(prod);
    }

//    deleting products
@CacheEvict(value = "products", allEntries = true)
    public void delProduct(String name, List<String> imageId) throws IOException {
        pRepo.deleteByName(name);
        for (String ids : imageId) {
            cloudinary.uploader().destroy(ids, ObjectUtils.emptyMap());
        }
    }

//    fetching particular Products
    public ResponseEntity<?> getProdByName(String name) {

            Product product=pRepo.findByName(name).
                    orElseThrow(()->new ProductNotFoundError(name+" Not Found"));
            return ResponseEntity.ok(product);

    }

//    updating products
    @CacheEvict(value = "products", allEntries = true)
    public ResponseEntity<?> updateProd(String id, String name, String desc, Double price, Double rating,List<String> imageId, List<MultipartFile> image) throws IOException {

        Optional<Product> mine= pRepo.findById(id);


        if (mine.isPresent()){
            Product change= mine.get();
            change.setName(name);
            change.setDesc(desc);
            change.setPrice(price);
            change.setRating(rating);
            List<Map<String, String>> currentImages = change.getImage();
            if (imageId != null) {
                for (String ids : imageId) {
                    cloudinary.uploader().destroy(ids, ObjectUtils.emptyMap());
                    List<Map<String, String>> unWantedImages = change.getImage();
                    unWantedImages.removeIf(detailsMap -> ids.equals(detailsMap.get("public_id")));
                }
            }

            if (image != null && !image.isEmpty()) {
            for(MultipartFile images: image){
                Map uploadResult = cloudinary.uploader().upload(images.getBytes(),
                        ObjectUtils.asMap("folder", "products/" + name));
                Map<String, String> details = new HashMap<>();

                details.put("secure_url", uploadResult.get("secure_url").toString());
                details.put("public_id", uploadResult.get("public_id").toString());

                currentImages.add(details);
                }
            }
            change.setImage(currentImages);
            pRepo.save(change);
//            System.out.println("Updated!!!");
            return new ResponseEntity<>(change, HttpStatus.OK);
        } else {
//            System.out.println("Product not found.");
            return new ResponseEntity<>(new ProductNotFoundError(""), HttpStatus.NOT_FOUND);
        }
    }

    public List<Product> searchProducts(String productName) {
        return searchRepository.searchProducts(productName);
    }

    public List<Product> latestProducts() {
        List<LatestProductInterface> allIds=pRepo.findAllBy();
        List<String> myStringList=allIds.stream().skip(Math.max(0, allIds.size()-3)).map(LatestProductInterface::getId).toList();
//        System.out.println(myStringList);
        return pRepo.findAllById(myStringList);
    }
}
