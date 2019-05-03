package com.spider;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class M3u8ParserTest extends BaseTest{

    @Test
    public void testM3u8(){
        analysisIndex(getIndexString("http://video.caomin5168.com:8091/20181127/0LzzDD0V/index.m3u8"),".*m3u8");
//        analysisIndex(getIndexString("http://video.caomin5168.com:8091/20181127/NBNAaPzt/1127kb/hls/index.m3u8"),".*ts");
    }

    public String getIndexString(String urlPath){
        try{
            URL url = new URL(urlPath);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(),"UTF-8"));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line).append("\n");
            }
            in.close();
            return content.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static List analysisIndex(String content,String ptr){
//        Pattern pattern = Pattern.compile(".*ts");
        Pattern pattern = Pattern.compile(ptr);
        Matcher ma = pattern.matcher(content);

        List<String> list = new ArrayList<String>();

        while(ma.find()){
            String s = ma.group();
            list.add(s);
            System.out.println(s);
        }
        return list;
    }
}
