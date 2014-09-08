package com.skhu.bobinlee.skhuapp.model.code;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PS0001 {
	@JsonProperty("_mac")
	public String mac;
	@JsonProperty("_push_token_id")
	public String pushTokenId;
	@JsonProperty("_push_yn")
	public String pushYn;
	@JsonProperty("_rslt_yn")
	public String resultYn;
}
