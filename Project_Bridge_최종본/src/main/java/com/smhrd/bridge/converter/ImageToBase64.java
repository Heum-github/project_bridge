package com.smhrd.bridge.converter;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;

public class ImageToBase64 implements ImageConverter<File, String> {

	@Override
	public String convert(File f) throws IOException { // 부모인터페이스의 추상 메소드에도 throws해줘야함.
		
		// File 객체를 String으로 변환 (Base64 사용)
		// 1. File객체를 byte[]로 변환
		byte[] fileContent = FileUtils.readFileToByteArray(f);
		
		// 2. byte[] -> String(base64)
		String result = Base64.getEncoder().encodeToString(fileContent);
		return result;
	}

}
