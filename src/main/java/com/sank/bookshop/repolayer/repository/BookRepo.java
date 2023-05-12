package com.sank.bookshop.repolayer.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sank.bookshop.repolayer.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class BookRepo {
    @Autowired
    private ResourceLoader resourceLoader;

    public List<Book> findAll() {
        ObjectMapper mapper = new ObjectMapper();
        Resource resource = resourceLoader.getResource("classpath:bookFeed.json");
        try (FileReader reader = new FileReader(resource.getFile())) {
            return mapper.readValue(reader, new TypeReference<List<Book>>() {
            });
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }
}
