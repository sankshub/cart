package com.sank.bookshop.services.service;

import com.sank.bookshop.repos.entity.Author;
import com.sank.bookshop.repos.entity.Book;
import com.sank.bookshop.repos.repository.BookRepo;
import com.sank.bookshop.services.exceptions.BookNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {
    private static final String BOOK_NOT_FOUND_ERROR = "Requested book not found/ISBN is null, Please try with valid ISBN ";
    @Mock
    BookRepo bookRepo;
    Book book = new Book();
    @InjectMocks
    BookServiceImpl bookService;

    @Before
    public void setUp() {
        book.setIsbn("123456789");
        book.setTitle("Clean Code");
        Author author = new Author();
        author.setFirstName("Robert");
        author.setMiddleName(null);
        author.setLastName("Martin");
        book.setAuthor(author);
        book.setYearOfPublish("2008");
        book.setPrice("50");
    }

    @Test
    public void findAll() {
        when(bookRepo.findAll()).thenReturn(Collections.singletonList(book));
        Assert.assertTrue(bookService.findAll()
                                     .contains(book));
    }

    @Test
    public void findByIsbn() {
        when(bookRepo.findAll()).thenReturn(Collections.singletonList(book));
        Assert.assertEquals(bookService.findByIsbn("123456789"), book);
    }

    @Test
    public void findBookWithUnknownIsbn() {
        when(bookRepo.findAll()).thenReturn(Collections.emptyList());
        Exception exception = Assert.assertThrows(BookNotFoundException.class, () -> {
            bookService.findByIsbn("123456789");
        });
        Assert.assertEquals(BOOK_NOT_FOUND_ERROR, exception.getMessage());
    }
}