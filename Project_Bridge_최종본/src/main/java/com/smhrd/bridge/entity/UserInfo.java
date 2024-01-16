package com.smhrd.bridge.entity;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
	private String user_id;
	private String user_pw;
	private String user_name;
	private String user_nick;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate user_birthday;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate join_date;
	private String user_address;
	private int user_gender;
	private String user_intro;
	private String user_fav;
	private Double distance;
}
