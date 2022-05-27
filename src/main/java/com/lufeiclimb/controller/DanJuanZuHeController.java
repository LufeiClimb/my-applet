package com.lufeiclimb.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.lufeiclimb.utils.HttpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/danjuan")
@Api(value = "/danjuan", tags = "蛋卷")
public class DanJuanZuHeController {

    static final String cookie = "acw_tc=2760820416472216959166480e2587f7efc9674d88501640b80ca89edce72d; channel=1300100141;device_id=web_H1OTsG2W5; xq_a_token=c569d2189ddf9325cd8733b8d0863a9ef4f7d235; trdipcktrffcext=1; xq-dj-token=; accesstoken=240010000a3f79c9028154f89ec46d470c038728d1e6f2402; uid=579527679; u=579527679; refreshtoken=240010000a671b688ba2b21707f82e8e566fcb792771ea34e; timestamp=1647221750437";

    public static void main(String[] args) throws InterruptedException {
        DanJuanZuHeController controller = new DanJuanZuHeController();
        // controller.test();
        // System.out.println(controller.jiJinXinXi("004753,050026,001595,009860,320007,002190,006060"));
        JSONArray csi1019 = null;
        try {
            csi1019 = controller.zuhe("CSI1019");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(csi1019);
    }

    @GetMapping("/zuhe")
    @ApiOperation(value = "蛋卷")
    public JSONArray zuhe(String code) throws IOException {
        JSONArray result = new JSONArray();

        String[] split = code.split(",");
        for (String code1 : split) {

            String url = "https://danjuanapp.com/djapi/holding/plan/" + code1;
            JSONObject param = new JSONObject();
            Map<String, String> headers = new HashMap<>();
            headers.put("Cookie", cookie);
            String formResult1 = HttpUtil.httpsGet(url, param, headers);

            result = JSONObject.parseObject(formResult1).getJSONObject("data").getJSONArray("items");
            url = "https://danjuanapp.com/djapi/fund/estimate-nav/";
            for (Object o : result) {
                JSONObject o1 = (JSONObject) o;
                String fd_code = o1.getString("fd_code");
                formResult1 = HttpUtil.httpsGet(url + fd_code, param, headers);
                JSONArray a1 = JSONObject.parseObject(formResult1).getJSONObject("data").getJSONArray("items");
                System.out.println(o1.getString("fd_name"));
                if (a1.size() > 0) {
                    JSONObject o2 = (JSONObject) a1.get(a1.size() - 1);
                    ((JSONObject) o).put("guzhi", o2.getString("nav"));
                }
            }

            return result;
        }
        return null;
    }
}