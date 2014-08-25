package com.skhu.bobinlee.skhuapp.config;

import com.skhu.bobinlee.skhuapp.model.data.Skhu;

import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by BoBinLee on 2014-08-25.
 */
public class SkhuConfig {
    public final static String SKHU_URL = "http://www.skhu.ac.kr";

    public static Skhu getArticle(String url) throws IOException {
        Connection con = HttpConnection.connect(url);

        Document doc = con.get();

        Element content = doc.getElementById("cont");
        Elements tHead = content.getElementsByTag("thead");
        Elements tBody = content.getElementsByTag("tbody");
        Elements trs = tBody.get(0).getElementsByTag("tr");

        Skhu skhu = new Skhu();
        // head
        skhu.title = tHead.get(0).getElementsByTag("td").get(0).getElementsByTag("span").html();

        // body
        Elements td1 = tBody.get(0).getElementsByTag("tr").get(0).getElementsByTag("td");

        skhu.createdDate = td1.get(0).getElementsByTag("span").html();
        skhu.writer = td1.get(1).getElementsByTag("span").html();
//		System.out.println(tBody.get(0).getElementsByTag("tr").get(1).html());

        skhu.content = tBody.get(0).getElementsByTag("tr").get(2).getElementsByTag("td").get(0).getElementsByTag("span").html();
//        System.out.println(skhu.title + " - " + skhu.createdDate + " - " + skhu.writer);
        return skhu;
    }

    // 여기서 href 찾고 각각의 url에서 정보를 추출
    public static List<String> exportArticleUrls(int pageNum, int id) throws IOException {
        ArrayList<String> articles = new ArrayList<String>();
        Connection con = HttpConnection.connect(SKHU_URL + "/board/boardlist.aspx?curpage=" + pageNum + "&bsid=" + id + "&searchBun=51");
        // http://www.skhu.ac.kr/board/boardread.aspx?idx=22311&curpage=1&bsid=10004&searchBun=51
        Document doc = con.get();

        Element content = doc.getElementById("cont");
        Elements tBody = content.getElementsByTag("tbody");
        Elements trs = tBody.get(0).getElementsByTag("tr");

        for(int i=0, length=trs.size(); i<length; i++){
            Element tr = trs.get(i);
            articles.add(SKHU_URL + "/board/" + tr.getElementsByTag("td").get(1).getElementsByTag("a").get(0).attr("href"));
        }
        return articles;
    }
}
