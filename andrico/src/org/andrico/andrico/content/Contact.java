/****************************************
 * 		                       			*
 *     Copyright 2009 Andrico Team 		*
 *   http://code.google.com/p/andrico/	*
 *										*
 ****************************************/
package org.andrico.andrico.content;

public class Contact{
	private String fbId;//facebook id
    private String name;
    private String secondName;
    private String dateOfBirth;
    private String adress;
    private String page;
    private int id; //id in db
    
    public Contact()
    {
    	name = "";
    	secondName = "";
    	dateOfBirth = "date of birth is not avaliable";
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
    
    public String getDateOfBirth() 
    {
            return dateOfBirth;
    }
    
    public void setDateOfBirth(String date) 
    {
            this.dateOfBirth = date;
    }
            
    public String getAdress() 
    {
            return adress;
    }
    
    public void setAdress(String info) 
    {
            this.adress = info;
    }
    
    public String getPage() 
    {
            return page;
    }
    
    public void setPage(String info) 
    {
            this.page = info;
    }
    
    public String getFBid() 
    {
            return fbId;
    }
    
    public void setFBid(String info) 
    {
            this.fbId = info;
    }
    
    public String toString() 
    {
            return "facebook id: " + fbId +"name: " + name + " second name: " + secondName + " date of birth: " + dateOfBirth + " adress: " + adress + " id: " + id;
    }
    
    
}