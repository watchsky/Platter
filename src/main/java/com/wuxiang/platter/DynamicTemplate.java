package com.wuxiang.platter;


import org.apache.commons.io.IOUtils;

import java.io.InputStream;

public class DynamicTemplate extends Template {

    public DynamicTemplate(String templateName) {
        super(loadTemplate(templateName));
    }

    private static String loadTemplate(String payloadName) {
        String templateString;
        InputStream inputStream = null;
        try {
            inputStream = ClassLoader.getSystemResourceAsStream(payloadName);
            templateString = IOUtils.toString(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return templateString;
    }

}
