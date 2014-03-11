package com.wuxiang.platter.util;

import com.wuxiang.platter.Parameter;
import com.wuxiang.platter.Template;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.app.Velocity;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import java.io.*;

public class SOAPMessageFactory {

    public static SOAPMessage create(String templateName) throws SOAPException {
        return create(templateName, new Parameter());
    }

    public static SOAPMessage create(Template template) throws SOAPException {
        String soapContent = template.mergeToString();
        return getSoapMessage(soapContent);
    }

    private static SOAPMessage getSoapMessage(String content) throws SOAPException {
        MessageFactory messageFactory = MessageFactory.newInstance();
        ByteArrayInputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(content.getBytes(Charsets.UTF_8));
            SOAPMessage soapMessage = messageFactory.createMessage(new MimeHeaders(), inputStream);
            return soapMessage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static SOAPMessage create(String payloadName, Parameter context) throws SOAPException {
        String template = loadTemplate(payloadName);
        String soapContent = instantiate(template, context);
        return getSoapMessage(soapContent);
    }

    private static String loadTemplate(String payloadName) {
        String content;
        InputStream inputStream = null;
        try {
            inputStream = ClassLoader.getSystemResourceAsStream(payloadName);
            content = IOUtils.toString(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return content;
    }

    private static String instantiate(String template, Parameter parameter) {
        StringReader reader = new StringReader(template);
        StringWriter writer = new StringWriter();
        String soapContent;
        try {
            Velocity.evaluate(parameter.getContext(), writer, "", reader);
            writer.flush();
            soapContent = writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(writer);
        }
        return soapContent;
    }
}
