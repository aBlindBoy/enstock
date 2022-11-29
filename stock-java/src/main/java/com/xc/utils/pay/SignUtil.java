package com.xc.utils.pay;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import org.springframework.util.DigestUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.*;

public class SignUtil {
    /**
     * 生成签名
     * @param map 參數map
     * @param appKey key
     * @return string result
     */
    public static String getSign(Map<String, Object> map,String appKey) {
        String result = "";
        try {
            List<Map.Entry<String, Object>> infoIds = new ArrayList<Map.Entry<String, Object>>(map.entrySet());
            // 对所有传入參數按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, Object>>() {
                public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                    return ((String) o1.getKey()).compareToIgnoreCase((String) o2.getKey());
                }
            });
            // 构造签名键值对的格式
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> item : infoIds) {
                if (item.getKey() != null || item.getKey() != "") {
                    String key = item.getKey();
                    String val = "";
                    if(item.getValue()!=null){
                        val=item.getValue().toString();
                    }
                    if (!(val == "" || val == null)) {
                        sb.append(key + "=" + val + "&");
                    }
                }
            }
            result = sb.toString();
            result+="key="+appKey;
            //进行MD5加密
            result = DigestUtils.md5DigestAsHex(result.getBytes("UTF-8")).toUpperCase();
        } catch (Exception e) {
            return null;
        }
        return result;
    }

    public static void main(String[] args) {
        String str="{income=null, payOrderId=P0138382210501111031851, amount=null, mchId=2030, productId=8000, mchOrderNo=C1619838629823721, paySuccTime='', channelOrderNo='', backType=2, param1=test, param2=2222, appId=7059ea2ffdae4f7387674d9d0a5d33d7, channelAttach='', status=2}";
        Gson gson = new Gson();
         Map<String, Object> map = new HashMap<String, Object>();
         map = gson.fromJson(str, map.getClass());
        System.out.println(getSign(map,"94ST6ZIXTBQXAFW0DSTEB1IRAPMZVSMTJI4I6LW7SOHSESRJHC4NJRPHAZZNXRB4VSVMVGFD0DP578Q6CTIIDDZWHNPAJBM5JFOFGQ5SEC2OSU84JRZCCDZSKZ5IJTSH"));
    }
}
