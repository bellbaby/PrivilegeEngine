package com.asiainfo.pc;

import java.util.List;

/**
 * The main privilege control and manage interface
 * @author sunguihua
 *
 */
public interface PrivilegeEngine {
	
	/**
	 *  获取用户角色
	 * @param userid
	 * @return
	 */
	public List<Role> getRole(long userid);
	
	/**
	 * 获取用户分组
	 * @param userid
	 * @return
	 */
	public List<Group> getGroup(long userid);
	
	/**
	 * 判断用户是否有对应资源权限
	 * @param userId  用户ID
	 * @param url 统一资源地址
	 * @return 
	 */
	public boolean isPermited(long userId,String url);
	
	
	public boolean isPermited(List<Long> roleIds,List<Long> groupIds,String url);
	
	public boolean isPermited(List<Long> roleIds,String url);
	
	/**
	 * 判断用户是否有指定资源中 指定元素的权限
	 * @param userId
	 * @param url
	 * @param elementId
	 * @return
	 */
	public boolean isPermited(long userId,String url,long elementId);
	
	
	public void refreshCache();
}
