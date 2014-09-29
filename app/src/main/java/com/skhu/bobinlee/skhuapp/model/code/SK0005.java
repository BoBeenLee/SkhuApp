package com.skhu.bobinlee.skhuapp.model.code;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SK0005 {
	@JsonProperty("_res_cnt")
	public int resCnt;
	@JsonProperty("_res")
	public List<SK0005Food> res;
	@JsonProperty("_res_date")
	public String resDate;
	
	public static class SK0005Food {
		@JsonProperty("_menu_date")
		public String menuDate;
		@JsonProperty("_lunch")
		public String lunch;
		@JsonProperty("_special_lunch")
		public String specialLunch;
		@JsonProperty("_dinner")
		public String dinner;
	}
}
