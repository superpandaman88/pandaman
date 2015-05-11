package com.panda.setting.orm;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;

/**
 * 
 * @author gaopan
 * @version $Id: Server.java, v 0.1 2015-5-11 下午9:28:23 gaopan Exp $
 */
public class Server implements Serializable,Cloneable{
	private static final long serialVersionUID = 1L;
	
	@DatabaseField(generatedId = true)
	private int Id;
    @DatabaseField
	private String Name;
	@DatabaseField
	private String Url;
	@DatabaseField
	private String Type;
	@DatabaseField(defaultValue = "false")
	private boolean isSelected;

	
	public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
    
    
	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	@Override
	public Object clone(){  
	    try{  
	        return super.clone();  
	    }catch(Exception e){ 
	        return null; 
	    }
	}
}
