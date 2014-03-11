package com.wuxiang.platter;


import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

public class XPathEvaluator {
    private final XPath xpath = XPathFactory.newInstance().newXPath();
    private final Document document;

    public XPathEvaluator(String content) {
        this.document = createUnawareNamespaceDocument(content);
    }

    private Document createUnawareNamespaceDocument(String content) {
        Document doc;
        InputStream inputStream = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            inputStream = new ByteArrayInputStream(content.getBytes());
            doc = factory.newDocumentBuilder().parse(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return doc;
    }

    public XPathEvaluator(SOAPMessage soapMessage) {
        this.document = createUnawareNamespaceDoc(soapMessage.getSOAPPart());
    }

    private Document createUnawareNamespaceDoc(Node node) {
        Document doc;
        InputStream inputStream = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(false);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            StringWriter strWriter = new StringWriter();
            transformer.transform(new DOMSource(node), new StreamResult(strWriter));
            String content = strWriter.toString();
            inputStream = new ByteArrayInputStream(content.getBytes());
            doc = factory.newDocumentBuilder().parse(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        return doc;
    }

    public String getValueByXPath(String xpathText) {
        try {
            String value = xpath.compile(xpathText).evaluate(document);
            return value;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }

    public Object evaluate(String xpathText, QName qname) {
        try {
            Object value = xpath.compile(xpathText).evaluate(document, qname);
            return value;
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
