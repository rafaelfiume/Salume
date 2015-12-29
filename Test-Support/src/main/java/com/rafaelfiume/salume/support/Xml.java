package com.rafaelfiume.salume.support;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import static javax.xml.xpath.XPathConstants.STRING;

public final class Xml {

    private Xml() {
        // not intended to be instantiated
    }

    public static Document xmlFrom(String xml) {
        try {
            final Document xmlDoc = DocumentBuilderFactory
                    .newInstance()
                    .newDocumentBuilder()
                    .parse(new InputSource(new StringReader(xml)));

            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(new DOMSource(xmlDoc), new DOMResult());
            return xmlDoc;

        } catch (SAXException | IOException | ParserConfigurationException | TransformerException e) {
            throw new TestSetupException(e);
        }
    }

    public static String getValueFrom(Node item, String xpath) {
        try {
            return (String) xpath().evaluate(xpath + "/text()", item, STRING);
        } catch (XPathExpressionException e) {
            throw new TestSetupException(e);
        }
    }

    public static XPath xpath() {
        return XPathFactory.newInstance().newXPath();
    }

    public static String prettyPrint(Node xml) {
        try {
            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            final Writer out = new StringWriter();
            transformer.transform(new DOMSource(xml), new StreamResult(out));
            return out.toString();

        } catch (TransformerException e) {
            throw new TestSetupException(e);
        }
    }

}
