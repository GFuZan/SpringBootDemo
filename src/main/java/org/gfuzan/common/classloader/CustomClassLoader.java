package org.gfuzan.common.classloader;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class CustomClassLoader extends URLClassLoader{
	
	private static final Logger logger = LoggerFactory.getLogger(CustomClassLoader.class);
	
	public CustomClassLoader(){
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
		logger.trace("Load "+url);
        super.addURL(url);
    }
	@Override
	public Class<?> findClass(final String name)throws ClassNotFoundException{
		logger.trace("Load Class: "+name);
		return super.findClass(name);
	}
    
}
