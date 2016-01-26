package com.asiainfo.pc.spring;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.asiainfo.pc.Group;
import com.asiainfo.pc.PrivilegeEngine;
import com.asiainfo.pc.Role;

public class SpringSessionPrivilegeInterceptor extends HandlerInterceptorAdapter{
	
	private final static Log logger = LogFactory.getLog(SpringSessionPrivilegeInterceptor.class);
	
	private PrivilegeEngine privilegeEngine;
	
	private String redirectUrl;

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public boolean preHandle(HttpServletRequest req, HttpServletResponse res,
			Object handler) throws Exception {
		
		
		HttpSession session = req.getSession();
		Object ro = session.getAttribute("roleIds");
		Object go = session.getAttribute("groupIds");
		
		List<Long>  roleIds = null;
		if(ro!=null){
			roleIds = (List<Long>) ro;
		}
		List<Long> groupIds = null;
		if(go!=null){
			groupIds = (List<Long>) go;
		}
		
		if(ro==null&&go==null){
			Object useridObj =  session.getAttribute("userid");
			if(useridObj!=null){
				
				long userid = (Long) useridObj;
				
				List<Role> roleList = privilegeEngine.getRole(userid);
				if(roleList!=null&&roleList.size()!=0){
					roleIds = new ArrayList<Long>();
					for(Role role:roleList){
						roleIds.add(role.getRoleId());
					}
					session.setAttribute("roleIds", roleIds);
				}
				
				List<Group> groupList = privilegeEngine.getGroup(userid);
				if(groupList!=null&&groupList.size()!=0){
					groupIds = new ArrayList<Long>();
					for(Group group:groupList){
						groupIds.add(group.getGroupId());
					}
				}
				session.setAttribute("groupIds", groupIds);
			}
		}
		StringBuffer url = req.getRequestURL();
		
		boolean foo = privilegeEngine.isPermited(roleIds,groupIds, url.toString());
		if(!foo&&!StringUtils.isEmpty(redirectUrl)){
			logger.info("request for ["+url+"]is forbidden. userid:"+session.getAttribute("userid"));
			req.getRequestDispatcher(redirectUrl).forward(req, res);
			logger.info("redirect to ["+req.getContextPath()+redirectUrl+"]");
		}else{
			logger.info("request for ["+url+"] is permited. userid:"+session.getAttribute("userid"));
		}
		return foo;
	}

	public PrivilegeEngine getPrivilegeEngine() {
		return privilegeEngine;
	}

	public void setPrivilegeEngine(PrivilegeEngine privilegeEngine) {
		this.privilegeEngine = privilegeEngine;
	}

}
