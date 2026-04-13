/**
 *Clase MenuCoordinadorController.java
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
import com.luisdbb.tarea3AD2024base.services.CredencialesService;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

@Controller
public class MenuCoordinadorController implements Initializable {

	@FXML
	private Button btnIrGestion;
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
	private void irGestionar() {
		stageManager.switchScene(FxmlView.GESTIONAR_ESPECTACULO);
	}

	@FXML
	private void irBuscar() {
		stageManager.switchScene(FxmlView.BUSCAR_ESPECTACULO);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}


	@FXML
	private void cerrarSesion() {
		credencialesService.logout();
		stageManager.switchScene(FxmlView.LOGIN);
	}
}

