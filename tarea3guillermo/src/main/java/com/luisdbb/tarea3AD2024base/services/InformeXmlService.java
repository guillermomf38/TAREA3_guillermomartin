/**
 * Clase InformeXmlService.java
 * 
 * @author Guillermo Martin Fueyo
 * @version 1.0
 */

package com.luisdbb.tarea3AD2024base.services;

import java.io.File;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.luisdbb.tarea3AD2024base.modelo.Artista;
import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;
import com.luisdbb.tarea3AD2024base.modelo.Numero;

@Service
public class InformeXmlService {
	
	 private static final DateTimeFormatter FMT_FECHA =
	            DateTimeFormatter.ofPattern("dd-MM-yyyy");
	    private static final DateTimeFormatter FMT_FECHAHORA =
	            DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

	  
	    public String generarXml(Espectaculo espectaculo,
	                              List<Numero> numeros) throws Exception {

	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.newDocument();

	      
	        Element informe = doc.createElement("informe");
	        doc.appendChild(informe);

	        
	        Element fechahora = doc.createElement("fechahora");
	        fechahora.setTextContent(
	                LocalDateTime.now().format(FMT_FECHAHORA));
	        informe.appendChild(fechahora);

	        
	        Element espElem = doc.createElement("espectaculo");
	        informe.appendChild(espElem);

	        agregarElemento(doc, espElem, "id",
	                String.valueOf(espectaculo.getId()));
	        agregarElemento(doc, espElem, "nombre",
	                espectaculo.getNombre());
	        agregarElemento(doc, espElem, "fechaini",
	                espectaculo.getFechaini().format(FMT_FECHA));
	        agregarElemento(doc, espElem, "fechafin",
	                espectaculo.getFechafin().format(FMT_FECHA));

	       
	        if (espectaculo.getCoordinador() != null) {
	            Element coord = doc.createElement("coordinacion");
	            espElem.appendChild(coord);
	            agregarElemento(doc, coord, "nombre",
	                    espectaculo.getCoordinador().getNombre());
	            agregarElemento(doc, coord, "email",
	                    espectaculo.getCoordinador().getEmail());
	            agregarElemento(doc, coord, "senior",
	                    String.valueOf(espectaculo.getCoordinador().isSenior()));
	        }

	       
	        Element numerosElem = doc.createElement("numeros");
	        espElem.appendChild(numerosElem);

	        for (Numero n : numeros) {
	            Element numElem = doc.createElement("numero");
	            numerosElem.appendChild(numElem);

	            agregarElemento(doc, numElem, "orden",
	                    String.valueOf(n.getOrden()));
	            agregarElemento(doc, numElem, "nombre", n.getNombre());
	            agregarElemento(doc, numElem, "duracion",
	                    String.valueOf(n.getDuracion()));

	           
	            Element artistas = doc.createElement("artistas");
	            numElem.appendChild(artistas);

	            for (Artista a : n.getArtistas()) {
	                Element artElem = doc.createElement("artista");
	                artistas.appendChild(artElem);

	                agregarElemento(doc, artElem, "nombre", a.getNombre());
	                agregarElemento(doc, artElem, "nacionalidad",
	                        a.getNacionalidad());
	                agregarElemento(doc, artElem, "email", a.getEmail());

	                
	                String especialidades = a.getEspecialidades().toString()
	                        .replace("[", "").replace("]", "");
	                agregarElemento(doc, artElem, "especialidades",
	                        especialidades);

	               
	                if (a.getApodo() != null && !a.getApodo().isBlank()) {
	                    agregarElemento(doc, artElem, "apodo", a.getApodo());
	                }
	            }
	        }

	        
	        return documentoAString(doc);
	    }

	    
	    public void guardarEnFichero(String nombreFichero,
	                                  String contenidoXml) throws Exception {
	        File carpeta = new File("ficheros");
	        if (!carpeta.exists()) {
	            carpeta.mkdirs();
	        }
	        File fichero = new File("ficheros/" + nombreFichero);

	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer = tf.newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(
	                "{http://xml.apache.org/xslt}indent-amount", "4");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

	      
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document doc = builder.parse(
	                new java.io.ByteArrayInputStream(
	                        contenidoXml.getBytes("UTF-8")));

	        transformer.transform(new DOMSource(doc),
	                new StreamResult(fichero));
	    }

	 
	    private String documentoAString(Document doc) throws Exception {
	        TransformerFactory tf = TransformerFactory.newInstance();
	        Transformer transformer = tf.newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.setOutputProperty(
	                "{http://xml.apache.org/xslt}indent-amount", "4");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

	        StringWriter writer = new StringWriter();
	        transformer.transform(new DOMSource(doc),
	                new StreamResult(writer));
	        return writer.toString();
	    }

	    
	    private void agregarElemento(Document doc, Element padre,
	                                  String nombre, String valor) {
	        Element elem = doc.createElement(nombre);
	        elem.setTextContent(valor);
	        padre.appendChild(elem);
	    }
}

