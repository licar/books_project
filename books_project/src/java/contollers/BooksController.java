/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contollers;

import beans.Book;
import beans.Genre;
import com.google.common.io.ByteStreams;
import database.MySql;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import enums.enums.ConditionsShow;
import enums.enums.ShowMode;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.Part;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


/**
 *
 * @author user
 */
@ManagedBean
@SessionScoped 
public class BooksController implements Serializable {
    
    private MySql database = new MySql();
    private Book newBook = new Book();
    
    private ArrayList<Book> books = new ArrayList<Book>();
    private ArrayList<Integer> noPages = new ArrayList<Integer>();
    
    private ConditionsShow conditionShow = ConditionsShow.ALL;
    private Integer numberBooksOnPage = 3;
    private Integer noCurPage = 0;
    private Integer curGenreId = 0;
    private Integer numberBooks = 0;
    private Integer genreId = 0;
    private String searchLineValue = new String();
    private ShowMode showMode = ShowMode.DISPLAY;
    
    
    public void openPageNo(){
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        noCurPage = Integer.valueOf(params.get("no_page")) - 1;
        fillBooks();
    }
  
    public void fillBooks(){
        switch (conditionShow){
            case ALL:
                books = database.getAllBooks(numberBooksOnPage, noCurPage);
                break;
            case GENRE:
                books = database.getBooksByGenreId(genreId, numberBooksOnPage, noCurPage);
                break;
            case TITLE:
                books = database.getBooksByTitle(searchLineValue, numberBooksOnPage, noCurPage);
                break;
            case AUTHOR:
                books = database.getBooksByAuthor(searchLineValue, numberBooksOnPage, noCurPage);
                break;
            case PUBLISHER:
                books = database.getBooksByPublisher(searchLineValue, numberBooksOnPage, noCurPage);
                break;
            case ISBN:
                books = database.getBooksByIsbn(searchLineValue, numberBooksOnPage, noCurPage);
                break;    
        }
    }
    
    public void showAllBooks(){
        conditionShow = conditionShow.ALL;
        setAttributes();
    }
    
    public void showBooksByGenre()
    {
        conditionShow = conditionShow.GENRE;
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        genreId = Integer.valueOf(params.get("genre_id"));
        setAttributes();
    }
    
    public ArrayList<Book> getBooks(){
        if (books.isEmpty()){
            setAttributes();
        }
        return books;
    }

    public Integer getNumberBooks(){
        if (numberBooks == 0){
            setAttributes();
        }
        return numberBooks;
    }
    
    public Integer getNoCurPage(){
        return this.noCurPage;
    }
    
    private void setNumberBooks(){
        switch (conditionShow){
            case ALL:
                numberBooks = database.getNumberAllBooks();
                break;
            case GENRE:
                numberBooks = database.getNumberBooksByGenreId(genreId);
                break;
            case TITLE:
                numberBooks = database.getNumberBooksByTitle(searchLineValue);
                break;
            case AUTHOR:
                numberBooks = database.getNumberBooksByAuthor(searchLineValue);
                break;
            case PUBLISHER:
                numberBooks = database.getNumberBooksByPublisher(searchLineValue);
                break;
            case ISBN:
                numberBooks = database.getNumberBooksByIsbn(searchLineValue);
                break;
        }
    }
    
    public void setAttributes(){
        noCurPage = 0;
        fillBooks();
        setNumberBooks();
        setNoPages();
    }
    
    public ArrayList<Integer> getNoPages(){
        if (noPages.isEmpty()){
            setAttributes();
        }
        return noPages;
    }
    
    public void setNoPages(){ 
        noPages = fillNoPages(numberBooks, numberBooksOnPage);
    }
    
    public ArrayList<Integer> fillNoPages(Integer numberBooks, Integer numberBooksOnPage){

        ArrayList<Integer> noPages = new ArrayList<Integer>();
        Integer numberPages = numberBooks / numberBooksOnPage;
        
        for (Integer noPage = 1; noPage <= numberPages + 1; ++noPage){
            noPages.add(noPage);
        }
       
        return noPages;
    }
    
    public boolean deleteBook(){
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Integer bookId = Integer.valueOf(params.get("book_id"));
        if (database.deleteBookById(bookId)){
            setAttributes();
            return true;
        }
        return false;
    }
    
    public boolean updateBooks(){
        setModeDisplay();
        boolean updated = database.updateBooks(books);
        return updated;
    }
    
            
    public void cancelUpdate(){
        setModeDisplay();
        fillBooks();
    }
    
    public void setModeDisplay(){
        this.showMode = ShowMode.DISPLAY;
    }
    
    public void setModeEdit(){
        this.showMode = ShowMode.EDIT;
    }
    
    public boolean isModeDisplay(){
        return showMode == ShowMode.DISPLAY;
    }
    
    public boolean isModeEdit(){
        return showMode == ShowMode.EDIT;
    }
    
    public boolean isModeCreate(){
        return showMode == ShowMode.CREATE;
    }
    
    public void setModeCreate(){
        showMode = ShowMode.CREATE;
    }
    
    public Book getNewBook(){        
        return newBook;
    }
  
    public boolean createBook(){
        setModeDisplay();
        boolean created = database.createBook(newBook);
        if (created){
            books.add(newBook);
        }
        newBook = new Book();
        return created;
    }
    
    public boolean saveImages(){
        return false;
    }
    
    public void setSelectedGenreId(ValueChangeEvent event) {
        newBook.setGenreId(Integer.parseInt(event.getNewValue().toString()));
    }
    
    public void setNewBookImage(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        InputStream is = null;
        try {
            is = uploadedFile.getInputstream();
            newBook.setImage(ByteStreams.toByteArray(is));
        } catch (IOException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            if (is != null){
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    public void setNewBookFile(FileUploadEvent event) {
        newBook.setFile(event.getFile().getContents());    
    }
    
    public void setSearchCriteria(ValueChangeEvent event) {
        String searchCriteria = event.getNewValue().toString();
        switch (searchCriteria){
            case "title":
                conditionShow = ConditionsShow.TITLE;
                break;
            case "publisher":
                conditionShow = ConditionsShow.PUBLISHER;
                break;
            case "author":
                conditionShow = ConditionsShow.AUTHOR;
                break;
            case "isbn":
                conditionShow = ConditionsShow.ISBN;
                break;
        }
    }
    
    public void setSearchLineValue(String searchLineValue){
        this.searchLineValue = searchLineValue;
    }
    
    public String getSearchLineValue(){
        return this.searchLineValue;
    }
}   
