/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;
 
import beans.Book;
import contollers.BooksController;
import contollers.GenresController;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
public class MySql{   
    public static String DATA_SOURCE = "jdbc/library";   
    
    public ArrayList<Book> getAllBooks(Integer numberBooksOnPage, Integer noCurPage)
    {
        Connection conn = null;
        DataSource dataSource = null;
        Statement statment = null;
        ResultSet resultSet = null;
        ArrayList<Book> books = new ArrayList<Book>();
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
            
            
            for(Integer i = 0; resultSet.next(); ++i) {
                Book book = new Book();
                book.setId(resultSet.getInt("book_id"));
                book.setTitle(resultSet.getString("book_title"));
                book.setGenreId(resultSet.getInt("genre_id"));
                book.setDescription(resultSet.getString("book_description"));
                book.setIsbn(resultSet.getString("book_isbn"));
                book.setAuthor(resultSet.getString("book_author"));
                book.setPublisher(resultSet.getString("book_publisher"));
                book.setYear(resultSet.getInt("book_year"));
                book.setFile(resultSet.getBytes("book_file"));
                book.setImage(resultSet.getBytes("book_image"));
                book.setImageName(i.toString()  + ".jpg");
                books.add(book);
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
        return books;
    }
    
    public ArrayList<Book> getBooksByGenreId(Integer genreId, Integer numberBooksOnPage, Integer noCurPage)
    {
        Connection conn = null;
        DataSource dataSource = null;
        Statement statment = null;
        ResultSet resultSet = null;
        ArrayList<Book> books = new ArrayList<Book>();
        
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
            
            
            for(Integer j = 0; resultSet.next(); ++j) {
                Book book = new Book();
                book.setId(resultSet.getInt("book_id"));
                book.setTitle(resultSet.getString("book_title"));
                book.setDescription(resultSet.getString("book_description"));
                book.setIsbn(resultSet.getString("book_isbn"));
                book.setGenreId(resultSet.getInt("genre_id"));
                book.setAuthor(resultSet.getString("book_author"));
                book.setPublisher(resultSet.getString("book_publisher"));
                book.setYear(resultSet.getInt("book_year"));
                book.setFile(resultSet.getBytes("book_file"));
                book.setImage(resultSet.getBytes("book_image"));
                book.setImageName(j.toString()  + ".jpg");
                books.add(book);
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
        return books;
    }
    
    public Integer getNumberAllBooks()
    {
        Connection conn = null;
        DataSource dataSource = null;
        Statement statment = null;
        ResultSet resultSet = null;
        Integer numberBooks = 0;
        try{
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup(DATA_SOURCE);
            conn = dataSource.getConnection();
            statment = conn.createStatement();
            resultSet = statment.executeQuery(
                    "SELECT COUNT(*) AS number_pages FROM book");
            
            resultSet.last();
            numberBooks = resultSet.getInt("number_pages");
            
        } catch (NamingException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
            numberBooks = -1;
        } catch (SQLException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
            numberBooks = -1;
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
        return numberBooks;
    }
    
    public Integer getNumberBooksByGenreId(Integer genreId)
    {
        Connection conn = null;
        DataSource dataSource = null;
        Statement statment = null;
        ResultSet resultSet = null;
        Integer numberBooks = 0;
        try{
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup(DATA_SOURCE);
            conn = dataSource.getConnection();
            statment = conn.createStatement();
           
            resultSet = statment.executeQuery(
                    "SELECT COUNT(*) AS number_pages FROM book " +
                    "WHERE book.genre_id = " + genreId);
            
            resultSet.last();
            numberBooks = resultSet.getInt("number_pages");
            
        } catch (NamingException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
            numberBooks = -1;
        } catch (SQLException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
            numberBooks = -1;
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
        return numberBooks;
    }
    
    public boolean deleteBookById(Integer bookId){
        Connection conn = null;
        DataSource dataSource = null;
        Statement statment = null;
        ResultSet resultSet = null;
        boolean deleted = true;
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
            deleted = false;
        } catch (SQLException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
            deleted = false;
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
        return deleted;
    }
    
    public boolean updateBooks(ArrayList<Book> books){
        Connection conn = null;
        DataSource dataSource = null;
        PreparedStatement preparedStatment = null;
        ResultSet resultSet = null;
        boolean updated = true;
        
        try{
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup(DATA_SOURCE);
            conn = dataSource.getConnection();
            preparedStatment = conn.prepareStatement(
                    "UPDATE book" +
                    " SET book_title = ?," +
                    " book_description = ?," +
                    " book_author = ?," +
                    " book_publisher = ?," +
                    " book_year = ?," +
                    " book_isbn = ?" +
                    " WHERE book_id = ?"
                );
            
            
            for (Book book : books){
                if (book.isEdit()){
                    preparedStatment.setString(1, book.getTitle());
                    preparedStatment.setString(2, book.getDescription());
                    preparedStatment.setString(3, book.getAuthor());
                    preparedStatment.setString(4, book.getPublisher());
                    preparedStatment.setInt(5, book.getYear());
                    preparedStatment.setString(6, book.getIsbn());
                    preparedStatment.setString(7, book.getId().toString());
                    preparedStatment.executeUpdate();
                }
            }
            

        } catch (NamingException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
            updated = false;
        } catch (SQLException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
            updated = false;
        }finally{
            try {
                if (preparedStatment != null){
                    preparedStatment.close();
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
        return updated;
    }
    
    public boolean createBook(Book book){
        Connection conn = null;
        DataSource dataSource = null;
        PreparedStatement preparedStatment = null;
        ResultSet resultSet = null;
        boolean hadException = false;
        try{
            
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup(DATA_SOURCE);
            conn = dataSource.getConnection();
            preparedStatment = conn.prepareStatement(
                    "INSERT INTO book (book_title, book_description, book_year, book_isbn, book_image, book_file, book_author, genre_id, book_publisher)" +
                    " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)"
                );

        preparedStatment.setString(1, book.getTitle());
        preparedStatment.setString(2, book.getDescription());
        preparedStatment.setInt(3, book.getYear());
        preparedStatment.setString(4, book.getIsbn());
        preparedStatment.setBytes(5, book.getImage());
        preparedStatment.setBytes(6, book.getFile());
        preparedStatment.setString(7, book.getAuthor());
        preparedStatment.setInt(8, book.getGenreId());
        preparedStatment.setString(9, book.getPublisher());
        preparedStatment.executeUpdate();
        
        } catch (NamingException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
            hadException = true;
        } catch (SQLException ex) {
            Logger.getLogger(BooksController.class.getName()).log(Level.SEVERE, null, ex);
            hadException = true;
        }finally{
            try {
                if (preparedStatment != null){
                    preparedStatment.close();
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
        return !hadException;
    }
}
    
    
