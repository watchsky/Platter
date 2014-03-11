package com.wuxiang.platter.util;

import com.wuxiang.platter.Parameter;
import com.wuxiang.platter.XPathEvaluator;
import org.junit.Test;

import javax.xml.soap.SOAPMessage;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class SOAPMessageFactoryTest {
    @Test
    public void should_create_soapMessage_by_template_name() throws Exception {
        SOAPMessage soapMessage = SOAPMessageFactory.create("com/wuxiang/platter/templates/template1.xml");

        XPathEvaluator xPathEvaluator = new XPathEvaluator(soapMessage);
        assertThat(xPathEvaluator.getValueByXPath("/Envelope/Header"), is("Template1"));
        assertThat(xPathEvaluator.getValueByXPath("/Envelope/Body/Content1"), is("$p1"));
    }

    @Test
    public void should_create_soapMessage_by_context() throws Exception {
        Parameter context = new Parameter();
        context.put("p1", "template1").put("template2", "template2").put("template3", "template3");

        SOAPMessage soapMessage = SOAPMessageFactory.create("com/wuxiang/platter/templates/template1.xml", context);

        XPathEvaluator xPathEvaluator = new XPathEvaluator(soapMessage);
        assertThat(xPathEvaluator.getValueByXPath("/Envelope/Body/Content1"), is("template1"));
        assertThat(xPathEvaluator.getValueByXPath("/Envelope/Body/Content2"), is("template2"));
        assertThat(xPathEvaluator.getValueByXPath("/Envelope/Body/Content3"), is("template3"));
    }
}
