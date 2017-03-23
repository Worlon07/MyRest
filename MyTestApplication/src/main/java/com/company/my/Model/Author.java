package com.company.my.Model;

/**
 * Author of a book.
 */
public class Author {
    private String firstName;
    private String secondName;

    public Author(final String firstName, final String secondName) {
        this.firstName = firstName;
        this.secondName = secondName;
    }

    /**
     * The Author name.
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set Author name.
     *
     * @param firstName first name
     */
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    /**
     * The Author second name.
     *
     * @return second name
     */
    public String getSecondName() {
        return secondName;
    }

    /**
     * Set Author second name.
     *
     * @param secondName first name
     */
    public void setSecondName(final String secondName) {
        this.secondName = secondName;
    }
}
