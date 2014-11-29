package com.larcloud.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 配置文件操作支持类
 * 
 * @author jnad
 * 
 */
public abstract class ConfigurationSupport {
	
	/*
	 * 测试入口main
	 */
	public static void main(String[] args) {
		write("cloudConfig.properties","WGPTURLLL","12");
		System.out.println(read("WGPTURL"));
		System.out.println(read("cloudConfig.properties", "WGPTURL"));
	}
	
	/**
	 * 读取.properties文件(默认xxx.properties)
	 * 
	 * @param key
	 * @return !null
	 */
	// static
	public static String read(String key){
			
	
		Properties properties = new Properties();
//		String config = Thread.currentThread().getContextClassLoader().getResource("root.properties").getPath();
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("root.properties"));
		
		} catch (FileNotFoundException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		}
		return properties.getProperty(key.toUpperCase())==null?"":properties.getProperty(key.toUpperCase());
			
	}

	/**
	 * 根据名称读取.properties文件(默认xxx.properties)
	 * 
	 * @param name
	 * @param key
	 * @return !null
	 */
	// static
	
	
	public static String read(String name, String key){
		Properties properties = new Properties();
		String config = Thread.currentThread().getContextClassLoader().getResource(name).getPath();
		
		try {
			properties.load(new FileInputStream(config));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return properties.getProperty(key.toUpperCase())==null?"":properties.getProperty(key.toUpperCase());
	}

	/**
	 * 根据提供的filename ,key 写回配置
	 * 
	 * @param name
	 * @param key
	 * @return
	
	 */
	// static
	public static void write(String filename, String key, String val){

			
		
		Properties prop = new Properties();
		String config = Thread.currentThread().getContextClassLoader()
				.getResource(filename).getPath();
		try {
			prop.load(new FileInputStream(config));
			prop.setProperty(key, val);
			prop.store(new FileOutputStream(config), "saveFile");
			System.out.println(prop.getProperty(key));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	
	}

	

}
