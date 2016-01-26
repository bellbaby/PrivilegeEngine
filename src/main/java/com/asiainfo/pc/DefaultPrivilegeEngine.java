package com.asiainfo.pc;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultPrivilegeEngine implements PrivilegeEngine{
	
	private final static Log logger = LogFactory.getLog(DefaultPrivilegeEngine.class);

	private boolean isInited = false;
	
	private PrivilegeEngineStore stroe;

	private PrivilegeEngineCache engineCache;
	

	public void inite(){
		if(logger.isTraceEnabled()){
			logger.trace("inite DefaultPrivilegeEngine.");
		}
		if(isInited){
			logger.debug("already inited.");
			return;
		}
		if(this.stroe==null){
			logger.warn("no PrivilegeStore is setted.");
			throw new PrivilegeEngineException("PrivilegeEngineStore is required!");
		}
		if(engineCache==null){
			logger.info("no PrivilegeEngineCache setted, use default cache implementation.");
			this.engineCache = new MemoryPrivilegeEngineCache();
		}
		
		refreshCache();
		if(logger.isTraceEnabled()){
			logger.trace("DefaultPrivilege inite successfully.");
		}
	}
	
	//------------------interface----------------------

	public boolean isPermited(long userId, String url) {
		
		List<Role> roles = stroe.queryRole(userId);
		for(Role role:roles){
			if(isPermited(role, url)){
				return true;
			}
		}
		
		List<Group> groups = stroe.querGroup(userId);
		for(Group group:groups){
			if(isPermited(group, url)){
				return true;
			}
		}
		
		return false;
	}

	public boolean isPermited(long userId, String url, long elementId) {
		return false;
	}

	public boolean isPermited(List<Long> roleIds,List<Long> groupIds, String url) {
		for(Menu menu:this.engineCache.getExcludeMenu()){
			if(UrlUtils.isSuit(menu.getUrl(), url)){
				return true;
			}
		}
		if(roleIds!=null){
			for(Long roleId:roleIds){
				List<Menu> menuList = this.engineCache.getMenu(roleId);
				if(menuList==null||menuList.size()==0){
					continue;
				}
				for(Menu menu:menuList){
					if(UrlUtils.isSuit(menu.getUrl(), url)){
						return true;
					}
				}
			}
		}
		
		if(groupIds!=null&&groupIds.size()>0){
			for(Long groupId:groupIds){
				List<Role> roleList = this.engineCache.getRole(groupId);
				if(roleList==null||roleList.size()==0){
					continue;
				}
				for(Role role:roleList){
					List<Menu> menuList = this.engineCache.getMenu(role.getRoleId());
					for(Menu menu:menuList){
						if(UrlUtils.isSuit(menu.getUrl(), url)){
							return true;
						}
					}
				}
			}
		}
		
		
		return false;
	}
	
	public boolean isPermited(List<Long> roleIds,String url){
		return isPermited(roleIds, null, url);
	}
	
	private boolean isPermited(Role role,String url){
		
		for(Menu menu:this.engineCache.getExcludeMenu()){
			if(UrlUtils.isSuit(menu.getUrl(), url)){
				return true;
			}
		}
		List<Menu> menuList = this.engineCache.getMenu(role.getRoleId());
		if(menuList==null){
			return false;
		}
		for(Menu menu:menuList){
			if(UrlUtils.isSuit(menu.getUrl(), url)){
				return true;
			}
		}
		return false;
	}
	
	private boolean isPermited(Group group,String url){
		
		for(Menu menu:this.engineCache.getExcludeMenu()){
			if(UrlUtils.isSuit(menu.getUrl(), url)){
				return true;
			}
		}
		List<Role> roleList = this.engineCache.getRole(group.getGroupId());
		if(roleList==null){
			return false;
		}
		for(Role role:roleList){
			List<Menu> menuList = this.engineCache.getMenu(role.getRoleId());
			for(Menu menu:menuList){
				if(UrlUtils.isSuit(menu.getUrl(), url)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	public synchronized void refreshCache(){
		
		this.engineCache.clear();
		
		List<Group> groupList = this.stroe.queryAllGroup();
		if(groupList!=null){
			for(Group group:groupList){
				this.engineCache.addOrUpdateGroup(group.getGroupId(),group.getRoleList());
			}
		}
		
		List<Role> roleList = this.stroe.queryAllRole();
		if(roleList!=null){
			for(Role role:roleList){
				this.engineCache.addOrUpdateRole(role.getRoleId(), role.getMenuList());
			}
		}
		
		List<Menu> excludeMenuList = this.stroe.queryAllExcludeMenu();
		if(excludeMenuList!=null){
			this.engineCache.addExcludeMenu(excludeMenuList);
		}
		
	}
	
	//-----------------------get set---------------------------------
	public PrivilegeEngineCache getEngineCache() {
		return engineCache;
	}

	public void setEngineCache(PrivilegeEngineCache engineCache) {
		this.engineCache = engineCache;
	}


	public PrivilegeEngineStore getStroe() {
		return stroe;
	}

	public void setStroe(PrivilegeEngineStore stroe) {
		this.stroe = stroe;
	}

	public List<Role> getRole(long userid) {
		return this.stroe.queryRole(userid);
	}

	public List<Group> getGroup(long userid) {
		return this.stroe.querGroup(userid);
	}
}
