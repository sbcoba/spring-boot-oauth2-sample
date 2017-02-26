package com.example;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Member {
	@Id
	@GeneratedValue
	Long id;
	String name;
	@Column(unique = true)
	String username;
	String email;
	String phone;
    String nick;
    @Temporal(TemporalType.TIMESTAMP)
    Date lastLoginDate;
	String remark;

	public Member() {}
	public Member(String name, String username, String email, String phone, String nick, Date lastLoginDate, String remark) {
		this.name = name;
		this.username = username;
		this.email = email;
		this.phone = phone;
		this.remark = remark;
	}
}