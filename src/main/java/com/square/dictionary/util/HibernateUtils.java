package com.square.dictionary.util;

import java.io.IOException;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.service.ServiceRegistry;

import com.square.dictionary.service.WordServiceImpl;

public class HibernateUtils {
	private static Configuration configuration = null; static {
		try {
			Properties properties = new Properties(); 
			properties.load(WordServiceImpl.class.getClassLoader().getResourceAsStream("/config.properties"));
			configuration = new Configuration().setProperties(properties);			
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public static Session getSession() throws JDBCConnectionException {
		ServiceRegistry registry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		return configuration.buildSessionFactory(registry).openSession();		 
	}
}
