package com.smhrd.bridge.analysis;

import java.util.List;

import com.smhrd.bridge.entity.UserLikeList;

public interface cosain {
	public static double calculateCosineSimilarity(List<UserLikeList> listA, List<UserLikeList> listB) {
        double[] vectorA = extractRatings(listA);
        double[] vectorB = extractRatings(listB);

        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;

        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }

        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }

    private static double[] extractRatings(List<UserLikeList> userList) {
        double[] ratings = new double[userList.size()];
        for (int i = 0; i < userList.size(); i++) {
            ratings[i] = userList.get(i).getRating();
        }
        return ratings;
    }
	
	public static double CosineSimilarity (double[] vectorA, double[] vectorB){
		double dotProduct = 0.0;
		double normA = 0.0;
		double normB = 0.0;
		for (int i = 0; i < vectorA.length; i++) {
			dotProduct += vectorA[i] * vectorB[i];
			normA += Math.pow(vectorA[i], 2);
			normB += Math.pow(vectorB[i], 2);
		}
		return dotProduct / (Math.sqrt(normA)*Math.sqrt(normB));
	}
}
