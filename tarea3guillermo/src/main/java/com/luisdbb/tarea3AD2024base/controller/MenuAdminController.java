 /**
 *Clase MenuAdminController.java
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
public class MenuAdminController implements Initializable {

	@FXML
	private Button btnIrRegistrarPersona;
	@FXML
	private Button btnIrModificarPersona;
	@FXML
	private Button btnIrGestionar;
	@FXML
	private Button btnIrBuscar;
	
	@FXML
	private Button btnCerrarSesion;
	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private CredencialesService credencialesService;

	@FXML
	private void irRegistrar(ActionEvent event) {
		stageManager.switchScene(FxmlView.REGISTRAR_PERSONA);
	}

	@FXML
	private void irModificar(ActionEvent event) {
		stageManager.switchScene(FxmlView.MODIFICAR_PERSONA);
	}
	@FXML
	private void irGestionar(ActionEvent event) {
		stageManager.switchScene(FxmlView.GESTIONAR_ESPECTACULO);
	}
	@FXML
	private void irBuscar(ActionEvent event) {
		stageManager.switchScene(FxmlView.BUSCAR_ESPECTACULO);
	}
	

	@FXML
	private void cerrarSesion(ActionEvent event) throws IOException {
		credencialesService.logout();
		stageManager.switchScene(FxmlView.LOGIN);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		

	}
}