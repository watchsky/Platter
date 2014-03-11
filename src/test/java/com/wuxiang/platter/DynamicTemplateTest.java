package com.wuxiang.platter;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class DynamicTemplateTest {
    @Test
    public void should_merge_content_by_single_file_template() throws Exception {
        DynamicTemplate dynamicTemplate1 = new DynamicTemplate("com/wuxiang/platter/templates/template2.xml");
        dynamicTemplate1.put("p2", "template");

        String content = dynamicTemplate1.mergeToString();

        XPathEvaluator xpathEvaluator = new XPathEvaluator(content);
        assertThat(xpathEvaluator.getValueByXPath("/Template2"), is("template"));
    }

    @Test
    public void should_merge_content_by_multiple_file_template() throws Exception {
        DynamicTemplate dynamicTemplate1 = new DynamicTemplate("com/wuxiang/platter/templates/template1.xml");
        DynamicTemplate dynamicTemplate2 = new DynamicTemplate("com/wuxiang/platter/templates/template2.xml");
        DynamicTemplate dynamicTemplate3 = new DynamicTemplate("com/wuxiang/platter/templates/template3.xml");
        dynamicTemplate1.put("template2", dynamicTemplate2).put("template3", dynamicTemplate3);
        dynamicTemplate1.put("p1", "template1").put("p2", "template2").put("p3", "template3");

        String content = dynamicTemplate1.mergeToString();

        XPathEvaluator xpathEvaluator = new XPathEvaluator(content);
        assertThat(xpathEvaluator.getValueByXPath("/Envelope/Body/Content1"), is("template1"));
        assertThat(xpathEvaluator.getValueByXPath("/Envelope/Body/Content2/Template2"), is("template2"));
        assertThat(xpathEvaluator.getValueByXPath("/Envelope/Body/Content3/Template3"), is("template3"));
    }

    @Test
    public void should_merge_content_by_file_template_and_string_template() throws Exception {
        DynamicTemplate dynamicTemplate1 = new DynamicTemplate("com/wuxiang/platter/templates/template1.xml");
        DynamicTemplate dynamicTemplate2 = new DynamicTemplate("com/wuxiang/platter/templates/template2.xml");
        DynamicTemplate dynamicTemplate3 = new DynamicTemplate("com/wuxiang/platter/templates/template3.xml");
        Template template = new Template("<StringTemplate>$p4</StringTemplate>");
        dynamicTemplate1.put("template2", dynamicTemplate2).put("template3", dynamicTemplate3).put("p1", template);
        dynamicTemplate1.put("p2", "template2").put("p3", "template3").put("p4", "stringtemplate");

        String content = dynamicTemplate1.mergeToString();

        XPathEvaluator xpathEvaluator = new XPathEvaluator(content);
        assertThat(xpathEvaluator.getValueByXPath("/Envelope/Body/Content1/StringTemplate"), is("stringtemplate"));
        assertThat(xpathEvaluator.getValueByXPath("/Envelope/Body/Content2/Template2"), is("template2"));
        assertThat(xpathEvaluator.getValueByXPath("/Envelope/Body/Content3/Template3"), is("template3"));
    }

}
