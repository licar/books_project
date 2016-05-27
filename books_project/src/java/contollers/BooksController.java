/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contollers;

import beans.Book;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.event.ValueChangeEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;


/**
 *
 * @author user
 */
@ManagedBean(name = "booksController", eager = true)
@SessionScoped
public class BooksController implements Serializable {
    
    private MySql database = MySql.GetInstance();
    private Book newBook = new Book();
    
    private ArrayList<Book> books = new ArrayList<Book>();
    private ArrayList<Integer> pageNumbers = new ArrayList<Integer>();
    
    private ConditionsShow conditionShow = ConditionsShow.ALL;
    private Integer numberBooksPerPage = 3;
    private Integer currentPageNumber = 0;
    private Integer currentGenreId = 0;
    private Integer numberBooks = 0;
    private Integer genreId = 0;
    private String searchLineValue = new String();
    private ShowMode showMode = ShowMode.DISPLAY;
    
    
    public void openPage(){
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        currentPageNumber = Integer.valueOf(params.get("no_page")) - 1;
        fillBooks();
    }
  
    public void fillBooks(){
        books = null;
        switch (conditionShow){
            case ALL:
                books = database.getAllBooks(numberBooksPerPage, currentPageNumber);
                break;
            case GENRE:
                books = database.getBooksByGenreId(genreId, numberBooksPerPage, currentPageNumber);
                break;
            case TITLE:
                books = database.getBooksByTitle(searchLineValue, numberBooksPerPage, currentPageNumber);
                break;
            case AUTHOR:
                books = database.getBooksByAuthor(searchLineValue, numberBooksPerPage, currentPageNumber);
                break;
            case PUBLISHER:
                books = database.getBooksByPublisher(searchLineValue, numberBooksPerPage, currentPageNumber);
                break;
            case ISBN:
                books = database.getBooksByIsbn(searchLineValue, numberBooksPerPage, currentPageNumber);
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
    
    public Integer getCurrentPageNumber(){
        return this.currentPageNumber;
    }
    
    private void setNumberBooks(){
        numberBooks = 0;
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
        currentPageNumber = 0;
        fillBooks();
        setNumberBooks();
        setPageNumbers();
    }
    
    public ArrayList<Integer> getPageNumbers(){
        if (pageNumbers.isEmpty()){
            setAttributes();
        }
        return pageNumbers;
    }
    
    public void setPageNumbers(){ 
        pageNumbers = fillPageNumbers(numberBooks, numberBooksPerPage);
    }
    
    public ArrayList<Integer> fillPageNumbers(Integer numberBooks, Integer numberBooksOnPage){

        ArrayList<Integer> pageNumbers = new ArrayList<Integer>();
        Integer numberPages = numberBooks / numberBooksOnPage;
        Integer balance = numberBooks % numberBooksOnPage;
        
        numberPages = (balance == 0) ? numberPages : (numberPages + 1); 
        
        for (Integer pageNumber = 1; pageNumber <= numberPages ; ++pageNumber){
            pageNumbers.add(pageNumber);
        }
       
        return pageNumbers;
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
        setAttributes();
        return created;
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
    
    public void setNumberBooks(FileUploadEvent event) {
        newBook.setFile(event.getFile().getContents());    
    }
    
    public byte[] getImage(Integer id){
        return database.getImage(id);
    }
    
    public byte[] getFile(Integer id){
        return database.getFile(id);
    }
}   
