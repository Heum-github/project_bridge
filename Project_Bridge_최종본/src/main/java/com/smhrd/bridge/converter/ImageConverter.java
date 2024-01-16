package com.smhrd.bridge.converter;

import java.io.IOException;

public interface ImageConverter<F, S>{
	// <> : 사용자가 필요할 때 형태를 지정할 수 있음.
	// F : 변환할 파일 / S : 변환 형태
	public S convert(F f) throws IOException;
}
