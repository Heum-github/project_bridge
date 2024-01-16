package com.smhrd.bridge.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smhrd.bridge.analysis.cosain;
import com.smhrd.bridge.entity.Coordinates;
import com.smhrd.bridge.entity.UserInfo;
import com.smhrd.bridge.entity.UserLikeList;
import com.smhrd.bridge.entity.UserSong;
import com.smhrd.bridge.mapper.MatchingMapper;

@Service
public class MatchingService {

	@Autowired
	MatchingMapper mapper;

	public List<UserInfo> matchingPage() {
		return mapper.matchingPage();
	}

	public List<UserSong> matchingProfile(String user_id) {
		return mapper.matchingProfile(user_id);
	}

	// 코사인 유사도
	public List<Map<String, Object>> allUser(String user_id) {
		return mapper.allUser(user_id);
	}

	public List<UserLikeList> cosainA(String user_id) {
		return mapper.cosainA(user_id);
	}

	public List<UserLikeList> cosainB(String user_id) {
		return mapper.cosainB(user_id);
	}

	public UserInfo getUserInfoById(String user_id) {
		return mapper.getUserInfoById(user_id);
	}

	// 음원 분석 기반 노래 추천
	public List<UserSong> matchingRecommender(String user_id) {
		// 모든 usersong 정보 가져옴.
		ArrayList<UserSong> user = mapper.matchingRecommender(user_id);
		UserSong user_cri = new UserSong();
		ArrayList<UserSong> user_another = new ArrayList<UserSong>();
		Map<UserSong, Double> sortMap = new HashMap<UserSong, Double>();

		for (int i = 0; i < user.size(); i++) {
			if (user.get(i).getUser_id().equals(user_id)) {
				// user의 user_id가 받아온 user_id와 같은 리스트 user에서 제외하고 새로운 변수에 담기.
				user_cri = user.get(i);
			} else {
				UserSong a = user.get(i);
				user_another.add(a);
			}
		}

		// A를 기준으로 user_id의 노래를 제외한 특성들에 대해서 코사인 유사도 분석
		for (int i = 0; i < user_another.size(); i++) {
			// list의 값들을 가져와서 코사인 유사도 분석.
			double[] A = new double[] { user_cri.getTempo(), user_cri.getWave(), user_cri.getChroma_stft_mean(),
					user_cri.getRmse_mean(), user_cri.getSpec_cent_mean(), user_cri.getHarmony_mean(),
					user_cri.getMfcc_mean() };
			double[] B = new double[] { user_another.get(i).getTempo(), user_another.get(i).getWave(),
					user_another.get(i).getChroma_stft_mean(), user_another.get(i).getRmse_mean(),
					user_another.get(i).getSpec_cent_mean(), user_another.get(i).getHarmony_mean(), user_another.get(i).getMfcc_mean() };
			double result = cosain.CosineSimilarity(A, B);
			sortMap.put(user_another.get(i), result);
		}
		;

		// 코사인 유사도에 따라서 정렬
		List<Map.Entry<UserSong, Double>> sortedSimilarities = sortMap.entrySet().stream()
				.sorted(Map.Entry.<UserSong, Double>comparingByValue().reversed()).collect(Collectors.toList());

		// 상위 3개 데이터 산출 + List<usersong>에 담기
		List<UserSong> top3UserSongs = sortedSimilarities.stream().limit(3).map(Map.Entry::getKey)
				.collect(Collectors.toList());
		return top3UserSongs;
	}
	
	public List<UserInfo> searchProfile(String user){
		return mapper.searchProfile(user);
	}
	
}
