package com.smhrd.bridge.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.smhrd.bridge.entity.SongList;
import com.smhrd.bridge.service.PreferenceService;

@RestController
public class PreferenceRestController {
    @Autowired
    PreferenceService service;

    @PostMapping("/preference/search")
    @ResponseBody
    public Map<String, Object> search(@RequestBody Map<String, String> searchParams,
                                      @RequestParam(required = false) Integer page) {
    	 if (page == null) {
    		 	System.out.println("페이지 값이 0입니다.");
    	        page = 1;
    	    }
    	
    	System.out.println("Received searchParams: " + searchParams);
        System.out.println("Received page: " + page);
        
    	String searchType = searchParams.get("searchType");
        String searchInput = searchParams.get("searchInput");

        int pageSize = 30;
        
        List<SongList> searchResults = service.search(searchType, searchInput, page, pageSize);

        int totalSonglist = searchResults.size();
        int totalPages = (int) Math.ceil((double) totalSonglist / pageSize);
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, totalSonglist);

        List<SongList> songList = searchResults.subList(startIndex, endIndex);

        System.out.println("Total SongList Size: " + totalSonglist);
        System.out.println("Total Pages: " + totalPages);

        Map<String, Object> response = new HashMap<>();
        response.put("songList", songList); // 검색 결과로 나온 노래 리스트
        response.put("page", page);
        response.put("totalPages", totalPages);

        return response;
    }
}
