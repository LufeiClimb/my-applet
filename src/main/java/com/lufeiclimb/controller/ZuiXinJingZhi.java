package com.lufeiclimb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lufeiclimb.utils.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    public static void main(String[] args) {
        ZuiXinJingZhi zuiXinJingZhi = new ZuiXinJingZhi();
        zuiXinJingZhi.zuiXin();
    }

    @GetMapping("/zuiXin")
    @ApiOperation(value = "最新净值")
    public List<String> zuiXin(){

        List<String> re = new ArrayList<>();
        JSONObject param = new JSONObject();
        param.put("jjdmstr", "110022,160632,161725,519933,000248,000961,001548,008173");
        String s =
                HttpUtil.httpForm(
                        "https://www.howbuy.com/fund/ajax/gmfund/fundnetestimatejson.htm", param);

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
}
