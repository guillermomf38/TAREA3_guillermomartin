/**
 *Clase ExistDBService.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.services;




import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.modules.XMLResource;

@Service
public class ExistDBService {

    @Value("${existdb.url}")
    private String url;

    @Value("${existdb.user}")
    private String user;

    @Value("${existdb.password}")
    private String password;

    @Value("${existdb.collection}")
    private String collectionPath;
    
    private void registrarDriver() throws Exception {
    
    	    String driver = "org.exist.xmldb.DatabaseImpl";

    	    Class<?> cl = Class.forName(driver);

    	    Database database =
    	            (Database) cl.getDeclaredConstructor().newInstance();

    	    database.setProperty("create-database", "true");

    	    DatabaseManager.registerDatabase(database);
    	}
    
    
    public void guardarInforme(String nombreFichero, String contenidoXml)
            throws Exception {
        registrarDriver();

        Collection col = (Collection) DatabaseManager.getCollection(
                url + collectionPath, user, password);

        if (col == null) {
         
            Collection root = (Collection) DatabaseManager.getCollection(
                    url + "/db", user, password);
            org.xmldb.api.modules.CollectionManagementService cms =
                    (org.xmldb.api.modules.CollectionManagementService)
                    ((org.xmldb.api.base.Collection) root).getService("CollectionManagementService", "1.0");
            col = (Collection) cms.createCollection("informes");
        }

        XMLResource recurso = (XMLResource)
                ((org.xmldb.api.base.Collection) col).createResource(nombreFichero, "XMLResource");
        recurso.setContent(contenidoXml);
        ((org.xmldb.api.base.Collection) col).storeResource(recurso);
        ((org.xmldb.api.base.Collection) col).close();
    }
    
    
    public String obtenerInforme(String nombreFichero) throws Exception {
        registrarDriver();

        Collection col = (Collection) DatabaseManager.getCollection(
                url + collectionPath, user, password);
        if (col == null) return null;

        XMLResource recurso = (XMLResource)
                ((org.xmldb.api.base.Collection) col).getResource(nombreFichero);
        if (recurso == null) return null;

        String contenido = (String) recurso.getContent();
        ((org.xmldb.api.base.Collection) col).close();
        return contenido;
    }
}
