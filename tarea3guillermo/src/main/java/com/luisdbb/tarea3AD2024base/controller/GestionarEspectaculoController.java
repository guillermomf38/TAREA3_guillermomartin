/**
 *Clase GestionarEspectaculoController.java
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
import com.luisdbb.tarea3AD2024base.excepciones.ValidacionExcepcion;
import com.luisdbb.tarea3AD2024base.modelo.Artista;
import com.luisdbb.tarea3AD2024base.modelo.Coordinacion;
import com.luisdbb.tarea3AD2024base.modelo.Espectaculo;
import com.luisdbb.tarea3AD2024base.modelo.Numero;
import com.luisdbb.tarea3AD2024base.services.EspectaculoService;
import com.luisdbb.tarea3AD2024base.services.SesionService;
import com.luisdbb.tarea3AD2024base.view.FxmlView;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

@Lazy
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
	private ListView<String> lvNumeros;
	@FXML
	private TextField txtNombreNum;
	@FXML
	private TextField txtDuracion;
	@FXML
	private ListView<String> lvArtistas;

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

		if (sesionService.isCoordinacion()) {
			hboxCoord.setVisible(false);
			hboxCoord.setManaged(false);
		} else {
			cargarCoordinadores();
		}

		lvEspectaculos.getSelectionModel().selectedIndexProperty()
				.addListener((obs, o, n) -> {
					int idx = n.intValue();
					if (idx >= 0 && idx < espectaculos.size()) {
						seleccionarEspectaculo(espectaculos.get(idx));
					}
				});

		lvNumeros.getSelectionModel().selectedIndexProperty()
				.addListener((obs, o, n) -> {
					int idx = n.intValue();
					if (numerosActuales != null && idx >= 0
							&& idx < numerosActuales.size()) {
						seleccionarNumero(numerosActuales.get(idx));
					}
				});

		lvArtistas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}

	private void cargarEspectaculos() {
		espectaculos = espectaculoService.listarEspectaculos();
		lvEspectaculos.setItems(
				FXCollections.observableArrayList(espectaculos.stream()
						.map(e -> e.getId() + " | " + e.getNombre()).toList()));
	}

	private void cargarCoordinadores() {
		coordinadores = espectaculoService.listarCoordinadores();
		cbCoordinador.setItems(
				FXCollections.observableArrayList(coordinadores.stream()
						.map(c -> c.getId() + " - " + c.getNombre()).toList()));
	}

	private void cargarArtistas() {
		artistas = espectaculoService.listarArtistas();
		lvArtistas.setItems(FXCollections.observableArrayList(artistas.stream()
				.map(a -> a.getId() + " - " + a.getNombre()).toList()));
	}

	private void seleccionarEspectaculo(Espectaculo e) {
		espectaculoSeleccionado = e;
		txtNombreEsp.setText(e.getNombre());
		dpFechaini.setValue(e.getFechaini());
		dpFechafin.setValue(e.getFechafin());
		numerosActuales = espectaculoService
				.listarNumerosDeEspectaculo(e.getId());
		lvNumeros
				.setItems(FXCollections
						.observableArrayList(numerosActuales.stream()
								.map(n -> n.getOrden() + ". " + n.getNombre()
										+ " (" + n.getDuracion() + " min)")
								.toList()));
		txtNombreNum.clear();
		txtDuracion.clear();
		numeroSeleccionado = null;
	}

	private void seleccionarNumero(Numero n) {
		numeroSeleccionado = n;
		txtNombreNum.setText(n.getNombre());
		txtDuracion.setText(String.valueOf(n.getDuracion()));
	}

	@FXML
	private void nuevoEspectaculo() {
		espectaculoSeleccionado = null;
		limpiarEspectaculo();
	}

	@FXML
	private void guardarEspectaculo() {
		try {
			Coordinacion coord = getCoordinador();
			if (coord == null) {
				mostrarError("Debes seleccionar un coordinador");
				return;
			}
			if (espectaculoSeleccionado == null) {
				espectaculoSeleccionado = espectaculoService.crearEspectaculo(
						txtNombreEsp.getText(), dpFechaini.getValue(),
						dpFechafin.getValue(), coord);
				mostrarInfo("Espectaculo creado. Crea al menos 3 numeros.");
			} else {
				espectaculoService.modificarEspectaculo(
						espectaculoSeleccionado.getId(), txtNombreEsp.getText(),
						dpFechaini.getValue(), dpFechafin.getValue(), coord);
				mostrarInfo("Espectaculo modificado correctamente");
			}
			cargarEspectaculos();
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
		txtNombreNum.clear();
		txtDuracion.clear();
		numeroSeleccionado = null;
	}

	@FXML
	private void nuevoNumero() {
		if (espectaculoSeleccionado == null) {
			mostrarError("Primero selecciona o guarda un espectaculo");
			return;
		}
		numeroSeleccionado = null;
		txtNombreNum.clear();
		txtDuracion.clear();
	}

	@FXML
	private void editarNumero() {
		if (numeroSeleccionado == null) {
			mostrarError("Selecciona un numero de la lista");
		}
	}

	@FXML
	private void guardarNumero() {
		if (espectaculoSeleccionado == null) {
			mostrarError("Primero selecciona o guarda un espectaculo");
			return;
		}
		try {
			double duracion = Double
					.parseDouble(txtDuracion.getText().replace(",", "."));
			if (numeroSeleccionado == null) {
				espectaculoService.crearNumero(txtNombreNum.getText(), duracion,
						espectaculoSeleccionado);
				mostrarInfo("Numero creado correctamente");
			} else {
				espectaculoService.modificarNumero(numeroSeleccionado.getId(),
						txtNombreNum.getText(), duracion);
				mostrarInfo("Numero modificado correctamente");
			}
			seleccionarEspectaculo(espectaculoSeleccionado);
		} catch (ValidacionExcepcion e) {
			mostrarError(e.getMessage());
		} catch (NumberFormatException e) {
			mostrarError("Duracion invalida. Usa formato: 5.0 o 5.5");
		}
	}

	@FXML
	private void asignarArtistas() {
		if (numeroSeleccionado == null) {
			mostrarError("Selecciona un numero primero");
			return;
		}
		List<Integer> indices = lvArtistas.getSelectionModel()
				.getSelectedIndices();
		if (indices.isEmpty()) {
			mostrarError("Selecciona al menos un artista");
			return;
		}
		try {
			List<Artista> seleccionados = indices.stream().map(artistas::get)
					.toList();
			espectaculoService.asignarArtistas(numeroSeleccionado.getId(),
					seleccionados);
			mostrarInfo("Artistas asignados correctamente");
		} catch (ValidacionExcepcion e) {
			mostrarError(e.getMessage());
		}
	}

	private Coordinacion getCoordinador() {
		if (sesionService.isCoordinacion()) {
			String usuario = sesionService.getUsuarioActual().getNombre();
			return espectaculoService.listarCoordinadores().stream().filter(
					c -> c.getCredenciales().getNombre().equals(usuario))
					.findFirst().orElse(null);
		}
		int idx = cbCoordinador.getSelectionModel().getSelectedIndex();
		if (idx < 0 || coordinadores == null)
			return null;
		return coordinadores.get(idx);
	}

	@FXML
	private void atras() {
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