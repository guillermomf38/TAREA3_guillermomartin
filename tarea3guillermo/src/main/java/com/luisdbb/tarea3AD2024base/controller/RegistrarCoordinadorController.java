/**
 *Clase RegistrarCoordinadorController.java
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
import com.luisdbb.tarea3AD2024base.excepciones.ValidacionExcepcion;
import com.luisdbb.tarea3AD2024base.services.NacionalidadService;
import com.luisdbb.tarea3AD2024base.services.PersonaService;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

@Controller
public class RegistrarCoordinadorController implements Initializable {
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
	private CheckBox ckSenior;
	@FXML
	private DatePicker dpFecha;

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

		dpFecha.setDisable(true);
	}

	@FXML
	private void toggleFecha() {
		dpFecha.setDisable(!ckSenior.isSelected());
		if (!ckSenior.isSelected()) {
			dpFecha.setValue(null);
		}
	}

	@FXML
	private void guardar() {
		try {
			String nacionalidad = cbNacionalidad.getValue();
			if (nacionalidad == null) {
				mostrarError("Debes seleccionar una nacionalidad");
				return;
			}

			boolean senior = ckSenior.isSelected();
			if (senior && dpFecha.getValue() == null) {
				mostrarError("Si es senior debes indicar la fecha");
				return;
			}

			personaService.registrarCoordinacion(txtNombre.getText(),
					txtemail.getText(), nacionalidad, txtUsuario.getText(),
					psPassword.getText(), senior,
					senior ? dpFecha.getValue() : null);

			mostrarInfo("Coordinador registrado correctamente");
			reiniciar();

		} catch (ValidacionExcepcion e) {
			mostrarError(e.getMessage());
		}
	}

	@FXML
	private void reiniciar() {
		txtNombre.clear();
		psPassword.clear();
		txtUsuario.clear();
		txtemail.clear();
		cbNacionalidad.setValue(null);
		ckSenior.setSelected(false);
		dpFecha.setValue(null);
		dpFecha.setDisable(true);
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
