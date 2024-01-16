package com.smhrd.bridge.analysis;

public interface distance {
	public static double DistanceByDegree(double _latitude1, double _longitude1, double _latitude2, double _longitude2){
	    double theta, dist;
	    theta = _longitude1 - _longitude2;
	    dist = Math.sin(DegreeToRadian(_latitude1)) * Math.sin(DegreeToRadian(_latitude2)) + Math.cos(DegreeToRadian(_latitude1))
	            * Math.cos(DegreeToRadian(_latitude2)) * Math.cos(DegreeToRadian(theta));
	    dist = Math.acos(dist);
	    dist = RadianToDegree(dist);

	    dist = dist * 60 * 1.1515;
	    dist = dist * 1.609344;    // 단위 mile 에서 km 변환.

	    return dist;
	}

	// degree->radian 변환
	public static double DegreeToRadian(double degree){
	        return degree * Math.PI / 180.0;
	    }

	// randian -> degree 변환
	public static double RadianToDegree(double radian){
	        return radian * 180d / Math.PI;
	    }
}
