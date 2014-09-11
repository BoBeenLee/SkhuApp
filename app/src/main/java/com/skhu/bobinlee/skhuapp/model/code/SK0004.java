package com.skhu.bobinlee.skhuapp.model.code;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SK0004 {
	public static final int PARENT = 1;
	public static final int DEPTH = 2;
	
	@JsonProperty("_type")
	public int type;
	@JsonProperty("_db_type")
	public int dbType;
	@JsonProperty("_depth")
	public int depth;
	@JsonProperty("_parent_cate_no")
	public int parentCateNo;
	@JsonProperty("_res_cnt")
	public int resCnt;
	@JsonProperty("_res")
	public List<SK0004Category> res;
	@JsonProperty("_res_date")
	public String resDate;
	
	public static class SK0004Category {
		@JsonProperty("_cate_no")
		public int cateNo;
		@JsonProperty("_depth")
		public int depth;
		@JsonProperty("_name")
		public String name;
		@JsonProperty("_cate_id")
		public String cateId;
		@JsonProperty("_db_type")
		public int dbType;
		@JsonProperty("_parent_cate_no")
		public int parentCateNo;
	}
}
