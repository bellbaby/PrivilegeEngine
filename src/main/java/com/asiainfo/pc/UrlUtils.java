package com.asiainfo.pc;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtils {
	
	public static boolean isSuit(String oriUrl,String targetUrl){
		URL oriURL = null;
		URL targetURL = null;
		try {
			oriURL = new URL(oriUrl);
		} catch (MalformedURLException e) {
			throw new PrivilegeEngineException("oriUrl malformed exception",e);
		}
		
		try {
			targetURL = new URL(targetUrl);
		} catch (MalformedURLException e) {
			throw new PrivilegeEngineException("targetUrl malformed exception",e);
		}
		
		return oriURL.sameFile(targetURL);
	}
}
