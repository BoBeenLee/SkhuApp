package com.skhu.bobinlee.skhuapp.model.code;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SK0006 {
	@JsonProperty("_year")
	public int year;
	@JsonProperty("_month")
	public int month;
	
	@JsonProperty("_res_cnt")
	public int resCnt;
	@JsonProperty("_res")
	public List<SK0006Calendar> res;
	@JsonProperty("_res_date")
	public String resDate;
	
	public static class SK0006Calendar {
		@JsonProperty("_sdate")
		public Timestamp sDate;
		@JsonProperty("_ldate")
		public Timestamp lDate;
		@JsonProperty("_cal_content")
		public String content;
	}
}
