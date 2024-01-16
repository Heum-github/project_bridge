package com.smhrd.bridge.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchingCosainDTO {
	private long user_song_idx;
	private String user_id;
	private boolean noise_cancel;
	private String song_file;
	private String user_song_name;
	private String user_singer;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate created_at;
	private double tempo;
	private double wave;
	private double chroma_stft_mean;
	private double rmse_mean;
	private double spec_cent_mean;
	private double spec_bw_mean;
	private double rolloff_mean;
	private double zcr_mean;
	private double harmony_mean;
	private double mfcc_mean;
	private double cosineSimilarity;
}
