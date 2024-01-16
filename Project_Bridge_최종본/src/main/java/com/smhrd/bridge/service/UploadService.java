package com.smhrd.bridge.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smhrd.bridge.converter.ImageConverter;
import com.smhrd.bridge.converter.ImageToBase64;
import com.smhrd.bridge.entity.UserInfo;
import com.smhrd.bridge.entity.UserSong;
import com.smhrd.bridge.mapper.UploadMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UploadService {
	
	@Autowired
	UploadMapper mapper;
	
	//데이터를 JSON 객체로 변환하기 위해서 사용
    private final ObjectMapper objectMapper;
	
    // flask 서버로 정보 보내고 받기
    @Transactional
    public void uploadMusic(UserSong usersong) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();
        //헤더를 JSON으로 설정함
        HttpHeaders headers = new HttpHeaders();
        
        //파라미터로 들어온 usersong을 JSON 객체로 변환
        headers.setContentType(MediaType.APPLICATION_JSON);
        String param = objectMapper.writeValueAsString(usersong);
        HttpEntity<String> entity = new HttpEntity<>(param, headers);
        String url = "http://127.0.0.1:5000/receive_string";

        // Flask server로 POST request
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, entity, String.class);
        String jsonResponse = responseEntity.getBody();

        // JSON -> UserSong 변환
        UserSong responseUserSong = objectMapper.readValue(jsonResponse, UserSong.class);

        // flask에서 받아온 정보를 usersong에 추가하기
        usersong.setUser_id(responseUserSong.getUser_id());
        usersong.setUser_song_name(responseUserSong.getUser_song_name());
        usersong.setUser_singer(responseUserSong.getUser_singer());
        usersong.setSong_file(responseUserSong.getSong_file());
        System.out.println(responseUserSong);
        
        // DB로 정보 보내기
        mapper.uploadMusic(responseUserSong);
    }
	
	
	public List<UserSong> mypage(UserInfo member) throws IOException{
		List<UserSong> list = mapper.mypage(member);
		for(int i = 0; i < list.size(); i++) {
			UserSong contents = list.get(i);
			File file = new File("c:\\uploadMusic\\" + contents.getSong_file());
			ImageConverter<File, String> converter= new ImageToBase64();
			String fileStringValue = converter.convert(file);
			list.get(i).setSong_file(fileStringValue);
		}
		return list;		
	}	
}
