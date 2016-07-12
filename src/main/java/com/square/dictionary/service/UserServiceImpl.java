package com.square.dictionary.service;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.square.dictionary.model.User;
import com.square.dictionary.util.Log;

@Service("UserService")
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public User getUserDetails(String email, String password) {		
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		User user = new User();
		try {
			transaction = session.beginTransaction();
			Query procedure = session.createSQLQuery("call sp_get_user_details (:email, :password)")
					.setParameter("email", email).setParameter("password", password);
			procedure.setResultTransformer(Transformers.aliasToBean(User.class));		
			user = (User) procedure.uniqueResult();	
			transaction.commit();
		} catch (Exception e) {
			transaction.rollback();
			if (session.isOpen()) session.close();
			Log.e(e);			
		}				
		return user;
	}

	@Override	
	public boolean registerNewUser(User user) {
		
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			int isResigtered = (Integer) session.save(user);
			transaction.commit();			
			return (isResigtered> 0) ? true : false;			
		} catch (Exception e) {
			transaction.rollback();
			if (session.isOpen()) session.close();
			Log.e(e);
			return false;
		}
	}
}