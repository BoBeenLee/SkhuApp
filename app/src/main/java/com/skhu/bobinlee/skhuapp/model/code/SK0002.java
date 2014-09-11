package com.skhu.bobinlee.skhuapp.model.code;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SK0002 {
    @JsonProperty("_cate_no")
    public int cateNo;
	@JsonProperty("_order_type")
	public int orderType;
	@JsonProperty("_req_po_cnt")
	public int reqPoCnt;
	@JsonProperty("_req_po_no")
	public String reqPoNo;

	@JsonProperty("_res_cnt")
	public int resCnt;
	@JsonProperty("_res")
	public List<SK0002Article> res;
	@JsonProperty("_res_date")
	public String resDate;
	@JsonProperty("_res_last_no")
	public String resLastNo;

	public static class SK0002Article {
		@JsonProperty("_brd_no")
		public String brdNo;
		@JsonProperty("_brd_nm")
		public String brdNm;
		@JsonProperty("_brd_id")
		public String brdId;
		@JsonProperty("_brd_subject")
		public String brdSubject;
		@JsonProperty("_brd_url")
		public String brdUrl;
		@JsonProperty("_brd_content")
		public byte[] brdContent;
		@JsonProperty("_brd_cate_nm")
		public String brdCateNm;
		@JsonProperty("_brd_cate")
		public int brdCate;
		@JsonProperty("_brd_created")
		public String brdCreated;
		@JsonProperty("_brd_share_cnt")
		public String brdShareCnt;
		@JsonProperty("_brd_like_cnt")
		public String brdLikeCnt;
		@JsonProperty("_brd_comment_cnt")
		public int brdCommentCnt;
        @JsonProperty("_brd_img")
        public String brdImgUrl;
	}
}
