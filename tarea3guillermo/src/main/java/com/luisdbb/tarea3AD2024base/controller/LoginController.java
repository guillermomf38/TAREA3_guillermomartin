/**
 *Clase LoginController.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.excepciones.ValidacionExcepcion;
import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;
import com.luisdbb.tarea3AD2024base.modelo.Perfiles;
import com.luisdbb.tarea3AD2024base.services.CredencialesService;
import com.luisdbb.tarea3AD2024base.services.EspectaculoService;
import com.luisdbb.tarea3AD2024base.services.SesionService;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@Controller
public class LoginController implements Initializable {

    @FXML
    private ListView<String> lvEspectaculos;
    
    @FXML
    private TextField txtUsuario;
    
    @FXML 
    private PasswordField psPassword;
    @FXML private Button btnInicioSesion;
    @FXML private Button btnReiniciar;
    

    @Autowired
    private EspectaculoService espectaculoService;

    @Autowired
    private CredencialesService credencialesService;

    @Autowired
    private SesionService sesionService;
    @Lazy
    @Autowired
    private StageManager stageManager;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarEspectaculos();
       
    }
    @FXML
    private void iniciarSesion() {
        try {
            credencialesService.login(txtUsuario.getText(), psPassword.getText());
            Perfiles perfil = sesionService.getUsuarioActual().getPerfil();
            switch (perfil) {
                case ADMIN        -> stageManager.switchScene(FxmlView.MENU_ADMIN);
                case COORDINACION -> stageManager.switchScene(FxmlView.MENU_COORDINADOR);
                case ARTISTA      -> stageManager.switchScene(FxmlView.MENU_ARTISTA);
            }
        } catch (ValidacionExcepcion e) {
            mostrarError(e.getMessage());
        }
    }
    
    @FXML
    private void reiniciar() {
        txtUsuario.clear();
        psPassword.clear();
    }
    private void cargarEspectaculos() {
        List<Espectaculo> espectaculos = espectaculoService.listarEspectaculos();
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Espectaculo e : espectaculos) {
            items.add(e.getId() + " | " + e.getNombre()
                    + " | " + e.getFechaini()
                    + " → " + e.getFechafin());
        }
        lvEspectaculos.setItems(items);
    }



    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de autenticación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

}
