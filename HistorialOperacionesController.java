/**
 *Clase HistorialOperacionesController.java
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
import com.luisdbb.tarea3AD2024base.modelo.LogOperacion;
import com.luisdbb.tarea3AD2024base.modelo.TipoOperacion;
import com.luisdbb.tarea3AD2024base.services.LogDb4oService;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

@Controller
public class HistorialOperacionesController implements Initializable {
	
		@Lazy
		@Autowired
		private StageManager stageManager;

		@Autowired
		private LogDb4oService logDb4oService;

		@FXML
		private TextField txtUsuarioFiltro;

		@FXML
		private Button btnAtras;

		@FXML
		private Button btnBuscar;

		@FXML
		private Button btnMostrar;

		@FXML
		private Button btnLimpiar;

		@FXML
		private CheckBox ckNuevo;

		@FXML
		private CheckBox ckActualizacion;

		@FXML
		private CheckBox ckBorrado;

		@FXML
		private DatePicker dpDesde;

		@FXML
		private DatePicker dpHasta;

		@FXML
		private ListView<String> lvResultados;

		@FXML
		private Label lblTotal;

		@Override
		public void initialize(URL location, ResourceBundle resources) {
			mostrarTodos();

		}

		@FXML
		private void buscar(ActionEvent event) {

			List<TipoOperacion> tipos = new ArrayList<>();
			if (ckNuevo.isSelected()) {
				tipos.add(TipoOperacion.NUEVO);
			}

			if (ckActualizacion.isSelected()) {
				tipos.add(TipoOperacion.ACTUALIZACION);
			}
			if (ckBorrado.isSelected()) {
				tipos.add(TipoOperacion.BORRADO);
			}

			 String desde = dpDesde.getValue() != null ? dpDesde.getValue() + " 00:00:00" : null;
			    String hasta = dpHasta.getValue() != null ? dpHasta.getValue() + " 23:59:59" : null;
			    
			List<LogOperacion> resultados = logDb4oService.consultarHistorial(txtUsuarioFiltro.getText(), tipos, desde, hasta);

			mostrarResultados(resultados);
		}

		@FXML
		private void mostrarTodos(ActionEvent event) {
			mostrarTodos();
		}

		private void mostrarTodos() {
			List<LogOperacion> todos = logDb4oService.total();
			mostrarResultados(todos);
		}

		@FXML
		private void limpiarFiltros(ActionEvent event) {
			txtUsuarioFiltro.clear();
			ckNuevo.setSelected(true);
			ckActualizacion.setSelected(true);
			ckBorrado.setSelected(true);
			dpDesde.setValue(null);
			dpHasta.setValue(null);
			mostrarTodos();
		}

		private void mostrarResultados(List<LogOperacion> logs) {
			ObservableList<String> items = FXCollections.observableArrayList();
			for (LogOperacion log : logs) {
				items.add(log.getFechaHora() + " | " + log.getUsuario() + " | " + log.getTipoOperacion() + " | " + log.getResumen());
			}

			lvResultados.setItems(items);

			lblTotal.setText("Total: " + logs.size() + " registros");
		}

		@FXML
		private void atras(ActionEvent event) {
			stageManager.switchScene(FxmlView.MENU_ADMIN);
		}

	}
