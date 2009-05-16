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
    private String phone1;
    private String phone2;
    private String adress;
    private int id;
    
    public Contact()
    {
    	name = "";
    	secondName = "";
    	phone1 = "no number avaliable";
    	phone2 = "no number avaliable";
    	adress = "no adress avaliable";	
    }
    
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
    
    public String getPhone_private() 
    {
            return phone1;
    }
    
    public String getPhone_work() 
    {
            return phone2;
    }
    
    public String getAdress() 
    {
            return adress;
    }
    
    public void setPhone_private(String info) 
    {
            this.phone1 = info;
    }
    
    public void setPhone_work(String info) 
    {
            this.phone2 = info;
    }
    
    public void setAdress(String info) 
    {
            this.adress = info;
    }
    
    public String toString() 
    {
            return "name: " + name + " second name: " + secondName + " phone1: " + phone1 + " phone2: " + phone2 + " adress: " + adress + " id: " + id;
    }
    
    
}