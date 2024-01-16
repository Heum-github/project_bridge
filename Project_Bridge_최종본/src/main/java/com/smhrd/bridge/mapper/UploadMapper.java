package com.smhrd.bridge.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.smhrd.bridge.entity.UserInfo;
import com.smhrd.bridge.entity.UserSong;

@Mapper
public interface UploadMapper {
	// 업로드한 음원 불러오기
	public void uploadMusic(UserSong UserSong);
	public List<UserSong> mypage(UserInfo member);
}
