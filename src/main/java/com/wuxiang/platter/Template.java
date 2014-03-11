package com.wuxiang.platter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Template {
    protected VelocityContext context = new VelocityContext();
    protected LinkedHashMap<String, Template> dynamicTemplateMap = new LinkedHashMap<String, Template>();
    protected ArrayList<String> keyNames = new ArrayList<String>();
    protected String templateString;

    static {
        Velocity.init();
    }

    public Template(String templateString) {
        this.templateString = templateString;
    }

    public ArrayList<String> getKeyNames() {
        return keyNames;
    }

    public Template put(String key, Object value) {
        for (Template template : dynamicTemplateMap.values()) {
            if (template == null)
                break;
            if (template.existKey(key))
                continue;
            template.put(key, value);
        }
        if (!existKey(key)) {
            context.put(key, value);
            keyNames.add(key);
        }
        return this;
    }

    public Template put(String key, Template template) {
        for (Template temp : dynamicTemplateMap.values()) {
            if (temp == null)
                break;
            if (temp.existKey(key))
                continue;
            temp.put(key, template);
        }
        if (!existKey(key)) {
            dynamicTemplateMap.put(key, template);
            keyNames.add(key);
        }
        return this;
    }

    private boolean existKey(String key) {
        for (int i = 0; i < keyNames.size(); i++) {
            if (keyNames.get(i).compareTo(key) == 0) {
                return true;
            }
        }
        return false;
    }

    public String mergeToString() {
        for (Map.Entry<String, Template> temp : dynamicTemplateMap.entrySet()) {
            if (temp == null)
                break;
            context.put(temp.getKey(), temp.getValue().mergeToString());
        }

        String content;
        StringWriter writer = new StringWriter();
        try {
            Velocity.evaluate(context, writer, "", templateString);
            writer.flush();
            content = writer.toString();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return content;
    }

}
