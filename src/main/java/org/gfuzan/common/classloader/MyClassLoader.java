package org.gfuzan.common.classloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;


public class MyClassLoader extends URLClassLoader{
	
	public MyClassLoader(){
		super(new URL[0]);
	}
	
	public void addURL(String dir){
		try {
			this.addURL(new URL("file",null,dir));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void addURL(URL url) {
		System.err.println(url);
        super.addURL(url);
    }
	@Override
	public Class<?> findClass(final String name)throws ClassNotFoundException{
		System.err.println("Load Class: "+name);
		return super.findClass(name);
	}
    
}
