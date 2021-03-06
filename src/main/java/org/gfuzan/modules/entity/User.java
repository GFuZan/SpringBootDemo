package org.gfuzan.modules.entity;

import java.io.Serializable;

public class User implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5362158762069971405L;

	private Integer id;
	
    private String name;

	private Integer age;
    
    public User() {
        
    }
    
    public User(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User [name=" + name + "]";
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public User(String name, Integer age) {
		this.name = name;
		this.age = age;
	}
}
