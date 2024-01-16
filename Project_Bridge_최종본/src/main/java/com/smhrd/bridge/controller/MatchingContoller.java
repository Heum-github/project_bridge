package com.smhrd.bridge.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smhrd.bridge.analysis.cosain;
import com.smhrd.bridge.analysis.distance;
import com.smhrd.bridge.entity.UserInfo;
import com.smhrd.bridge.entity.UserLikeList;
import com.smhrd.bridge.service.AddressService;
import com.smhrd.bridge.service.MatchingService;

@Controller
public class MatchingContoller {
	@Autowired
	MatchingService service;

	@Autowired
	AddressService addressService;
	
	@GetMapping("/matchingPage")
	public String matchingPage(HttpSession session, Model model, @RequestParam(defaultValue = "1") int page) {
		// 코사인 유사도 test
		UserInfo user = (UserInfo) session.getAttribute("loginUser");
		List<Map<String, Object>> allUser = service.allUser(user.getUser_id()); // 로그인 한 유저 제외 모든 유저의 user_id 추출
		List<UserLikeList> cosainA = service.cosainA(user.getUser_id()); // 로그인한 유저의 userlikelist 추출

		Map<String, Double> similarityMap = new HashMap<>();

		for (Map<String, Object> userMap : allUser) {
			String B = (String) userMap.get("user_id");
			if (!B.equals(user.getUser_id())) {
				List<UserLikeList> cosainB = service.cosainB(B); // 다른 유저의 userlikelist 추출

				double similarity = calculateCosineSimilarity(cosainA, cosainB); // 코사인 유사도 계산

				similarityMap.put(B, similarity); // Map에 push
			}
		}
		// 큰 순으로 나열하기 (sortedSimilarities)
		List<Map.Entry<String, Double>> sortedSimilarities = similarityMap.entrySet().stream()
				.sorted(Map.Entry.<String, Double>comparingByValue().reversed()).collect(Collectors.toList());

		for (Map.Entry<String, Double> entry : sortedSimilarities) {
			System.out.println("User: " + entry.getKey() + ", Similarity: " + entry.getValue());
		}
		
		List<UserInfo> sortedUserInfoList = new ArrayList<>();
	    for (Map.Entry<String, Double> entry : sortedSimilarities) {
	        String userId = entry.getKey();
	        UserInfo userInfo = service.getUserInfoById(userId); // 해당 메서드는 userId를 이용해 UserInfo를 가져오는 것으로 가정
	        sortedUserInfoList.add(userInfo);
	    }
		
		// 페이징
		int pageSize = 4; // 한 페이지에 표시할 항목 수
		int totalRecipes = sortedUserInfoList.size();
		int totalPages = (int) Math.ceil((double) totalRecipes / pageSize);

		int startIndex = (page - 1) * pageSize;
		int endIndex = Math.min(startIndex + pageSize, totalRecipes);

		List<UserInfo> userAllList = sortedUserInfoList.subList(startIndex, endIndex);

		model.addAttribute("userAll", userAllList);
		model.addAttribute("page", page);
		model.addAttribute("totalPages", totalPages);

		// 확인겸 출력해보기


		return "matchingPage";
	}

	private double calculateCosineSimilarity(List<UserLikeList> cosainA, List<UserLikeList> cosainB) {
		return cosain.calculateCosineSimilarity(cosainA, cosainB);
	}
	
    private int calAge(int birthYear, int birthMonth, int birthDay, int currentYear, int currentMonth, int currentDay) {
        int age = currentYear - birthYear;
        // 만약 생일이 지나지 않았으면 -1
        if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay) 
            age--;
        return age;
    }

    // 조건에 따른 검색.
	@GetMapping("/matchingCon")
	public String matchingCon(HttpSession session, Model model, @RequestParam(defaultValue = "1") int page,
			int sorted, int gender, int age, String user_address) {
		// 코사인 유사도 test
		System.out.println(user_address);
		UserInfo user = (UserInfo) session.getAttribute("loginUser");
		List<Map<String, Object>> allUser = service.allUser(user.getUser_id()); // 로그인 한 유저 제외 모든 유저의 user_id 추출	
		List<UserInfo> sortedUserInfoList = new ArrayList<UserInfo>();		
		
		// 거리 순 일 경우
		if (sorted == 2){
			// 거리순으로 정렬.
			System.out.println("거리분석 실시.");
			
			// 선택한 노래방 주소 가져오기
			Map<String, String> user_real_address = addressService.getCoordinate(user_address);

			// 모든 사용자 주소 좌표 가져오기
			List<Map<String, String>> userResults = addressService.getUserCoordinate(user.getUser_id(), session);

			// 모든 사용자 정보 가져오기 (본인 제외)
			List<UserInfo> userAll = addressService.selectAllUser(user.getUser_id());

			// 거리 계산 결과 담을 리스트
			List<Double> distances = new ArrayList<>();
			
			for (Map<String, String> userResult : userResults) {
			    double result = distance.DistanceByDegree(
			            Double.parseDouble(user_real_address.get("x")),
			            Double.parseDouble(user_real_address.get("y")),
			            Double.parseDouble(userResult.get("x")),
			            Double.parseDouble(userResult.get("y"))
			    );
			    double resultRound = Math.round(result*100) / 100.0;
			    distances.add(resultRound);
			}
			System.out.println("선택한 노래방 주소: " + user_real_address);
			System.out.println("사용자 주소: " + userResults);
			System.out.println("거리 리스트: " + distances);
			
			System.out.println(userAll.size());
			System.out.println(distances.size());
			// 거리는 distances에 남겨있음.
			if (userAll.size() == distances.size()) {
			    for (int i = 0; i < userAll.size(); i++) {
			    	UserInfo userinfo = userAll.get(i);
			    	double result = distances.get(i);
			    	userinfo.setDistance(result);
			        sortedUserInfoList.add(i, userinfo);
			    }
			} else {
			    // userResults와 distances가 다른 크기를 가진 경우 처리
			    System.out.println("오류: userResults와 distances의 크기가 다릅니다.");
			}
			
			Map<Double, UserInfo> distanceMap = new HashMap<>();

		    for (int i = 0; i < userAll.size(); i++) {
		        UserInfo userInfo = userAll.get(i);
		        double result = distances.get(i);
		        distanceMap.put(result, userInfo);
		    }

		    // 거리에 따라 정렬된 키의 Set을 생성
		    Set<Double> sortedDistances = new TreeSet<>(distanceMap.keySet());

		} else {
			List<UserLikeList> cosainA = service.cosainA(user.getUser_id()); // 로그인한 유저의 userlikelist 추출

			Map<String, Double> similarityMap = new HashMap<>();

			for (Map<String, Object> userMap : allUser) {
				String B = (String) userMap.get("user_id");
				if (!B.equals(user.getUser_id())) {
					List<UserLikeList> cosainB = service.cosainB(B); // 다른 유저의 userlikelist 추출

					double similarity = calculateCosineSimilarity(cosainA, cosainB); // 코사인 유사도 계산

					similarityMap.put(B, similarity); // Map에 push
				}
			}
			// 큰 순으로 나열하기 (sortedSimilarities)
			List<Map.Entry<String, Double>> sortedSimilarities = similarityMap.entrySet().stream()
					.sorted(Map.Entry.<String, Double>comparingByValue().reversed()).collect(Collectors.toList());
			for (Map.Entry<String, Double> entry : sortedSimilarities) {
				System.out.println("User: " + entry.getKey() + ", Similarity: " + entry.getValue());
			}
			
			// 정렬된 순서로 list만들기.
		    for (Map.Entry<String, Double> entry : sortedSimilarities) {
		        String userId = entry.getKey();
		        UserInfo userInfo = service.getUserInfoById(userId);
		        sortedUserInfoList.add(userInfo);
		    }
		}
		if (gender == 0) {
			System.out.println("남성");
			int i = 0;
	        while (sortedUserInfoList.size() > i) {
	        	if (sortedUserInfoList.get(i).getUser_gender() == 1) {
		        	sortedUserInfoList.remove(i);	        		
	        	} else {
		        	i += 1;
	        	}
	        }
		} else if (gender == 1){
			System.out.println("여성");
			int i = 0;
			while (sortedUserInfoList.size() > i) {
	        	if (sortedUserInfoList.get(i).getUser_gender() == 0) {
		        	sortedUserInfoList.remove(i);	        		
	        	} else {
		        	i += 1;
	        	}
	        }
		}
			
		// 현재 날짜 - 생년웡일 = 나이
		Calendar current = Calendar.getInstance();
        
        int currentYear  = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay   = current.get(Calendar.DAY_OF_MONTH);

		if (age == 10) {
			int i = 0;
	        while (sortedUserInfoList.size() > i) {
	        	int birthYear = sortedUserInfoList.get(i).getUser_birthday().getYear();
	        	int birthMonth = sortedUserInfoList.get(i).getUser_birthday().getMonthValue();
	        	int birthDay = sortedUserInfoList.get(i).getUser_birthday().getDayOfMonth();	        	
	        	
	        	if (calAge(birthYear, birthMonth, birthDay, currentYear, currentMonth, currentDay) >= 20 ) {
	        		sortedUserInfoList.remove(i);        		
	        	} else {
		        	i += 1;
	        	}
	        }
		} else if (age == 20){
			int i = 0;
	        while (sortedUserInfoList.size() > i) {
	        	int birthYear = sortedUserInfoList.get(i).getUser_birthday().getYear();
	        	int birthMonth = sortedUserInfoList.get(i).getUser_birthday().getMonthValue();
	        	int birthDay = sortedUserInfoList.get(i).getUser_birthday().getDayOfMonth();	        	
	        	
	        	if (calAge(birthYear, birthMonth, birthDay, currentYear, currentMonth, currentDay) >= 30
	        		|| calAge(birthYear, birthMonth, birthDay, currentYear, currentMonth, currentDay) < 20) {
	        		sortedUserInfoList.remove(i);        		
	        	} else {
		        	i += 1;
	        	}
	        }
		} else if (age == 30) {
			int i = 0;
	        while (sortedUserInfoList.size() > i) {
	        	int birthYear = sortedUserInfoList.get(i).getUser_birthday().getYear();
	        	int birthMonth = sortedUserInfoList.get(i).getUser_birthday().getMonthValue();
	        	int birthDay = sortedUserInfoList.get(i).getUser_birthday().getDayOfMonth();	        	
	        	
	        	if (calAge(birthYear, birthMonth, birthDay, currentYear, currentMonth, currentDay) >= 40
	        		|| calAge(birthYear, birthMonth, birthDay, currentYear, currentMonth, currentDay) < 30) {
	        		sortedUserInfoList.remove(i);        		
	        	} else {
		        	i += 1;
	        	}
	        }
		} else if (age == 40) {
			int i = 0;
	        while (sortedUserInfoList.size() > i) {
	        	int birthYear = sortedUserInfoList.get(i).getUser_birthday().getYear();
	        	int birthMonth = sortedUserInfoList.get(i).getUser_birthday().getMonthValue();
	        	int birthDay = sortedUserInfoList.get(i).getUser_birthday().getDayOfMonth();	        	
	        	
	        	if (calAge(birthYear, birthMonth, birthDay, currentYear, currentMonth, currentDay) >= 50
	        		|| calAge(birthYear, birthMonth, birthDay, currentYear, currentMonth, currentDay) < 40) {
	        		sortedUserInfoList.remove(i);        		
	        	} else {
		        	i += 1;
	        	}
	        }
		} else if (age == 50) {
			int i = 0;
	        while (sortedUserInfoList.size() > i) {
	        	int birthYear = sortedUserInfoList.get(i).getUser_birthday().getYear();
	        	int birthMonth = sortedUserInfoList.get(i).getUser_birthday().getMonthValue();
	        	int birthDay = sortedUserInfoList.get(i).getUser_birthday().getDayOfMonth();	        	
	        	
	        	if (calAge(birthYear, birthMonth, birthDay, currentYear, currentMonth, currentDay) >= 60
	        		|| calAge(birthYear, birthMonth, birthDay, currentYear, currentMonth, currentDay) < 50) {
	        		sortedUserInfoList.remove(i);        		
	        	} else {
		        	i += 1;
	        	}
	        }			
		} else if (age == 60) {
			int i = 0;
	        while (sortedUserInfoList.size() > i) {
	        	int birthYear = sortedUserInfoList.get(i).getUser_birthday().getYear();
	        	int birthMonth = sortedUserInfoList.get(i).getUser_birthday().getMonthValue();
	        	int birthDay = sortedUserInfoList.get(i).getUser_birthday().getDayOfMonth();	        	
	        	
	        	if (calAge(birthYear, birthMonth, birthDay, currentYear, currentMonth, currentDay) < 60) {
	        		sortedUserInfoList.remove(i);        		
	        	} else {
		        	i += 1;
	        	}
	        }
		}

		// 페이징
		int pageSize = 4; // 한 페이지에 표시할 항목 수
		int totalRecipes = sortedUserInfoList.size();
		int totalPages = (int) Math.ceil((double) totalRecipes / pageSize);
		int startIndex = (page - 1) * pageSize;
		int endIndex = Math.min(startIndex + pageSize, totalRecipes);

		List<UserInfo> userAllList = sortedUserInfoList.subList(startIndex, endIndex);

		model.addAttribute("userAll", userAllList);
		model.addAttribute("page", page);
		model.addAttribute("totalPages", totalPages);
		return "matchingPage";
		}	
}
