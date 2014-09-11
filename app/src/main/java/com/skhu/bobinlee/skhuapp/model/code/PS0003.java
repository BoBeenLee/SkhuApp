package com.skhu.bobinlee.skhuapp.model.code;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PS0003 {
	@JsonProperty("_mac")
	public String mac;
	
	@JsonProperty("_res_cnt")
	public int resCnt;
	@JsonProperty("_res")
	public List<PS0003Alarm> res;
	@JsonProperty("_res_date")
	public String resDate;
	
	public static class PS0003Alarm {
		@JsonProperty("_alarm_no")
		public long alarmNo;
		@JsonProperty("_cate_no")
		public int cateNo;
		@JsonProperty("_cate_nm")
		public String cateNm;
		@JsonProperty("_src_no")
		public String srcNo;
		@JsonProperty("_filter")
		public String filter;
	}
}
