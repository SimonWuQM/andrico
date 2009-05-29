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
    	dateOfBirth = "";
    	adress = "";	
    }
    
    public boolean Equals(Contact cont)
    {
    	if (!(this.getName().equals(cont.getName())) || !(this.getSecondName().equals(cont.getSecondName())) || 
    				(!this.getFBid().equals(cont.getFBid())) || (!this.getAdress().equals(cont.getAdress())) || 
    				(!this.getPage().equals(cont.getPage())))
    	{	
    		return false;
    	}
    	else
    	{
    		return  true;
    	}
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
    	if (!dateOfBirth.equals(""))
    	{
            return dateOfBirth;
    	}
    	else
    	{
    		return "date of birth isn't avaliable";
    	}
    }
    
    public void setDateOfBirth(String date) 
    {
            this.dateOfBirth = date;
    }
            
    public String getAdress() 
    {
    	if (!adress.equals(""))
    	{
            return adress;
    	}
    	else
    	{
    		return "no address avaliable";
    	}
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