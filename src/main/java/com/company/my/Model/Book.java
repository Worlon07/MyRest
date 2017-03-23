package com.company.my.Model;

import java.util.List;

/**
 * The book class
 */
public class Book {
    private int id;
    private String title;
    private List<Author> authors;

    public Book(final int id, final String title, final List<Author> authors){
        this.id = id;
        this.title = title;
        this.authors = authors;
    }

    /**
     * The book unique Id.
     *
     * @return book id
     */
    public int getId() {
        return id;
    }

    /**
     * Set unique Id of the book.
     *
     * @param id unique Id
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * The book title.
     *
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set title of the book.
     *
     * @param title title of the book
     */
    public void setTitle(final String title) {
        this.title = title;
    }

    /**
     * List of the book authors.
     *
     * @return author list
     */
    public List<Author> getAuthors() {
        return authors;
    }

    /**
     * Set author list of the book.
     *
     * @param authors author list
     */
    public void setAuthors(final List<Author> authors) {
        this.authors = authors;
    }
}
