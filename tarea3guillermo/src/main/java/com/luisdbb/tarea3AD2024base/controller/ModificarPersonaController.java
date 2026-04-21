/**
 *Clase ModificarPersonaController.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package com.luisdbb.tarea3AD2024base.controller;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.excepciones.ValidacionExcepcion;
import com.luisdbb.tarea3AD2024base.modelo.Artista;
import com.luisdbb.tarea3AD2024base.modelo.Coordinacion;
import com.luisdbb.tarea3AD2024base.modelo.Especialidad;
import com.luisdbb.tarea3AD2024base.modelo.Persona;
import com.luisdbb.tarea3AD2024base.services.NacionalidadService;
import com.luisdbb.tarea3AD2024base.services.PersonaService;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

@Controller
public class ModificarPersonaController implements Initializable {

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private NacionalidadService nacionalidadService;

	@FXML
	private ListView<String> lvPersonas;
	@FXML
	private TextField txtNombre;
	@FXML
	private TextField txtEmail;
	@FXML
	private ChoiceBox<String> cbNacionalidad;


	@FXML
	private VBox panelArtista;
	@FXML
	private VBox panelCoord;

	@FXML
	private TextField txtApodo;
	@FXML
	private CheckBox ckAcrobacia;
	@FXML
	private CheckBox ckHumor;
	@FXML
	private CheckBox ckMagia;
	@FXML
	private CheckBox ckEquilibrismo;
	@FXML
	private CheckBox ckMalabarismo;

	@FXML
	private CheckBox ckSenior;
	@FXML
	private DatePicker dpFechaSenior;
	
	@FXML
	private Button btnGuardar;
	
	@FXML
	private Button btnReiniciar;
	
	@FXML
	private Button btnAtras;

	private List<Persona> personas;
	private Persona personaSeleccionada;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbNacionalidad.getItems()
				.addAll(nacionalidadService.getPaises().values());
		cargarPersonas();
		lvPersonas.getSelectionModel().selectedIndexProperty()
        .addListener((obs, oldVal, newVal) -> {
            int idx = newVal.intValue();
            if (idx >= 0 && idx < personas.size()) {
                personaSeleccionada = personas.get(idx);
                cargarPersona(); 
            }
        });
	}

	private void cargarPersonas() {
		personas = personaService.listarPersonas();
		lvPersonas
				.setItems(FXCollections.observableArrayList(personas.stream()
						.map(p -> p.getId() + " - " + p.getNombre() + " ("
								+ p.getCredenciales().getPerfil() + ")")
						.toList()));
	}

	@FXML
	private void cargarPersona() {
		int idx = lvPersonas.getSelectionModel().getSelectedIndex();
		if (idx < 0) {
			mostrarError("Selecciona una persona de la lista");
			return;
		}
		personaSeleccionada = personas.get(idx);
		txtNombre.setText(personaSeleccionada.getNombre());
		txtEmail.setText(personaSeleccionada.getEmail());
		cbNacionalidad.setValue(personaSeleccionada.getNacionalidad());

		if (personaSeleccionada instanceof Artista artista) {
			
			panelArtista.setVisible(true);
			panelArtista.setManaged(true);
			panelCoord.setVisible(false);
			panelCoord.setManaged(false);
			txtApodo.setText(
					artista.getApodo() != null ? artista.getApodo() : "");
			ckAcrobacia.setSelected(artista.getEspecialidades()
					.contains(Especialidad.ACROBACIA));
			ckHumor.setSelected(
					artista.getEspecialidades().contains(Especialidad.HUMOR));
			ckMagia.setSelected(
					artista.getEspecialidades().contains(Especialidad.MAGIA));
			ckEquilibrismo.setSelected(artista.getEspecialidades()
					.contains(Especialidad.EQUILIBRISMO));
			ckMalabarismo.setSelected(artista.getEspecialidades()
					.contains(Especialidad.MALABARISMO));

		} else if (personaSeleccionada instanceof Coordinacion coord) {
			
			panelCoord.setVisible(true);
			panelCoord.setManaged(true);
			panelArtista.setVisible(false);
			panelArtista.setManaged(false);
			ckSenior.setSelected(coord.isSenior());
			dpFechaSenior.setValue(coord.getFechasenior());
			dpFechaSenior.setDisable(!coord.isSenior());
		}
	}

	@FXML
	private void toggleFecha(ActionEvent event)  {
		dpFechaSenior.setDisable(!ckSenior.isSelected());
		if (!ckSenior.isSelected())
			dpFechaSenior.setValue(null);
	}

	@FXML
	private void guardar(ActionEvent event)  {
		if (personaSeleccionada == null) {
			mostrarError("Primero carga una persona");
			return;
		}
		try {
			personaService.modificarDatosPersonales(personaSeleccionada.getId(),
					txtNombre.getText(), txtEmail.getText(),
					cbNacionalidad.getValue());

			if (personaSeleccionada instanceof Artista) {
				List<Especialidad> esp = new ArrayList<>();
				if (ckAcrobacia.isSelected())
					esp.add(Especialidad.ACROBACIA);
				if (ckHumor.isSelected())
					esp.add(Especialidad.HUMOR);
				if (ckMagia.isSelected())
					esp.add(Especialidad.MAGIA);
				if (ckEquilibrismo.isSelected())
					esp.add(Especialidad.EQUILIBRISMO);
				if (ckMalabarismo.isSelected())
					esp.add(Especialidad.MALABARISMO);
				String apodo = txtApodo.getText().isBlank() ? null
						: txtApodo.getText().trim();
				personaService.modificarArtista(personaSeleccionada.getId(),
						apodo, esp);

			} else if (personaSeleccionada instanceof Coordinacion) {
				boolean senior = ckSenior.isSelected();
				if (senior && dpFechaSenior.getValue() == null) {
					mostrarError("Si es senior indica la fecha");
					return;
				}
				personaService.modificarCoordinacion(
						personaSeleccionada.getId(), senior,
						senior ? dpFechaSenior.getValue() : null);
			}

			mostrarInfo("Persona modificada correctamente");
			reiniciar(event);

		} catch (ValidacionExcepcion e) {
			mostrarError(e.getMessage());
		}
	}

	@FXML
	private void reiniciar(ActionEvent event) {
		personaSeleccionada = null;
		lvPersonas.getSelectionModel().clearSelection();
		txtNombre.clear();
		txtEmail.clear();
		cbNacionalidad.setValue(null);
		txtApodo.clear();
		ckAcrobacia.setSelected(false);
		ckHumor.setSelected(false);
		ckMagia.setSelected(false);
		ckEquilibrismo.setSelected(false);
		ckMalabarismo.setSelected(false);
		ckSenior.setSelected(false);
		dpFechaSenior.setValue(null);
		dpFechaSenior.setDisable(true);
		panelArtista.setVisible(false);
		panelArtista.setManaged(false);
		panelCoord.setVisible(false);
		panelCoord.setManaged(false);
		
		cargarPersonas();
	}

	@FXML
	private void atras(ActionEvent event) {
		stageManager.switchScene(FxmlView.MENU_ADMIN);
	}

	private void mostrarError(String msg) {
		Alert a = new Alert(Alert.AlertType.ERROR);
		a.setHeaderText(null);
		a.setContentText(msg);
		a.showAndWait();
	}

	private void mostrarInfo(String msg) {
		Alert a = new Alert(Alert.AlertType.INFORMATION);
		a.setHeaderText(null);
		a.setContentText(msg);
		a.showAndWait();
	}
}