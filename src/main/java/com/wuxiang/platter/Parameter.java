package com.wuxiang.platter;

import org.apache.velocity.VelocityContext;

public class Parameter {
    private VelocityContext context = new VelocityContext();

    public Parameter put(String key, Object value) {
        context.put(key, value);
        return this;
    }

    public VelocityContext getContext() {
        return context;
    }
}
