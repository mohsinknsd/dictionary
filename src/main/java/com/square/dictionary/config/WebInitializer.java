package com.square.dictionary.config;

import javax.servlet.Filter;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.square.dictionary.util.Log;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	@Override
    protected Class<?>[] getRootConfigClasses() {
		Log.i("system.initialization");
        return new Class[] { Webconfiguration.class };
    }
  
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }
  
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
    
    @Override
    protected Filter[] getServletFilters() {
        Filter [] singleton = { new TokenFilter()};
        return singleton;
    }
}