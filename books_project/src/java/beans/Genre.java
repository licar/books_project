package beans;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author user
 */

public class Genre{
private Integer id;
    private String name;
    
    public Genre(){
        
    }
    
    public Genre(String name, Integer id){
        this.name = name;
        this.id = id;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public String getName(){
        return name;
    }
    
    public void setId(Integer id){
        this.id = id;
    }
    
    public Integer getId(){
        return id;
    }
}
