package com.smhrd.bridge.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.smhrd.bridge.entity.UserInfo;
import com.smhrd.bridge.entity.UserLikeList;
import com.smhrd.bridge.entity.UserSong;

@Mapper
public interface MatchingMapper {
	public List<UserInfo> matchingPage();
	public List<UserSong> matchingProfile(String user_id);
	public ArrayList<UserSong> matchingRecommender(String user_id);
	public List<Map<String, Object>> allUser(String user_id);
	public List<UserLikeList> cosainA(String user_id);
	public List<UserLikeList> cosainB(String user_id);
	public UserInfo getUserInfoById(String user_id);
	public List<UserInfo> searchProfile(String user);
	public List<UserInfo> selectAllUser(String user);
}
