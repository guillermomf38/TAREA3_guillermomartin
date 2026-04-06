/**
 *Clase RegistrarArtistaController.java
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
import com.luisdbb.tarea3AD2024base.modelo.Especialidad;
import com.luisdbb.tarea3AD2024base.services.NacionalidadService;
import com.luisdbb.tarea3AD2024base.services.PersonaService;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@Controller
public class RegistrarArtistaController implements Initializable {
	@Lazy
    @Autowired
    private StageManager stageManager;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private NacionalidadService nacionalidadService;

	@FXML
	private TextField txtNombre;
	@FXML
	private PasswordField psPassword;
	@FXML
	private TextField txtUsuario;
	@FXML
	private TextField txtemail;
	@FXML
	private ChoiceBox<String> cbNacionalidad;

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
	private Button btnGuardar;
	@FXML
	private Button btnReiniciar;
	@FXML
	private Button btnIrAtras;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		cbNacionalidad.getItems()
				.addAll(nacionalidadService.getPaises().values());
	}

	@FXML
	private void guardarArtista() {
		try {
			String nacionalidad = cbNacionalidad.getValue();
			if (nacionalidad == null) {
				mostrarError("Debes seleccionar una nacionalidad");
				return;
			}

			List<Especialidad> especialidades = getEspecialidades();

			String apodo = txtApodo.getText().isBlank() ? null
					: txtApodo.getText().trim();

			personaService.registrarArtista(txtNombre.getText(),
					txtemail.getText(), nacionalidad, txtUsuario.getText(),
					psPassword.getText(), apodo, especialidades);

			mostrarInfo("Artista registrado correctamente");
			reiniciar();

		} catch (ValidacionExcepcion e) {
			mostrarError(e.getMessage());
		}
	}

	private List<Especialidad> getEspecialidades() {
		List<Especialidad> lista = new ArrayList<>();
		if (ckAcrobacia.isSelected())
			lista.add(Especialidad.ACROBACIA);
		if (ckHumor.isSelected())
			lista.add(Especialidad.HUMOR);
		if (ckMagia.isSelected())
			lista.add(Especialidad.MAGIA);
		if (ckEquilibrismo.isSelected())
			lista.add(Especialidad.EQUILIBRISMO);
		if (ckMalabarismo.isSelected())
			lista.add(Especialidad.MALABARISMO);
		return lista;
	}

	@FXML
	private void reiniciar() {
		txtNombre.clear();
		psPassword.clear();
		txtUsuario.clear();
		txtemail.clear();
		cbNacionalidad.setValue(null);
		txtApodo.clear();
		ckAcrobacia.setSelected(false);
		ckHumor.setSelected(false);
		ckMagia.setSelected(false);
		ckEquilibrismo.setSelected(false);
		ckMalabarismo.setSelected(false);
	}

	@FXML
	private void atras() {
		stageManager.switchScene(FxmlView.REGISTRAR_PERSONA);
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
