package com.skhu.bobinlee.skhuapp.model.code;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PS0005 {
	@JsonProperty("_mac")
	public String mac;
	@JsonProperty("_alarm_no")
	public long alarmNo;
	
	@JsonProperty("_rslt_yn")
	public String resultYn;
}
