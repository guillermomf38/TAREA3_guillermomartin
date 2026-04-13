/**
 *Clase BuscarEspectaculoController.java
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

import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;

import com.luisdbb.tarea3AD2024base.services.EspectaculoService;
import com.luisdbb.tarea3AD2024base.services.SesionService;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;



@Controller
public class BuscarEspectaculoController implements Initializable {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private EspectaculoService espectaculoService;

    @Autowired
    private SesionService sesionService;

    @FXML private ListView<String> lvEspectaculos;
    @FXML private Label lblNombre;
    @FXML private Label lblFechaini;
    @FXML private Label lblFechafin;
   

    private List<Espectaculo> espectaculos;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarEspectaculos();


        lvEspectaculos.getSelectionModel().selectedIndexProperty()
                .addListener((obs, o, n) -> {
                    int idx = n.intValue();
                    if (idx >= 0 && idx < espectaculos.size()) {
                        mostrarDetalle(espectaculos.get(idx));
                    }
                });
    }

    private void cargarEspectaculos() {
        espectaculos = espectaculoService.listarEspectaculos();
        ObservableList<String> items = FXCollections.observableArrayList();
        for (Espectaculo e : espectaculos) {
            items.add(e.getId() + " | " + e.getNombre()
                    + " | " + e.getFechaini()
                    + " → " + e.getFechafin());
        }
        lvEspectaculos.setItems(items);
    }

    private void mostrarDetalle(Espectaculo e) {

        if (e == null) return;

        lblNombre.setText(e.getNombre() != null ? e.getNombre() : "-");
        lblFechaini.setText(e.getFechaini() != null ? e.getFechaini().toString() : "-");
        lblFechafin.setText(e.getFechafin() != null ? e.getFechafin().toString() : "-");
    }

    @FXML
    private void atras() {
        if (sesionService.isAdmin()) {
            stageManager.switchScene(FxmlView.MENU_ADMIN);
        } else if (sesionService.isCoordinacion()) {
            stageManager.switchScene(FxmlView.MENU_COORDINADOR);
        } else if (sesionService.isArtista()) {
            stageManager.switchScene(FxmlView.MENU_ARTISTA);
        } else {
            stageManager.switchScene(FxmlView.LOGIN);
        }
    }
}