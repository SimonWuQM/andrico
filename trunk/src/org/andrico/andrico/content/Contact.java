/****************************************
 * 		                       			*
 *     Copyright 2009 Andrico Team 		*
 *   http://code.google.com/p/andrico/	*
 *										*
 ****************************************/
package org.andrico.andrico.content;

public class Contact{
    private String name;
    private String secondName;
    private String info;
    private int id;
    
    public int getId() 
    {
            return id;
    }
    
    public void setId(int id) 
    {
            this.id = id;
    }
    
    public String getName() 
    {
            return name;
    }
    
    public void setName(String name) 
    {
            this.name = name;
    }
    
    public String getSecondName() 
    {
            return secondName;
    }
    
    public void setSecondName(String secondName) 
    {
            this.secondName = secondName;
    }
    
    public String getInfo() 
    {
            return info;
    }
    
    public void setInfo(String info) 
    {
            this.info = info;
    }
    
    public String toString() 
    {
            return "name: " + name + " second name: " + secondName + " info: " + info + " id: " + id;
    }
    
    
}