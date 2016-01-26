package com.asiainfo.pc;

import java.util.List;

public interface PrivilegeEngineStore {
	
	public List<Role> queryRole(long userid);
	
	public List<Group> querGroup(long userid);
	
	public List<Role> queryAllRole();
	
	public List<Group> queryAllGroup();
	
	public List<Menu> queryAllExcludeMenu();
	
}
