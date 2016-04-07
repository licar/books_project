/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contollers;

import beans.Book;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import static database.MySql.DATA_SOURCE;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.swing.ImageIcon;


/**
 *
 * @author user
 */
@SessionScoped 
@ManagedBean
public class BooksController implements Serializable {
    
    public enum ConditionsShow { ALL, GENRE, AUTHOR, TITLE, PUBLISHER};
    private ArrayList<Book> books = new ArrayList<Book>();
    private ArrayList<Integer> noPages = new ArrayList<Integer>();
    
    private ConditionsShow conditionShow = ConditionsShow.ALL;
    private Integer numberBooksOnPage = 4;
    private Integer noCurPage = 0;
    private Integer curGenreId = 0;
    private Integer numberBooks = 0;
    private Integer genreId = 0;
    
    public void openPageNo(){
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        noCurPage = Integer.valueOf(params.get("no_page")) - 1;
        fillBooks();
    }
  
    private void fillBooks(){
        switch (conditionShow){
            case ALL:
                fillAllBooksRequest();
                break;
            case GENRE:
                fillBooksByGenreIdRequest(genreId);
                break;
            case TITLE:
                break;
            case AUTHOR:
                break;
            case PUBLISHER:
                break;
        }
    }
    
    private void fillAllBooksRequest()
    {
        Connection conn = null;
        DataSource dataSource = null;
        Statement statment = null;
        ResultSet resultSet = null;
        ArrayList<Book> tempBooks = new ArrayList();
        try{
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup(DATA_SOURCE);
            
            conn = dataSource.getConnection();
            statment = conn.createStatement();
            
            resultSet = statment.executeQuery(
                    "SELECT * FROM book, genre" +
                    " WHERE book.genre_id = genre.genre_id" +
                    " LIMIT " + (numberBooksOnPage * noCurPage) + "," +
                    numberBooksOnPage);
            
            
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getInt("book_id"));
                book.setTitle(resultSet.getString("book_title"));
                book.setDescription(resultSet.getString("book_description"));
                book.setIsbn(resultSet.getString("book_isbn"));
                book.setAuthor(resultSet.getString("author"));
                book.setPublisher(resultSet.getString("publisher"));
                book.setYear(resultSet.getInt("book_year"));
                book.setFile(resultSet.getBytes("book_file"));
                book.setImage(new ImageIcon(resultSet.getBytes("book_image")).getImage());
                tempBooks.add(book);
            }
            
        } catch (NamingException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if (statment != null){
                    statment.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
                if (conn != null){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GenresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        books = tempBooks;
    }
    private void fillBooksByGenreIdRequest(Integer genreId)
    {
        Connection conn = null;
        DataSource dataSource = null;
        Statement statment = null;
        ResultSet resultSet = null;
        ArrayList<Book> tempBooks = new ArrayList<>();
        try{
            
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup(DATA_SOURCE);
            conn = dataSource.getConnection();
            statment = conn.createStatement();
            
            resultSet = statment.executeQuery(
                        " SELECT * FROM book, genre" +
                        " WHERE book.genre_id = genre.genre_id" +
                        " AND book.genre_id = " + genreId + 
                        " LIMIT " + (numberBooksOnPage * noCurPage) + "," +
                             numberBooksOnPage);
            
            while (resultSet.next()) {
                Book book = new Book();
                Integer id = resultSet.getInt("book_id");
                book.setId(resultSet.getInt("book_id"));
                book.setTitle(resultSet.getString("book_title"));
                book.setDescription(resultSet.getString("book_description"));
                book.setIsbn(resultSet.getString("book_isbn"));
                book.setAuthor(resultSet.getString("author"));
                book.setPublisher(resultSet.getString("publisher"));
                book.setYear(resultSet.getInt("book_year"));
                book.setFile(resultSet.getBytes("book_file"));
                book.setImage(new ImageIcon(resultSet.getBytes("book_image")).getImage());
                tempBooks.add(book);
            }
            
        } catch (NamingException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if (statment != null){
                    statment.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
                if (conn != null){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GenresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        books = tempBooks;
    }
       
    public void openAllBooks(){
        conditionShow = conditionShow.ALL;
        setAttributes();
    }
    
    public void openBooksByGenre()
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
    
    public ArrayList<Integer> getNoPages(){
        if (noPages.isEmpty()){
            setAttributes();
        }
        return noPages;
    }
    
    private void setNumberBooks(){
        switch (conditionShow){
            case ALL:
                setNumberAllBooksRequest();
                break;
            case GENRE:
                setNumberBooksByGenreIdRequest(genreId);
                break;
            case TITLE:
                break;
            case AUTHOR:
                break;
            case PUBLISHER:
                break;
        }
    }
    
    private void setAttributes(){
        noCurPage = 0;
        fillBooks();
        setNumberBooks();
        setNoPages();
    }
    
    public void setNumberAllBooksRequest()
    {
        Connection conn = null;
        DataSource dataSource = null;
        Statement statment = null;
        ResultSet resultSet = null;
        Integer tempNumberBooks = 0;
        try{
            numberBooks = 0;
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup(DATA_SOURCE);
            conn = dataSource.getConnection();
            statment = conn.createStatement();
            resultSet = statment.executeQuery(
                    "SELECT COUNT(*) AS number_pages FROM book");
            
            resultSet.last();
            tempNumberBooks = resultSet.getInt("number_pages");
            
        } catch (NamingException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if (statment != null){
                    statment.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
                if (conn != null){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GenresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        numberBooks = tempNumberBooks;
    }   
    private void setNumberBooksByGenreIdRequest(Integer genreId)
    {
        Connection conn = null;
        DataSource dataSource = null;
        Statement statment = null;
        ResultSet resultSet = null;
        Integer tempNumberBooks = 0;
        try{
            numberBooks = 0;
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup(DATA_SOURCE);
            conn = dataSource.getConnection();
            statment = conn.createStatement();
           
            resultSet = statment.executeQuery(
                    "SELECT COUNT(*) AS number_pages FROM book " +
                    "WHERE book.genre_id = " + genreId);
            
            resultSet.last();
            tempNumberBooks = resultSet.getInt("number_pages");
            
        } catch (NamingException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if (statment != null){
                    statment.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
                if (conn != null){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GenresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        numberBooks = tempNumberBooks;
    }
    
    public void setNoPages(){

        ArrayList<Integer> tempNoPages = new ArrayList<Integer>();
        Integer numberPages = numberBooks / numberBooksOnPage;
        
        for (Integer noPage = 1; noPage <= numberPages; ++noPage){
            tempNoPages.add(noPage);
        }
        
        if (numberBooks < numberBooksOnPage){
            tempNoPages.add(1);
        }
        noPages = tempNoPages;
    }
    
    private void deleteBookByIdRequest(Integer bookId){
        Connection conn = null;
        DataSource dataSource = null;
        Statement statment = null;
        ResultSet resultSet = null;
        try{
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup(DATA_SOURCE);
            conn = dataSource.getConnection();
            statment = conn.createStatement();
            
            statment.executeUpdate(
                    "DELETE FROM book " +
                    "WHERE book.book_id = " + bookId);
        
        } catch (NamingException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if (statment != null){
                    statment.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
                if (conn != null){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GenresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
   
    public void deleteBook(){
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Integer bookId = 0;
        bookId = Integer.valueOf(params.get("book_id"));
        deleteBookByIdRequest(bookId);
        setAttributes();
    }
    
    private void updateBookRequest(){
        Connection conn = null;
        DataSource dataSource = null;
        Statement statment = null;
        ResultSet resultSet = null;
        ArrayList<Book> tempBooks = new ArrayList<>();
        try{
            
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup(DATA_SOURCE);
            conn = dataSource.getConnection();
            statment = conn.createStatement();
            
            resultSet = statment.executeQuery(
                        " SELECT * FROM book, genre" +
                        " WHERE book.genre_id = genre.genre_id" +
                        " AND book.genre_id = " + genreId + 
                        " LIMIT " + (numberBooksOnPage * noCurPage) + "," +
                             numberBooksOnPage);
            
            while (resultSet.next()) {
                Book book = new Book();
                Integer id = resultSet.getInt("book_id");
                book.setId(resultSet.getInt("book_id"));
                book.setTitle(resultSet.getString("book_title"));
                book.setDescription(resultSet.getString("book_description"));
                book.setIsbn(resultSet.getString("book_isbn"));
                book.setAuthor(resultSet.getString("author"));
                book.setPublisher(resultSet.getString("publisher"));
                book.setYear(resultSet.getInt("book_year"));
                book.setFile(resultSet.getBytes("book_file"));
                book.setImage(new ImageIcon(resultSet.getBytes("book_image")).getImage());
                tempBooks.add(book);
            }
            
        } catch (NamingException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if (statment != null){
                    statment.close();
                }
                if (resultSet != null){
                    resultSet.close();
                }
                if (conn != null){
                    conn.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(GenresController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        books = tempBooks;
    }
    
    public void updateBook(){
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        Integer bookId = 0;
        bookId = Integer.valueOf(params.get("book_id"));
        //ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        //String title = ec.getRequestParameterMap().get("ada" + ":title");
        int i = 0;
        
    }

}