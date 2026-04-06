/**
 *Clase VerFichaController.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */



package com.luisdbb.tarea3AD2024base.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.modelo.Artista;
import com.luisdbb.tarea3AD2024base.modelo.Numero;
import com.luisdbb.tarea3AD2024base.services.PersonaService;
import com.luisdbb.tarea3AD2024base.services.SesionService;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

@Lazy
@Controller
public class VerFichaController implements Initializable {

    @Lazy
    @Autowired
    private StageManager stageManager;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private SesionService sesionService;

    @FXML private Label lblNombre;
    @FXML private Label lblEmail;
    @FXML private Label lblNacionalidad;
    @FXML private Label lblApodo;
    @FXML private Label lblEspecialidades;
    @FXML private TextArea taParticipaciones;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarFicha();
    }

    private void cargarFicha() {
        String usuario = sesionService.getUsuarioActual().getNombre();
        Artista artista = personaService.buscarArtistaPorUsuario(usuario);

        if (artista == null) return;

        lblNombre.setText(artista.getNombre());
        lblEmail.setText(artista.getEmail());
        lblNacionalidad.setText(artista.getNacionalidad());
        lblApodo.setText(artista.getApodo() != null ? artista.getApodo() : "-");
        lblEspecialidades.setText(artista.getEspecialidades().toString());

        StringBuilder sb = new StringBuilder();
        for (Numero n : artista.getNumeros()) {
            sb.append("Espectáculo: ")
                    .append(n.getEspectaculo().getNombre())
                    .append("\n")
                    .append("  Número: ").append(n.getNombre())
                    .append(" - Orden: ").append(n.getOrden())
                    .append("\n\n");
        }
        taParticipaciones.setText(sb.isEmpty()
                ? "Sin participaciones registradas" : sb.toString());
    }

    @FXML
    private void atras() {
        stageManager.switchScene(FxmlView.MENU_ARTISTA);
    }
}