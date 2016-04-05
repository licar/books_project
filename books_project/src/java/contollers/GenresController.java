/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contollers;

import beans.Genre;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import database.MySql;
import static database.MySql.DATA_SOURCE;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author user
 */

@ApplicationScoped
@ManagedBean
public class GenresController implements Serializable{
    private ArrayList<Genre> genres = new ArrayList<Genre>();
    
    public ArrayList<Genre> getGenres(){
        if (genres.isEmpty()){
            genres = getAllGenres();
        }
        return genres;
    }
    
    public static ArrayList<Genre> getAllGenres(){
        Connection conn = null;
        DataSource dataSource = null;
        Statement statment = null;
        ResultSet resultSet = null;
        ArrayList<Genre> genres = new ArrayList();
        try{
            InitialContext initialContext = new InitialContext();
            dataSource = (DataSource) initialContext.lookup(DATA_SOURCE);
            conn = dataSource.getConnection();
            statment = conn.createStatement();
            resultSet = statment.executeQuery("SELECT * FROM genre");
                
            while (resultSet.next()) {
                Genre genre = new Genre();
                genre.setId(resultSet.getInt("genre_id"));
                genre.setName(resultSet.getString("genre_name"));
                genres.add(genre);
            }
            
        } catch (NamingException ex) {
            Logger.getLogger(MySql.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MySql.class.getName()).log(Level.SEVERE, null, ex);
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
        return genres;
    }
}
