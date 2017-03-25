package com.company.my.Controller;

import com.company.my.Model.Author;
import com.company.my.Model.Book;
import com.company.my.rest.MyRequestMapping;
import com.company.my.rest.MyRequestMethod;
import com.company.my.rest.MyRestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Simple controller to demonstrate MyRest
 */
@MyRestController
@MyRequestMapping("/books")
public class BookRestController {
    private final Author author = new Author("Harry", "Harrison");
    private final List<Book> books = Arrays.asList(new Book(1, "Steel Rat", Collections.singletonList(author)),
            new Book(2, "Death World", Collections.singletonList(author)));

    @MyRequestMapping(method = MyRequestMethod.GET)
    Collection<Book> listOfBooks() {
        return books;
    }

    @MyRequestMapping(path = "/count", method = MyRequestMethod.GET)
    int getBooksCount() {
        return books.size();
    }

    @MyRequestMapping(path = "/{bookId}", method = MyRequestMethod.GET)
    Book getBook(final int bookId) {
        for (final Book book : books) {
            if (book.getId() == bookId) {
                return book;
            }
        }
        return null;
    }
}
