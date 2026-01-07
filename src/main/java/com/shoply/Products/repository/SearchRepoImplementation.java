package com.shoply.Products.repository;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.shoply.Products.Model.Product;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class SearchRepoImplementation implements SearchRepository{

    @Autowired
    MongoClient mongoClient;
    @Autowired
    MongoConverter mongoConverter;

    @Override
    public List<Product> searchProducts(String productName) {

        List<Product> searchResult = new ArrayList<>();

        MongoDatabase database = mongoClient.getDatabase("productlist");
        MongoCollection<Document> collection = database.getCollection("products");
        AggregateIterable<Document> result = collection.aggregate(Arrays.asList(new Document("$search",
                        new Document("index", "productSearch")
                                .append("text",
                                        new Document("query", productName)
                                                .append("path", "name")
                                                .append("matchCriteria", "any"))),
                new Document("$sort",
                        new Document("price", -1L))));
        result.forEach(document -> searchResult.add(mongoConverter.read(Product.class, document)));
        return searchResult;
    }
}
