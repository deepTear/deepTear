package com.deepTear.springboot.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name="t_role")
public class Role {

	private Integer id;
	private Integer pid;
	private String name;
	private List<User> users = new ArrayList<User>();
	private List<Source> sources = new ArrayList<Source>();

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	@Column
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	@Column
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ManyToMany(mappedBy="roles")
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}
	@ManyToMany
	@JoinTable(name="t_role_source",joinColumns={@JoinColumn(name="role_id")},inverseJoinColumns={@JoinColumn(name="source_id")})
	public List<Source> getSources() {
		return sources;
	}
	public void setSources(List<Source> sources) {
		this.sources = sources;
	}

}
