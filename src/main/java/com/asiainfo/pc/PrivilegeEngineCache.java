package com.asiainfo.pc;

import java.util.List;

public interface PrivilegeEngineCache {
	
	public void clear();
	
	public List<Menu> getMenu(long roleId);
	
	public List<Role> getRole(long groupId);
	
	public void addOrUpdateRole(long roleId,List<Menu> menuList);
	
	public void addOrUpdateGroup(long goupId,List<Role> roleList);
	
	public List<Menu> getExcludeMenu();
	
	public void addExcludeMenu(List<Menu> menuList);
}
