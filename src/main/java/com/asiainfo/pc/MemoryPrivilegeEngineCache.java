package com.asiainfo.pc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryPrivilegeEngineCache implements PrivilegeEngineCache{
	
	private Map<Long,List<Menu>> roleMenuMap = new HashMap<Long,List<Menu>>();
	private Map<Long,List<Role>> groupRoleMap = new HashMap<Long,List<Role>>();
	private List<Menu> excludeMenuList = new ArrayList<Menu>();
	

	public void clear() {
		roleMenuMap.clear();
		groupRoleMap.clear();
	}

	public List<Menu> getMenu(long roleId) {
		return roleMenuMap.get(roleId);
	}

	public List<Role> getRole(long groupId) {
		return groupRoleMap.get(groupId);
	}

	public void addOrUpdateRole(long roleId, List<Menu> menuList) {
		roleMenuMap.put(roleId, menuList);
	}

	public void addOrUpdateGroup(long goupId, List<Role> roleList) {
		groupRoleMap.put(goupId, roleList);
	}

	public List<Menu> getExcludeMenu() {
		return excludeMenuList;
	}

	public void addExcludeMenu(List<Menu> menuList) {
		excludeMenuList.addAll(menuList);
	}
	
}
