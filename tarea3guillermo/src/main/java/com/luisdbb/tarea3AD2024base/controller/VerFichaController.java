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
import com.luisdbb.tarea3AD2024base.modelo.Especialidad;
import com.luisdbb.tarea3AD2024base.modelo.Numero;
import com.luisdbb.tarea3AD2024base.services.PersonaService;
import com.luisdbb.tarea3AD2024base.services.SesionService;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;


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
    @FXML private ListView<Especialidad> lvEspecialidades;
    @FXML private TextArea taParticipaciones;
    @FXML private Button btnAtras;

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
        lvEspecialidades.getItems().setAll(artista.getEspecialidades());

        StringBuilder sb = new StringBuilder();
        if (artista.getNumeros().isEmpty()) 
        {
            sb.append("Sin participaciones registradas");
        } else 
        {
            for (Numero n : artista.getNumeros()) 
            {
                sb.append("────────────────────────────\n");
                sb.append("Espectáculo ID: ").append(n.getEspectaculo().getId()).append("\n");
                sb.append("Nombre Espectáculo: ").append(n.getEspectaculo().getNombre()).append("\n");
                sb.append("Número ID: ").append(n.getId()).append("\n");
                sb.append("Nombre Número: ").append(n.getNombre()).append("\n\n");
            }
        }

        taParticipaciones.setText(sb.toString());
    }

    @FXML
    private void atras(ActionEvent event)  {
        stageManager.switchScene(FxmlView.MENU_ARTISTA);
    }
}