/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package contollers;

import beans.Genre;
import java.io.Serializable;
import java.util.ArrayList;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import database.MySql;

/**
 *
 * @author user
 */

@ApplicationScoped
@ManagedBean(name = "genresController", eager = true)
public class GenresController implements Serializable{
    private ArrayList<Genre> genres = new ArrayList<Genre>();
    private MySql database = MySql.GetInstance();
    
    public ArrayList<Genre> getGenres(){
        if (genres.isEmpty()){
            genres = database.getAllGenres();
        }
        return genres;
    }
}
