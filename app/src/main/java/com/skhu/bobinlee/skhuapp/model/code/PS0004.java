package com.skhu.bobinlee.skhuapp.model.code;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PS0004 {
	@JsonProperty("_mac")
	public String mac;
	@JsonProperty("_cate_no")
	public int cateNo;
	@JsonProperty("_src_no")
	public String srcNo;
	@JsonProperty("_filter")
	public String filter;
	
	@JsonProperty("_rslt_yn")
	public String resultYn;
}
