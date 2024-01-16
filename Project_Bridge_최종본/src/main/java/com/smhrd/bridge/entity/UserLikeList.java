package com.smhrd.bridge.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLikeList {
	private long idx;
	private String user_id;
	private int song_num;
	private int rating;
}
