/**
 *Clase GestionarEspectaculoController.java
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
import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;
import com.luisdbb.tarea3AD2024base.modelo.Numero;
import com.luisdbb.tarea3AD2024base.services.EspectaculoService;
import com.luisdbb.tarea3AD2024base.services.SesionService;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

@Controller
public class GestionarEspectaculoController implements Initializable {

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private EspectaculoService espectaculoService;

	@Autowired
	private SesionService sesionService;

	@FXML
	private ListView<String> lvEspectaculos;
	@FXML
	private TextField txtNombreEsp;
	@FXML
	private DatePicker dpFechaini;
	@FXML
	private DatePicker dpFechafin;
	@FXML
	private ComboBox<String> cbCoordinador;
	@FXML
	private HBox hboxCoord;
	@FXML
	private Button btnCrearEsp;
	@FXML
	private Button btnGuardarEsp;

	@FXML
	private ListView<String> lvNumeros;
	@FXML
	private TextField txtNombreNum;
	@FXML
	private TextField txtOrden;
	@FXML
	private TextField txtDuracion;
	@FXML
	private Button btnCrearNum;
	@FXML
	private Button btnGuardarNum;

	@FXML
	private ListView<String> lvArtistas;
	@FXML
	private Button btnNuevoNum;
	@FXML
	private Button btnAsignarArtistas;
	@FXML
	private Button btnLimpiar;
	@FXML
	private Button btnAtras;

	private List<Espectaculo> espectaculos;
	private List<Coordinacion> coordinadores;
	private List<Artista> artistas;
	private List<Numero> numerosActuales;

	private Espectaculo espectaculoSeleccionado;
	private Numero numeroSeleccionado;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cargarEspectaculos();
		cargarArtistas();

		if (sesionService.isCoordinacion()) 
		{
			hboxCoord.setVisible(false);
			hboxCoord.setManaged(false);
		} else {
			cargarCoordinadores();
		}

		lvEspectaculos.getSelectionModel().selectedIndexProperty().addListener((obs, o, n) -> {
					int idx = n.intValue();
					if (idx >= 0 && idx < espectaculos.size()) {
						seleccionarEspectaculo(espectaculos.get(idx));
					}
				});

		lvNumeros.getSelectionModel().selectedIndexProperty().addListener((obs, o, n) -> {
					int idx = n.intValue();
					if (numerosActuales != null && idx >= 0
							&& idx < numerosActuales.size()) 
					{
						numeroSeleccionado = numerosActuales.get(idx);
						txtNombreNum.setText(numeroSeleccionado.getNombre());
						txtOrden.setText(String.valueOf(numeroSeleccionado.getOrden()));
						txtDuracion.setText(String.valueOf(numeroSeleccionado.getDuracion()));

						btnGuardarNum.setDisable(false);
						btnCrearNum.setDisable(true);
					}
				});

		lvArtistas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	private void cargarEspectaculos() {
		espectaculos = espectaculoService.listarEspectaculos();
		ObservableList<String> items = FXCollections.observableArrayList();
		for (Espectaculo e : espectaculos) 
		{
			items.add(e.getId() + " | " + e.getNombre());
		}
		lvEspectaculos.setItems(items);
	}

	private void cargarCoordinadores() {
		coordinadores = espectaculoService.listarCoordinadores();
		ObservableList<String> items = FXCollections.observableArrayList();
		for (Coordinacion c : coordinadores) 
		{
			items.add(c.getId() + " - " + c.getNombre());
		}
		cbCoordinador.setItems(items);
	}

	private void cargarArtistas() {
		artistas = espectaculoService.listarArtistas();
		ObservableList<String> items = FXCollections.observableArrayList();
		for (Artista a : artistas) {
			items.add(a.getId() + " - " + a.getNombre());
		}
		lvArtistas.setItems(items);
	}

	private void seleccionarEspectaculo(Espectaculo e) {
		espectaculoSeleccionado = e;
		txtNombreEsp.setText(e.getNombre());
		dpFechaini.setValue(e.getFechaini());
		dpFechafin.setValue(e.getFechafin());

		btnGuardarEsp.setDisable(false);
		btnCrearEsp.setDisable(true);

		numerosActuales = espectaculoService.listarNumerosDeEspectaculo(e.getId());
		ObservableList<String> items = FXCollections.observableArrayList();
		for (Numero n : numerosActuales) {
			items.add(n.getOrden() + ". " + n.getNombre() + " ("+ n.getDuracion() + " min)");
		}
		lvNumeros.setItems(items);

		limpiarFormularioNumero();
	}

	@FXML
	private void nuevoEspectaculo(ActionEvent event) {

		espectaculoSeleccionado = null;
		btnCrearEsp.setDisable(false);
		btnGuardarEsp.setDisable(true);
		limpiarEspectaculo();
	}

	@FXML
	private void crearEspectaculo(ActionEvent event) {
		try {
			Coordinacion coord = getCoordinador();
			if (coord == null) {
				mostrarError("Debes seleccionar un coordinador");
				return;
			}
			espectaculoSeleccionado = espectaculoService.crearEspectaculo(txtNombreEsp.getText(), dpFechaini.getValue(),dpFechafin.getValue(), coord);

			mostrarInfo("Espectáculo creado. Añade al menos 3 numeros antes de guardar.");

			btnGuardarEsp.setDisable(false);
			btnCrearEsp.setDisable(true);

			cargarEspectaculos();
			numerosActuales = new ArrayList<>();
			lvNumeros.getItems().clear();

		} catch (ValidacionExcepcion e) {
			mostrarError(e.getMessage());
		}
	}

	@FXML
	private void guardarEspectaculo(ActionEvent event) {
		if (espectaculoSeleccionado == null) {
			mostrarError("Primero crea o selecciona un espectaculo");
			return;
		}

		List<Numero> numeros = espectaculoService.listarNumerosDeEspectaculo(espectaculoSeleccionado.getId());
		if (numeros.size() < 3) 
		{
			mostrarError("El espectaculo debe tener al menos 3 numeros. "+ "Actualmente tiene: " + numeros.size());
			return;
		}
		for (Numero n : numeros) {
			if (n.getArtistas().isEmpty()) 
			{
				mostrarError("El numero '" + n.getNombre()+ "' no tiene artistas asignados. "+ "Cada numero debe tener al menos 1 artista.");
				return;
			}
		}

		try {
			Coordinacion coord = getCoordinador();
			if (coord == null) 
			{
				mostrarError("Debes seleccionar un coordinador");
				return;
			}
			espectaculoService.modificarEspectaculo(espectaculoSeleccionado.getId(), txtNombreEsp.getText(),dpFechaini.getValue(), dpFechafin.getValue(), coord);

			mostrarInfo("Espectaculo guardado correctamente con "+ numeros.size() + " numeros");
			cargarEspectaculos();
			nuevoEspectaculo(event);

		} catch (ValidacionExcepcion e) {
			mostrarError(e.getMessage());
		}
	}

	@FXML
	private void limpiarEspectaculo() {
		txtNombreEsp.clear();
		dpFechaini.setValue(null);
		dpFechafin.setValue(null);
		cbCoordinador.setValue(null);
		lvNumeros.getItems().clear();
		numerosActuales = null;
		limpiarFormularioNumero();
	}

	@FXML
	private void nuevoNumero(ActionEvent event) {
		if (espectaculoSeleccionado == null) {
			mostrarError("Primero crea o selecciona un espectaculo");
			return;
		}

		numeroSeleccionado = null;
		btnCrearNum.setDisable(false);
		btnGuardarNum.setDisable(true);
		limpiarFormularioNumero();
	}

	@FXML
	private void crearNumero(ActionEvent event) {
		if (espectaculoSeleccionado == null) {
			mostrarError("Primero crea o selecciona un espectaculo");
			return;
		}
		try {
			espectaculoService.crearNumero(txtOrden.getText(),txtNombreNum.getText(), txtDuracion.getText(),espectaculoSeleccionado);

			mostrarInfo("Numero creado correctamente");
			seleccionarEspectaculo(espectaculoSeleccionado);

		} catch (ValidacionExcepcion e) {
			mostrarError(e.getMessage());
		}
	}

	@FXML
	private void guardarNumero(ActionEvent event) {
		if (numeroSeleccionado == null) {
			mostrarError("Selecciona un numero para editar");
			return;
		}
		try {
			espectaculoService.modificarNumero(numeroSeleccionado.getId(),txtOrden.getText(), txtNombreNum.getText(),txtDuracion.getText());

			mostrarInfo("Numero modificado correctamente");
			seleccionarEspectaculo(espectaculoSeleccionado);
			nuevoNumero(event);

		} catch (ValidacionExcepcion e) {
			mostrarError(e.getMessage());
		}
	}

	@FXML
	private void asignarArtistas(ActionEvent event) {
		if (numeroSeleccionado == null) {
			mostrarError("Selecciona un numero primero");
			return;
		}
		List<Integer> indices = lvArtistas.getSelectionModel().getSelectedIndices();
		if (indices.isEmpty()) 
		{
			mostrarError("Debes seleccionar al menos un artista");
			return;
		}
		try {
			List<Artista> seleccionados = new ArrayList<>();
			for (Integer idx : indices) 
			{
				seleccionados.add(artistas.get(idx));
			}
			espectaculoService.asignarArtistas(numeroSeleccionado.getId(),seleccionados);
			mostrarInfo("Artistas asignados correctamente");
		} catch (ValidacionExcepcion e) {
			mostrarError(e.getMessage());
		}
	}

	private Coordinacion getCoordinador() {
		if (sesionService.isCoordinacion()) 
		{
			String usuario = sesionService.getUsuarioActual().getNombre();
			for (Coordinacion c : espectaculoService.listarCoordinadores())
			{
				if (c.getCredenciales().getNombre().equals(usuario)) 
				{
					return c;
				}
			}
			return null;
		}
		int idx = cbCoordinador.getSelectionModel().getSelectedIndex();
		if (idx < 0 || coordinadores == null)
			return null;
		return coordinadores.get(idx);
	}

	private void limpiarFormularioNumero() {
		txtNombreNum.clear();
		txtOrden.clear();
		txtDuracion.clear();
		numeroSeleccionado = null;
		btnCrearNum.setDisable(false);
		btnGuardarNum.setDisable(true);
	}

	@FXML
	private void atras(ActionEvent event) {
		if (sesionService.isAdmin()) {
			stageManager.switchScene(FxmlView.MENU_ADMIN);
		} else {
			stageManager.switchScene(FxmlView.MENU_COORDINADOR);
		}
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
