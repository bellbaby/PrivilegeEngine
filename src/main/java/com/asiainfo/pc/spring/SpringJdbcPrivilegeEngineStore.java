package com.asiainfo.pc.spring;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.asiainfo.pc.Group;
import com.asiainfo.pc.Menu;
import com.asiainfo.pc.PrivilegeEngineStore;
import com.asiainfo.pc.Role;

public class SpringJdbcPrivilegeEngineStore implements PrivilegeEngineStore {

	private DataSource dataSource;

	private JdbcTemplate template;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.template = new JdbcTemplate(dataSource);
	}

	public List<Role> queryRole(long userid) {
		List<Role> roleList = queryRoleByUserid(userid);
		for(Role role:roleList){
			List<Menu> menuList = queryMenuByRoleid(role.getRoleId());
			role.setMenuList(menuList);
		}
		return roleList;
	}

	public List<Group> querGroup(long userid) {
		List<Group> groupList = queryGroupByUserid(userid);
		for(Group group:groupList){
			List<Role> roleList = queryRoleByGroupid(group.getGroupId());
			group.setRoleList(roleList);
		}
		return groupList;
	}

	public List<Role> queryAllRole() {
		List<Role> roleList = this.template.query("select * from pm_role", new RoleRowMapper());
		if(roleList!=null){
			for(Role role:roleList){
				List<Menu> menuList = queryMenuByRoleid(role.getRoleId());
				role.setMenuList(menuList);
			}
		}
		return roleList;
	}

	public List<Group> queryAllGroup() {
		List<Group> groupList = this.template.query("select * from pm_group",new GroupRowMapper());
		if(groupList!=null){
			for(Group group:groupList){
				List<Role> roleList = queryRoleByGroupid(group.getGroupId());
				group.setRoleList(roleList);
			}
		}
		return groupList;
	}

	private List<Role> queryRoleByUserid(long userid) {
		return this.template
				.query("select r.* from pm_role r,pm_user_role ur where r.role_id = ur.role_id and ur.user_id = ?",
						new Object[] { userid }, new RoleRowMapper());
	}

	private List<Menu> queryMenuByRoleid(long roleid) {
		return this.template
				.query("select m.* from pm_menu m, pm_role_menu rm where m.menu_id = rm.menu_id and rm.role_id = ?",
						new Object[] { roleid }, new MenuRowMapper());
	}
	
	private List<Group> queryGroupByUserid(long userid){
		return this.template.query("select g.* from pm_group g,pm_user_group ug where g.group_id=ug.group_id and ug.user_id = ?", new Object[]{userid},new GroupRowMapper());
	}
	
	private List<Role> queryRoleByGroupid(long groupid){
		return this.template.query("select r.* from pm_role r,pm_group_role gr where r.role_id = gr.role_id and gr.group_id = ?", new Object[]{groupid},new RoleRowMapper());
	}
	
	public List<Menu> queryAllExcludeMenu() {
		return this.template.query("select * from pm_menu where menu_type = 2",new MenuRowMapper());
	}
	
	class RoleRowMapper implements RowMapper<Role>{

		public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
			Role role = new Role();
			role.setRoleId(rs.getLong("role_id"));
			role.setRoleName(rs.getString("role_title"));
			return role;
		}
		
	}
	
	class MenuRowMapper implements RowMapper<Menu>{

		public Menu mapRow(ResultSet rs, int rowNum) throws SQLException {
			Menu m = new Menu();
			m.setMenuId(rs.getLong("menu_id"));
			m.setMenuName(rs.getString("menu_title"));
			m.setUrl(rs.getString("menu_url"));
			return m;
		}
		
	}
	
	class GroupRowMapper implements RowMapper<Group>{

		public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
			Group group = new Group();
			group.setGroupId(rs.getLong("group_id"));
			group.setGroupName(rs.getString("group_name"));
			return group;
		}
		
	}

}
