package org.apdplat.data.generator.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ysc on 18/04/2018.
 */
public class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
    private static final Map<String, String> CONFIG = new HashMap<>();

    static {
        Path path = Paths.get("config.txt");
        if (!Files.exists(path)){
            path = Paths.get("src","main", "resources","config.txt");
        }
        if(Files.exists(path)){
            LOGGER.info("存在配置文件");
            try {
                Files.readAllLines(Paths.get("config.txt")).forEach(line -> {
                    line = line.trim();
                    if("".equals(line) || line.startsWith("#")){
                        return;
                    }
                    int index = line.indexOf("=");
                    if(index==-1){
                        LOGGER.error("error config:"+line);
                        return;
                    }
                    //有K V
                    if(index>0 && line.length()>index+1) {
                        String key = line.substring(0, index).trim();
                        String value = line.substring(index + 1, line.length()).trim();
                        CONFIG.put(key, value);
                    }
                    //有K无V
                    else if(index>0 && line.length()==index+1) {
                        String key = line.substring(0, index).trim();
                        CONFIG.put(key, "");
                    }else{
                        LOGGER.error("error config:"+line);
                    }
                });
            }catch (Exception e){
                LOGGER.error("读取配置文件异常", e);
            }
        }else {
            throw new RuntimeException(path.toString()+" 不存在");
        }
    }

    public static String getStringValue(String key){
        return CONFIG.get(key);
    }

    public static int getIntValue(String key){
        String value = CONFIG.get(key);
        if(value == null){
            return -1;
        }
        return Integer.parseInt(value);
    }
}
