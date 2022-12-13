package com.xc.utils;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

public class MainTask {
    //    https://fanyi-api.baidu.com/product/111
    static String url="http://api.fanyi.baidu.com/api/trans/vip/translate";

    static String appId="20220118001058388";

    static String miyao="ag31ETDvCP1YZYMy0xKK";

    public static void main(String[] args) throws IOException, InterruptedException {
        BufferedInputStream in = FileUtil.getInputStream("d:/en.txt");
        BufferedReader reader = IoUtil.getReader(in, Charset.defaultCharset());
        String text = null;

        while ((text = reader.readLine())!=null){
            if (!text.contains("{") && !text.contains("}") && StringUtils.isNotBlank(text)){
                TimeUnit.MILLISECONDS.sleep(1100);
                String s1 = RandomUtil.randomInt(10000000,999999999)+"";

                String wenben = StrUtil.sub(text, text.indexOf("\""), text.length()-2).replace("\"", "");
                String pre =text.split(":")[0];

                StringJoiner stringJoiner = new StringJoiner("&");
                stringJoiner.add("q="+wenben);
                stringJoiner.add("from=en");
                stringJoiner.add("to=cht");
                stringJoiner.add("appid="+appId);
                stringJoiner.add("salt="+s1 );

                String sign=appId+wenben+s1+miyao;
//                System.out.println(sign);
                Digester md5 = new Digester(DigestAlgorithm.MD5);
                String digestHex = md5.digestHex(sign);
//                System.out.println(url + "?" + stringJoiner + "&sign=" + digestHex);
                try{
                     String s = HttpUtil.get(url + "?" + stringJoiner + "&sign=" + digestHex);
//                System.out.println(text+"--"+s);

                    JSONObject jsonObject = JSONObject.parseObject(s);
                    JSONArray trans_result = jsonObject.getJSONArray("trans_result");
                    JSONObject o = trans_result.getJSONObject(0);
                    System.out.println(pre+" : \""+o.get("dst")+"\",");
                }catch (Exception e){

                    System.out.println(text);
                }

                continue;
            }
            System.out.println(text);
        }
    }

}