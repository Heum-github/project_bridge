package com.smhrd.bridge.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.smhrd.bridge.entity.SongList;
import com.smhrd.bridge.entity.UserInfo;
import com.smhrd.bridge.entity.UserLikeList;

@Mapper
public interface UserMapper {
	// 회원가입
	public int register(UserInfo userinfo);

	// 로그인
	public UserInfo login(UserInfo userinfo);

	// 선택한 노래 저장
	public int songList(List<UserLikeList> userLikeList);

	// 노래 다 불러오기
	public List<SongList> allSong();

	// 이전에 선택한 노래 불러오기
	public List<UserLikeList> getSavedSongs(String user_id);

	// 검색 기능
	public List<SongList> search(Map<String, String> searchParams);

	// 회원 정보 수정 기능
	public int update(UserInfo user);

	// 탈퇴 기능
	public int deleteUser(String user_id);
	
	// 노래방 TOP30 불러오기
	public List<SongList> songRanking();
}
