package com.smhrd.bridge.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.smhrd.bridge.entity.UserInfo;
import com.smhrd.bridge.mapper.MatchingMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

@Service
public class AddressService {
	private final String uri = "https://dapi.kakao.com/v2/local/search/address.json";

	@Autowired
	MatchingMapper mapper;
	
	@Value("${kakao.local.key}")
	private String kakaoLocalKey;
	
	// 입력된 노래방 주소 좌표 반환
	public Map<String, String> getCoordinate(String user_address) {
		RestTemplate restTemplate = new RestTemplate();

		String apiKey = "KakaoAK " + kakaoLocalKey;

		// 요청 헤더에 만들기, Authorization 헤더 설정하기
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", apiKey);
		HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

		UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(uri).queryParam("query", user_address).build();

		ResponseEntity<String> response = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, entity,
				String.class);

		// API Response로부터 body 뽑아내기
		String body = response.getBody();
		JSONObject json = new JSONObject(body);

		// body에서 좌표 뽑아내기
		JSONArray documents = json.getJSONArray("documents");
		String x = documents.getJSONObject(0).getString("x");
		String y = documents.getJSONObject(0).getString("y");

		// 좌표를 Map에 담아서 반환
		Map<String, String> coordinates = new HashMap<>();
		coordinates.put("x", x);
		coordinates.put("y", y);

		return coordinates;
	}
	
	// 사용자 주소 좌표 반환
	public List<Map<String, String>> getUserCoordinate(String user_id, HttpSession session) {
		UserInfo user = (UserInfo) session.getAttribute("loginUser");
		List<UserInfo> allUser = mapper.selectAllUser(user.getUser_id()); // 로그인 한 유저 제외 모든 유저의 user_address 추출
		
		List<String> allUserAddresses = new ArrayList<String>();
		for (UserInfo user_address : allUser) {
			allUserAddresses.add(user_address.getUser_address());			
		}

		List<Map<String, String>> allUserCoordinates = new ArrayList<>();
		
		RestTemplate restTemplate = new RestTemplate();
		String apiKey = "KakaoAK " + kakaoLocalKey;

		// 요청 헤더에 만들기, Authorization 헤더 설정하기
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", apiKey);
		HttpEntity<String> entity = new HttpEntity<>(httpHeaders);

		for (String user_address : allUserAddresses) {
	        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(uri).queryParam("query", user_address).build();

	        ResponseEntity<String> response = restTemplate.exchange(uriComponents.toString(), HttpMethod.GET, entity, String.class);

	        String body = response.getBody();
	        JSONObject json = new JSONObject(body);

	        JSONArray documents = json.getJSONArray("documents");
	        String x = documents.getJSONObject(0).getString("x");
	        String y = documents.getJSONObject(0).getString("y");

	        Map<String, String> userCoordinates = new HashMap<>();
	        userCoordinates.put("x", x);
	        userCoordinates.put("y", y);

	        allUserCoordinates.add(userCoordinates);
	    }		
		// 
	    return allUserCoordinates;
	}
	
	// 거리 구하기 (모든 사용자 user_address 가져오기)
	public List<UserInfo> selectAllUser(String user_id) {
		return mapper.selectAllUser(user_id);
	}
}
