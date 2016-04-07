/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beans;

import java.awt.Image;

/**
 *
 * @author user
 */

public class Book {
    private Integer id;
    private String title;
    private String description;
    private String author;
    private String publisher;
    private Integer year;
    private String isbn;
    private Image image;
    private byte[] file;  
    private boolean edited = false;
    
    public Book() {
    }
    
    public Book(Integer id, String title, String description, String author, String publisher, Integer year, String isbn, Image image, byte[] file) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.author = author;
        this.publisher = publisher;
        this.year = year;
        this.isbn = isbn;
        this.image = image;
        this.file = file;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public byte[] getFile() {
        return file;
    }

    public Integer getId() {
        return id;
    }

    public Image getImage() {
        return image;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getTitle() {
        return title;
    }

    public Integer getYear() {
        return year;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
    
    public void setEdited(){
        edited = true;
    }
    
    public void setUnedited(){
        edited = false;
    }
    
    public boolean isEdited(){
        return edited == true;
    }
}
