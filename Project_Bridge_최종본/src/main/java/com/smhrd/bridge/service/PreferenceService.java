package com.smhrd.bridge.service;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smhrd.bridge.entity.SongList;
import com.smhrd.bridge.entity.UserLikeList;
import com.smhrd.bridge.mapper.UserMapper;

@Service
public class PreferenceService {
	@Autowired
	UserMapper mapper;
	
	public List<SongList> allSong() {
		return mapper.allSong();
	}
	
	public int songList(List<UserLikeList> userLikeList) {
	    return mapper.songList(userLikeList);
	}
	
	// public List<UserLikeList> getSavedSongs(String user_id) {
    //    return mapper.getSavedSongs(user_id);
    // }
	
	public List<SongList> search(String searchType, String searchInput, int page, int pageSize) {
	    Map<String, String> searchParams = new HashMap<>();
	    searchParams.put("searchType", searchType);
	    searchParams.put("searchInput", searchInput);
	    searchParams.put("offset", String.valueOf((page - 1) * pageSize));
	    searchParams.put("pageSize", String.valueOf(pageSize));

	    return mapper.search(searchParams);
	}
	
	public List<SongList> songRanking() {
		return mapper.songRanking();
	}

}
