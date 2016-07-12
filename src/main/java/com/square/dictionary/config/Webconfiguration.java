package com.square.dictionary.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.square.dictionary.util.Log;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.square.dictionary")
public class Webconfiguration {
	{
		Log.i("system.configuration");
	}
}
