package com.lx.framework.tool.utils.yaml;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.IOException;


/**
 * @author xin.liu
 * @description 对象与yaml字符串互转工具
 * @date 2023-07-05  09:53
 * @Version 1.0
 */
public class YamlUtil {


    /**
     * @description 将yaml字符串转成类对象
     * @param yamlStr 
     * @param clazz
     * @return: T
     * @author xin.liu
     * @date 2023/7/5 10:04
     */
    public static <T> T toObject(String yamlStr, Class<T> clazz){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        mapper.findAndRegisterModules();
        try {
            return mapper.readValue(yamlStr, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * @description 将类对象转yaml字符串
     * @param object
     * @return: java.lang.String
     * @author xin.liu
     * @date 2023/7/5 10:05
     */
    public static String toYaml(Object object){
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER)
                                                                .enable(YAMLGenerator.Feature.INDENT_ARRAYS_WITH_INDICATOR)
                                                                .enable(YAMLGenerator.Feature.USE_PLATFORM_LINE_BREAKS)
        );
        mapper.findAndRegisterModules();
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



    /**
     * @description json 字符串转
     * @param jsonStr
     * @return: java.lang.String
     * @author xin.liu
     * @date 2023/7/5 10:05
     */
    public static String json2Yaml(String jsonStr) throws JsonProcessingException {
        JsonNode jsonNode = new ObjectMapper().readTree(jsonStr);
        return new YAMLMapper().writeValueAsString(jsonNode);
    }

}
