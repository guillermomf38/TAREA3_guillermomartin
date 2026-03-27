/**
 *Clase NacionalidadService.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

@Service
public class NacionalidadService {
	private final Map<String, String> paises;

    public NacionalidadService() {
        this.paises = leerPaises();
    }

    private Map<String, String> leerPaises() {
        Map<String, String> paises = new LinkedHashMap<>();
        Properties prop = new Properties();

        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("ruta.properties")) {
            if (is == null) {
                System.out.println("No se encontro ruta.properties");
                return paises;
            }
            prop.load(is);
        } catch (IOException e) {
            System.out.println("Error leyendo ruta.properties");
            return paises;
        }

        String ruta = prop.getProperty("rutaPaisesxml");
        if (ruta == null) {
            System.out.println("No esta configurada la ruta del XML");
            return paises;
        }

        try {
            InputStream xmlIs = getClass().getClassLoader()
                    .getResourceAsStream(ruta);
            if (xmlIs == null) {
                System.out.println("No se encontro paises.xml en: " + ruta);
                return paises;
            }
            DocumentBuilder builder = DocumentBuilderFactory
                    .newInstance().newDocumentBuilder();
            Document doc = builder.parse(xmlIs);
            doc.getDocumentElement().normalize();

            NodeList lista = doc.getElementsByTagName("pais");
            for (int i = 0; i < lista.getLength(); i++) {
                Element pais = (Element) lista.item(i);
                String id = pais.getElementsByTagName("id")
                        .item(0).getTextContent();
                String nombre = pais.getElementsByTagName("nombre")
                        .item(0).getTextContent();
                paises.put(id.trim(), nombre.trim());
            }
        } catch (Exception e) {
            System.out.println("Error leyendo paises.xml");
        }

        return paises;
    }

    public boolean esValida(String nacionalidad) {
        return paises.containsValue(nacionalidad);
    }

    public Map<String, String> getPaises() {
        return paises;
    }
}
