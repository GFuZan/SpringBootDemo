package org.gfuzan.modules.dto;

import java.io.Serializable;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UserVo implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -5362158762069971405L;

	@Max(value=100,message="id 验证失败")
	private Integer id;
	
	@NotNull(message="name 不能为空")
    private String name;

	@Max(value=170,message="年龄验证失败")
	@Min(value=0,message="年龄验证失败")
	private Integer age;
    
    public UserVo() {
        
    }
    
    public UserVo(String name) {
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

	public UserVo(@NotNull(message = "name 不能为空") String name,
			@Max(value = 170, message = "年龄验证失败") @Min(value = 0, message = "年龄验证失败") Integer age) {
		this.name = name;
		this.age = age;
	}
}
