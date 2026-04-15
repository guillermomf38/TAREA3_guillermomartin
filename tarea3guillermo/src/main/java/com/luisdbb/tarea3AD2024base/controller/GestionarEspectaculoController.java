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
	private ListView<String> lvNumeros;
	@FXML
	private TextField txtNombreNum;
	@FXML
	private TextField txtDuracion;
	@FXML
	private ListView<String> lvArtistas;
	@FXML
	private Button btnAtras;
	@FXML
	private Button btnNuevoEspectaculo;
	@FXML
	private Button BtnGuardarEspectaculo;
	@FXML
	private Button btnLimpiarEspectaculo;
	
	@FXML
	private Button btnNuevoNumero;
	@FXML
	private Button btnEditarNumero;
	@FXML
	private Button btnGuardarNumero;
	@FXML
	private Button btnAsignarArtistas;
	
	
	
	
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

		ObservableList<String> items = FXCollections.observableArrayList();

		for (Espectaculo e : espectaculos) {
			items.add(e.getId() + " | " + e.getNombre());
		}

		lvEspectaculos.setItems(items);
	}

	private void cargarCoordinadores() {
		coordinadores = espectaculoService.listarCoordinadores();
		ObservableList<String> items = FXCollections.observableArrayList();
		for (Coordinacion c : coordinadores) {
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

	    numerosActuales = espectaculoService.listarNumerosDeEspectaculo(e.getId());
	    ObservableList<String> items = FXCollections.observableArrayList();
	    for (Numero n : numerosActuales) {
	        items.add(n.getOrden() + ". " + n.getNombre()
	                + " (" + n.getDuracion() + " min)");
	    }
	    lvNumeros.setItems(items);

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
	private void nuevoEspectaculo(ActionEvent event)  {
		espectaculoSeleccionado = null;
		limpiarEspectaculo(event);
	}

	@FXML
	private void guardarEspectaculo(ActionEvent event)  {
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
				mostrarInfo("Espectaculo creado.");
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
	private void limpiarEspectaculo(ActionEvent event)  {
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
	private void nuevoNumero(ActionEvent event)  {
		if (espectaculoSeleccionado == null) {
			mostrarError("Primero selecciona o guarda un espectaculo");
			return;
		}
		numeroSeleccionado = null;
		txtNombreNum.clear();
		txtDuracion.clear();
	}

	@FXML
	private void editarNumero(ActionEvent event) {
		if (numeroSeleccionado == null) {
			mostrarError("Selecciona un numero de la lista");
		}
	}

	@FXML
	private void guardarNumero(ActionEvent event) {
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
	private void asignarArtistas(ActionEvent event) {
	    if (numeroSeleccionado == null) {
	        mostrarError("Selecciona un numero primero");
	        return;
	    }
	    List<Integer> indices = lvArtistas.getSelectionModel().getSelectedIndices();
	    if (indices.isEmpty()) {
	        mostrarError("Selecciona al menos un artista");
	        return;
	    }
	    try {
	        List<Artista> seleccionados = new ArrayList<>();
	        for (Integer idx : indices) {
	            seleccionados.add(artistas.get(idx));
	        }
	        espectaculoService.asignarArtistas(
	                numeroSeleccionado.getId(), seleccionados);
	        mostrarInfo("Artistas asignados correctamente");
	    } catch (ValidacionExcepcion e) {
	        mostrarError(e.getMessage());
	    }
	}

	private Coordinacion getCoordinador() {
		if (sesionService.isCoordinacion()) {
	        String usuario = sesionService.getUsuarioActual().getNombre();
	        for (Coordinacion c : espectaculoService.listarCoordinadores()) {
	            if (c.getCredenciales().getNombre().equals(usuario)) {
	                return c;
	            }
	        }
	        return null;
	    }
	    int idx = cbCoordinador.getSelectionModel().getSelectedIndex();
	    if (idx < 0 || coordinadores == null) return null;
	    return coordinadores.get(idx);
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