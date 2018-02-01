package me.lumen.shadowsocks.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.netty.bootstrap.Bootstrap;
import me.lumen.shadowsocks.model.Config;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by lumen on 18-1-13.
 */
public class FileUtil {
    public static String readFileToString(String filePath) throws IOException{
        String result = "";
        InputStreamReader inReader =
                new InputStreamReader(Bootstrap.class.getClassLoader().getResourceAsStream(filePath));
        BufferedReader bufferedReader = new BufferedReader(inReader);
        String tempStr = null;
        while ((tempStr = bufferedReader.readLine()) != null){
            result += tempStr;
        }
        return result;
    }

    public static void main(String[] args) throws IOException{
        String result = readFileToString("config.json");
        System.out.println(result);
        Config config = new GsonBuilder().create().fromJson(result, Config.class);
        System.out.println(config.toString());

    }

}
