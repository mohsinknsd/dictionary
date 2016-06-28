package com.square.dictionary.service;

import java.io.IOException;
import java.util.Properties;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.JDBCConnectionException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.square.dictionary.model.User;
import com.square.dictionary.util.Log;

@Service("UserService")
@Transactional
public class UserServiceImpl implements UserService{

	@Override
	public User loginIfExist(String email, String password) {
		Session session = null;
		User user = new User();
		try {
			session = getSession();
			session.beginTransaction();
			Query procedure = session.createSQLQuery("call get_user(:email, :password)")
					.setParameter("email", email).setParameter("password", password);
			procedure.setResultTransformer(Transformers.aliasToBean(User.class));		
			user = (User) procedure.uniqueResult();			
			session.getSessionFactory().close();
		} catch (JDBCConnectionException e) {			
			Log.e(WordServiceImpl.class, e);
		} catch (IOException e) {
			Log.e(WordServiceImpl.class, e);
		} finally {
			if (session != null && session.isOpen())
				session.close();
		}				
		return user;
	}
	
	public static Session getSession() throws JDBCConnectionException, IOException {
		Log.i(UserServiceImpl.class, "hibernate.session.opening");
		Properties properties = new Properties();		
		properties.load(WordServiceImpl.class.getClassLoader().getResourceAsStream("/config.properties"));
		Configuration configuration = new Configuration().setProperties(properties);
		return configuration.buildSessionFactory(new StandardServiceRegistryBuilder().
				applySettings(configuration.getProperties()).build()).openSession();		 
	}
}