/**
 *Clase MenuRegistrarPersona.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package com.luisdbb.tarea3AD2024base.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.services.CredencialesService;

import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;


@Controller
public class MenuRegistrarPersonaController implements Initializable {

    @FXML
    private Button btnIrRegistrarArtista;

    @FXML
    private Button btnIrRegistrarCoordinador;

    @FXML
    private Button btnCerrarSesion;

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private CredencialesService credencialesService;



    @FXML
    private void irRegistrarArtista(ActionEvent event)  {
        stageManager.switchScene(FxmlView.REGISTRAR_ARTISTA);
    }

    @FXML
    private void irRegistrarCoordinador(ActionEvent event)  {
        stageManager.switchScene(FxmlView.REGISTRAR_COORDINADOR);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void cerrarSesion(ActionEvent event) throws IOException {
        credencialesService.logout();
        stageManager.switchScene(FxmlView.LOGIN);
    }
}