package com.wuxiang.platter;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TemplateTest {
    @Test
    public void should_merge_content_by_single_string_template() throws Exception {
        Template template = new Template("<StringTemplate>$param</StringTemplate>");
        template.put("param", "hello wuxiang");

        String content = template.mergeToString();

        XPathEvaluator xpathEvaluator = new XPathEvaluator(content);
        assertThat(xpathEvaluator.getValueByXPath("/StringTemplate"), is("hello wuxiang"));
    }

    @Test
    public void should_merge_content_by_multiple_string_templates() throws Exception {
        Template template1 = new Template("<StringTemplate>$p1</StringTemplate>");
        Template template2 = new Template("<Template2>$p2</Template2>");
        template1.put("p1", template2);
        template1.put("p2", "hello wuxiang");

        String content = template1.mergeToString();

        XPathEvaluator xpathEvaluator = new XPathEvaluator(content);
        assertThat(xpathEvaluator.getValueByXPath("/StringTemplate/Template2"), is("hello wuxiang"));
    }

    @Test
    public void should_merge_content_by_string_template_and_file_template() throws Exception {
        DynamicTemplate dynamicTemplate1 = new DynamicTemplate("com/wuxiang/platter/templates/template1.xml");
        DynamicTemplate dynamicTemplate2 = new DynamicTemplate("com/wuxiang/platter/templates/template2.xml");
        DynamicTemplate dynamicTemplate3 = new DynamicTemplate("com/wuxiang/platter/templates/template3.xml");
        Template template = new Template("<StringTemplate>$param</StringTemplate>");
        template.put("param", dynamicTemplate1);
        template.put("template2", dynamicTemplate2).put("template3", dynamicTemplate3);
        template.put("p1", "template1").put("p2", "template2").put("p3", "template3");

        String content = template.mergeToString();

        XPathEvaluator xpathEvaluator = new XPathEvaluator(content);
        assertThat(xpathEvaluator.getValueByXPath("/StringTemplate/Envelope/Body/Content1"), is("template1"));
        assertThat(xpathEvaluator.getValueByXPath("/StringTemplate/Envelope/Body/Content2/Template2"), is("template2"));
        assertThat(xpathEvaluator.getValueByXPath("/StringTemplate/Envelope/Body/Content3/Template3"), is("template3"));
    }

}
