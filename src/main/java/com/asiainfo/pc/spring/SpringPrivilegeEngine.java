package com.asiainfo.pc.spring;

import org.springframework.beans.factory.InitializingBean;

import com.asiainfo.pc.DefaultPrivilegeEngine;

public class SpringPrivilegeEngine extends DefaultPrivilegeEngine implements InitializingBean{
	
	public void afterPropertiesSet() throws Exception {
		inite();
		
	}
	
}
