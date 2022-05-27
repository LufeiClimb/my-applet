package com.lufeiclimb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lufeiclimb.utils.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 我的基金的最新净值
 *
 * @author lufeixia
 * @version 1.0
 * @date 2020-4-16 09:54:43
 * @since 1.8
 */
@Api(value = "/jingzhi", tags = "基金净值")
@RestController
@RequestMapping("/jingzhi")
public class ZuiXinJingZhi {

    public static void main(String[] args) throws InterruptedException {
        ZuiXinJingZhi zuiXinJingZhi = new ZuiXinJingZhi();
        zuiXinJingZhi.test();
    }

    public void test() throws InterruptedException {
        ZuiXinJingZhi zuiXinJingZhi = new ZuiXinJingZhi();
        // System.out.println("净值");
        // zuiXinJingZhi.jingZhi();

        String[] codes = {"004753","050026","001595","009860","320007","002190","006060"};

        List<String> guzhi = new ArrayList<>();
        List<String> jingzhi = new ArrayList<>();
        for (int i = 0; i < codes.length; i++) {
            JSONObject result = zuiXinJingZhi.jiJinXinXi(codes[i]);
            System.out.println(result);
            jingzhi.add(result.getString("jingzhi"));
            guzhi.add(result.getString("guzhi"));
            Thread.sleep(100);
        }

        for (String s : jingzhi) {
            System.out.println(s);
        }
        System.out.println("----");
        for (String s : guzhi) {
            System.out.println(s);
        }

    }

    @GetMapping("/jiJinXinXi/{code}")
    @ApiOperation(value = "基金信息")
    public JSONObject jiJinXinXi(@PathVariable String code) {

        JSONObject result = new JSONObject();
        result.put("code", code);

        JSONObject param = new JSONObject();

        String cookie =
                "";
        String url = "https://www.howbuy.com/fund/" + code + "/";
        String formResult = HttpUtil.httpForm(url, param, cookie);

        Document totalDoc = Jsoup.parse(formResult);
        String name = totalDoc.getElementsByTag("h1").text().replace("(", "-").split("-")[0];
        result.put("name", name);

        String jingzhi = totalDoc.getElementsByClass("dRate").text();
        result.put("jingzhi", jingzhi);

        String date =
                totalDoc.getElementsByClass("b-0")
                        .get(0)
                        .text()
                        .replace("单位净值 [", "")
                        .replace("]", "");
        result.put("date", date);

        String zhangfu = totalDoc.getElementsByClass("b-rate").get(0).text();
        result.put("zhangfu", zhangfu);

        url = "https://www.howbuy.com/fund/ajax/gmfund/valuation/valuationnav.htm?jjdm="+code;
        String formResult1 = HttpUtil.httpForm(url, param, cookie);
        Document totalDoc1 = Jsoup.parse(formResult1);
        String guzhi = totalDoc1.getElementsByClass("con_value").text();
        result.put("guzhi", guzhi);
        String guzhiriqi = totalDoc1.getElementsByClass("tips_icon_con").text();
        result.put("guzhiriqi", guzhiriqi);

        return result;
    }

    @GetMapping("/jiJinJingZhi/{code}")
    @ApiOperation(value = "基金信息")
    public String jiJinJingZhi(@PathVariable String code) {

        JSONObject param = new JSONObject();

        String cookie = "";
        String url = "https://www.howbuy.com/fund/" + code + "/";
        String formResult = HttpUtil.httpForm(url, param, cookie);

        Document totalDoc = Jsoup.parse(formResult);

        return totalDoc.getElementsByClass("dRate").text();

    }

    @GetMapping("/jiJinGuZhi/{code}")
    @ApiOperation(value = "基金信息")
    public String jiJinGuZhi(@PathVariable String code) {

        JSONObject param = new JSONObject();

        String cookie = "";
        String url = "https://www.howbuy.com/fund/ajax/gmfund/valuation/valuationnav.htm?jjdm="+code;
        String formResult1 = HttpUtil.httpForm(url, param, cookie);
        Document totalDoc1 = Jsoup.parse(formResult1);
        return totalDoc1.getElementsByClass("con_value").text();

    }

    @GetMapping("/guZhi")
    @ApiOperation(value = "最新估值")
    public List<String> guZhi() {

        List<String> re = new ArrayList<>();
        JSONObject param = new JSONObject();
        param.put("jjdmstr", "110022,160632,161725,519933,000248,000961,001548,008173");
        String s =
                HttpUtil.httpForm(
                        "https://www.howbuy.com/fund/ajax/gmfund/fundnetestimatejson.htm",
                        param,
                        null);

        JSONArray jsonArray = JSONArray.parseArray(s);
        jsonArray.sort(
                (o1, o2) -> {
                    JSONObject a1 = (JSONObject) o1;
                    JSONObject a2 = (JSONObject) o2;
                    return a1.getString("code").compareTo(a2.getString("code"));
                });
        for (Object o : jsonArray) {
            JSONObject a = (JSONObject) o;
            // System.out.println(a.getString("jjjc"));
            System.out.println(a.getString("valuation"));
            re.add(a.getString("valuation"));
        }
        return re;
    }

    @GetMapping("/jingZhi")
    @ApiOperation(value = "最新净值")
    public List<String> jingZhi() {

        List<String> re = new ArrayList<>();
        JSONObject param = new JSONObject();
        // param.put("tabType", "2");
        String cookie =
                "__hutmz=268394641.1585122498.1.1.hutmcsr=(direct)|hutmccn=(direct)|hutmcmd=(none); __hutmmobile=F5C45DCC-405E-45DB-84EE-CFB365AAF197; _ga=GA1.2.1873930785.1585122498; selectedFeatureType=lx; USER_INFO_COOKIE=8013087048; USER_SALT_COOKIE=6cd638adfe6a18ee3f6ea1a41aee636d; FUNDID_COOKIE=COOKIE20200401000001205771; SESSION=10393d71-88e1-4ec2-8336-fcbdc39d1663; __hutmc=268394641; Hm_lvt_394e04be1e3004f9ae789345b827e8e2=1585122498,1585723127,1586327428; _hb_ref_pgid=11010; _hb_pgid=; welcome_fund_attention=true; GM_COMPARE_COOKIE=003741B519746B519933B008173; GM_VISIT_COOKIE=008173%2C519933%2C009058%2C001548%2C161725%2C003741%2C006147%2C006985; _gid=GA1.2.768083053.1587029184; __hutma=268394641.783161955.1585122498.1587089178.1587105163.22; __hutmb=268394641.1.10.1587105163; Hm_lpvt_394e04be1e3004f9ae789345b827e8e2=1587105164; OZ_SI_1497=sTime=1586327427&sIndex=613; OZ_1U_1497=vid=ve7b0cc121a2b8.0&ctime=1587105336&ltime=1587105333; OZ_1Y_1497=erefer=-&eurl=https%3A//www.howbuy.com/fundtool/filter.htm&etime=1586327427&ctime=1587105336&ltime=1587105333&compid=1497";
        String formResult =
                HttpUtil.httpForm(
                        "https://www.howbuy.com/fund/ajax/fundtool/findopenfund.htm",
                        param,
                        cookie);

        Document totalDoc = Jsoup.parse(formResult);
        Elements elements = totalDoc.getAllElements();
        String text = elements.text();
        text = text.replaceAll("[ 盈亏备注删除]", "");
        String[] splits = text.split("购买");
        Set<String> all = new TreeSet<>(Arrays.asList(splits));
        Set<String> a = new TreeSet<>();
        for (String split : all) {
            String temp = split.substring(0, split.indexOf("("));
            if (temp.contains(".")) {
                String daiMa = temp.substring(0, 6);
                String jingZhi = temp.substring(temp.lastIndexOf("-") + 1);
                a.add(daiMa + "-" + jingZhi);
                // System.out.println(daiMa + "-" + jingZhi);
            }
        }

        for (String s : a) {
            System.out.println(s.substring(s.lastIndexOf("-") + 1));
        }
        return re;
    }
}
