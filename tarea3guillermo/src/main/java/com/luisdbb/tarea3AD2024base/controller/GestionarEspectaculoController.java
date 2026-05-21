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
import javafx.scene.control.Label;
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
	private Label lblNumeros;
	@FXML
	private Button btnGuardarEsp;
	@FXML
	private Button btnAtras;
	@FXML
	private Button btnLimpiar;
	@FXML
	private Button btnModificarNum;
	@FXML
	private ListView<String> lvNumeros;
	@FXML
	private ListView<String> lvListadoArtistasParaNum;

	@FXML
	private TextField txtNombreNum;
	@FXML
	private TextField txtOrden;
	@FXML
	private TextField txtDuracion;
	@FXML
	private Button btnCrearNum;
	@FXML
	private Button btnBorrarNum;
	
	@FXML
	private Button btnAsignarArtistas;

	@FXML
	private Button btnCrearEsp;
	@FXML
	private ListView<String> lvArtistas;

	private List<Espectaculo> espectaculos;
	private List<Coordinacion> coordinadores;
	private List<Artista> artistas;

	private Numero numeroSeleccionado;

	private Espectaculo espectaculoSeleccionado;

	private List<NumeroTemp> numerosEnMemoria = new ArrayList<>();

	private List<Artista> artistasDelNumeroActual = new ArrayList<>();

	private static class NumeroTemp {
		int orden;
		String nombre;
		double duracion;
		List<Artista> artistas;

		NumeroTemp(int orden, String nombre, double duracion,
				List<Artista> artistas) {
			this.orden = orden;
			this.nombre = nombre;
			this.duracion = duracion;
			this.artistas = new ArrayList<>(artistas);
		}
	}

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

		lvArtistas.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		btnCrearNum.setDisable(true);

		lvEspectaculos.getSelectionModel().selectedIndexProperty()
				.addListener((obs, o, n) -> {
					int idx = n.intValue();
					if (idx >= 0 && idx < espectaculos.size()) {
						cargarEspectaculoSeleccionado(espectaculos.get(idx));
					}
				});

		lvNumeros.getSelectionModel().selectedIndexProperty()
				.addListener((obs, o, n) -> {
					int idx = n.intValue();
					if (espectaculoSeleccionado == null) {

						if (idx >= 0 && idx < numerosEnMemoria.size()) {
							ObservableList<String> items = FXCollections
									.observableArrayList();
							for (Artista a : numerosEnMemoria
									.get(idx).artistas) {
								items.add(a.getId() + " - " + a.getNombre());
							}
							lvListadoArtistasParaNum.setItems(items);
						}
					} else {

						List<Numero> numeros = espectaculoService
								.listarNumerosDeEspectaculo(
										espectaculoSeleccionado.getId());
						if (idx >= 0 && idx < numeros.size()) {
							numeroSeleccionado = numeros.get(idx);
							txtNombreNum
									.setText(numeroSeleccionado.getNombre());
							txtOrden.setText(String
									.valueOf(numeroSeleccionado.getOrden()));
							txtDuracion.setText(String
									.valueOf(numeroSeleccionado.getDuracion()));

							ObservableList<String> items = FXCollections
									.observableArrayList();
							for (Artista a : numeroSeleccionado.getArtistas()) {
								items.add(a.getId() + " - " + a.getNombre());
							}
							lvListadoArtistasParaNum.setItems(items);
						}
					}
				});
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

	private void cargarEspectaculoSeleccionado(Espectaculo e) {
		espectaculoSeleccionado = e;
		numerosEnMemoria.clear();
		txtNombreEsp.setText(e.getNombre());
		dpFechaini.setValue(e.getFechaini());
		dpFechafin.setValue(e.getFechafin());

		if (!sesionService.isCoordinacion() && e.getCoordinador() != null) {
			for (int i = 0; i < coordinadores.size(); i++) {
				if (coordinadores.get(i).getId()
						.equals(e.getCoordinador().getId())) {
					cbCoordinador.getSelectionModel().select(i);
					break;
				}
			}
		}

		btnGuardarEsp.setDisable(false);

		List<Numero> numeros = espectaculoService
				.listarNumerosDeEspectaculo(e.getId());
		ObservableList<String> items = FXCollections.observableArrayList();
		for (Numero n : numeros) {
			items.add(n.getOrden() + ". " + n.getNombre() + " ("
					+ n.getDuracion() + " min)" + " - " + n.getArtistas().size()
					+ " artistas");
		}
		lvNumeros.setItems(items);

		limpiarFormularioNumero();
	}

	@FXML
	private void modificarNumero(ActionEvent event) {
		if (espectaculoSeleccionado == null) {
			mostrarError("Selecciona un espectaculo primero");
			return;
		}
		if (numeroSeleccionado == null) {
			mostrarError("Selecciona un numero de la lista primero");
			return;
		}
		try {
			espectaculoService.modificarNumero(numeroSeleccionado.getId(),
					txtOrden.getText(), txtNombreNum.getText(),
					txtDuracion.getText());
			mostrarInfo("Numero modificado correctamente");
			cargarEspectaculoSeleccionado(espectaculoSeleccionado);
		} catch (ValidacionExcepcion e) {
			mostrarError(e.getMessage());
		}
	}
	@FXML
	private void borrarNumero() {
	    if (espectaculoSeleccionado == null) {
	        mostrarError("Selecciona un espectaculo primero");
	        return;
	    }
	    if (numeroSeleccionado == null) {
	        mostrarError("Selecciona un numero de la lista primero");
	        return;
	    }
	    try {
	        Long idEspectaculo = espectaculoSeleccionado.getId();
	        Long idNumero = numeroSeleccionado.getId();

	        espectaculoService.borrarNumero(idNumero);

	     
	        numeroSeleccionado = null;
	        espectaculoSeleccionado = null;

	        
	        List<Espectaculo> todos = espectaculoService.listarEspectaculos();
	        Espectaculo fresco = null;
	        for (Espectaculo e : todos) {
	            if (e.getId().equals(idEspectaculo)) {
	                fresco = e;
	                break;
	            }
	        }

	       
	        cargarEspectaculos();

	       
	        if (fresco != null) {
	            cargarEspectaculoSeleccionado(fresco);
	        }

	        mostrarInfo("Numero borrado correctamente.");

	    } catch (Exception e) {
	        mostrarError("Error al borrar: " + e.getMessage());
	        e.printStackTrace();
	    }
	}

	@FXML
	private void nuevoEspectaculo(ActionEvent event) {
		espectaculoSeleccionado = null;
		numerosEnMemoria.clear();
		artistasDelNumeroActual.clear();
		lvEspectaculos.getSelectionModel().clearSelection();
		btnGuardarEsp.setDisable(true);
		limpiarTodo();
	}

	@FXML
	private void crearEspectaculo(ActionEvent event) {

		if (espectaculoSeleccionado != null) {
			mostrarError("Ya hay un espectaculo seleccionado. "
					+ "Usa 'Guardar Cambios' para modificarlo "
					+ "o 'Nuevo Espectaculo' para crear uno.");
			return;
		}

		if (numerosEnMemoria.size() < 3) {
			mostrarError("Debes añadir al menos 3 numeros antes de crear "
					+ "el espectaculo. Tienes: " + numerosEnMemoria.size());
			return;
		}

		for (NumeroTemp nt : numerosEnMemoria) {
			if (nt.artistas.isEmpty()) {
				mostrarError("El numero '" + nt.nombre
						+ "' no tiene artistas asignados");
				return;
			}
		}

		try {
			Coordinacion coord = getCoordinador();
			if (coord == null) {
				mostrarError("Debes seleccionar un coordinador");
				return;
			}

			Espectaculo nuevo = espectaculoService.crearEspectaculo(
					txtNombreEsp.getText(), dpFechaini.getValue(),
					dpFechafin.getValue(), coord);

			for (NumeroTemp nt : numerosEnMemoria) {
				Numero numGuardado = espectaculoService.crearNumero(
						String.valueOf(nt.orden), nt.nombre,
						String.valueOf(nt.duracion), nuevo);
				espectaculoService.asignarArtistas(numGuardado.getId(),
						nt.artistas);
			}

			mostrarInfo("Espectaculo creado correctamente con "
					+ numerosEnMemoria.size() + " numeros");
			numerosEnMemoria.clear();
			cargarEspectaculos();
			limpiarTodo();

		} catch (ValidacionExcepcion e) {
			mostrarError(e.getMessage());
		}
	}

	@FXML
	private void guardarEspectaculo(ActionEvent event) {
		if (espectaculoSeleccionado == null) {
			mostrarError(
					"Selecciona un espectaculo de la lista para modificarlo");
			return;
		}
		try {
			Coordinacion coord = getCoordinador();
			if (coord == null) {
				mostrarError("Debes seleccionar un coordinador");
				return;
			}
			espectaculoService.modificarEspectaculo(
					espectaculoSeleccionado.getId(), txtNombreEsp.getText(),
					dpFechaini.getValue(), dpFechafin.getValue(), coord);
			mostrarInfo("Espectaculo modificado correctamente");
			cargarEspectaculos();
			limpiarTodo();
		} catch (ValidacionExcepcion e) {
			mostrarError(e.getMessage());
		}
	}

	@FXML
	private void limpiarEspectaculo(ActionEvent event) {
		espectaculoSeleccionado = null;
		numerosEnMemoria.clear();
		btnGuardarEsp.setDisable(true);
		limpiarTodo();
	}

	@FXML
	private void nuevoNumero(ActionEvent event) {
		limpiarFormularioNumero();
	}

	@FXML
	private void crearNumero(ActionEvent event) {
	    if (artistasDelNumeroActual.isEmpty()) {
	        mostrarError("Debes asignar al menos un artista al numero");
	        return;
	    }
	    try {
	        if (txtNombreNum.getText().isBlank()) {
	            mostrarError("El nombre del numero no puede estar vacio");
	            return;
	        }
	        if (txtOrden.getText().isBlank()) {
	            mostrarError("El orden no puede estar vacio");
	            return;
	        }
	        int orden;
	        try {
	            orden = Integer.parseInt(txtOrden.getText().trim());
	            if (orden <= 0) {
	                mostrarError("El orden debe ser un numero entero positivo");
	                return;
	            }
	        } catch (NumberFormatException ex) {
	            mostrarError("El orden debe ser un numero entero");
	            return;
	        }
	        double duracion = validarDuracion(txtDuracion.getText());

	        if (espectaculoSeleccionado == null) {
	         
	            for (NumeroTemp n : numerosEnMemoria) {
	                if (n.orden == orden) {
	                    mostrarError("Ya existe un numero con orden " + orden);
	                    return;
	                }
	            }
	            numerosEnMemoria.add(new NumeroTemp(
	                    orden,
	                    txtNombreNum.getText(),
	                    duracion,
	                    artistasDelNumeroActual));

	            numerosEnMemoria.sort((a, b) -> Integer.compare(a.orden, b.orden));
	            ObservableList<String> items = FXCollections.observableArrayList();
	            for (NumeroTemp n : numerosEnMemoria) {
	                items.add(n.orden + ". " + n.nombre
	                        + " (" + n.duracion + " min)"
	                        + " - " + n.artistas.size() + " artistas");
	            }
	            lvNumeros.setItems(items);
	          
	            limpiarFormularioNumero();

	        } else {
	       
	            List<Numero> numerosActuales = espectaculoService
	                    .listarNumerosDeEspectaculo(espectaculoSeleccionado.getId());
	            for (Numero n : numerosActuales) {
	                if (n.getOrden() == orden) {
	                    mostrarError("Ya existe un numero con orden " + orden);
	                    return;
	                }
	            }
	            Numero numGuardado = espectaculoService.crearNumero(
	                    String.valueOf(orden),
	                    txtNombreNum.getText(),
	                    String.valueOf(duracion),
	                    espectaculoSeleccionado);
	            espectaculoService.asignarArtistas(
	                    numGuardado.getId(), artistasDelNumeroActual);

	            mostrarInfo("Numero añadido correctamente");
	            cargarEspectaculoSeleccionado(espectaculoSeleccionado);
	        }

	    } catch (ValidacionExcepcion e) {
	        mostrarError(e.getMessage());
	    }
	}

	@FXML
	private void asignarArtistas(ActionEvent event) {
	    List<Integer> indices = lvArtistas.getSelectionModel()
	            .getSelectedIndices();
	    if (indices.isEmpty()) {
	        mostrarError("Selecciona al menos un artista");
	        return;
	    }

	    if (espectaculoSeleccionado == null) {
	        
	        for (Integer idx : indices) {
	            Artista artista = artistas.get(idx);
	            boolean yaEsta = false;
	            for (Artista a : artistasDelNumeroActual) {
	                if (a.getId().equals(artista.getId())) {
	                    yaEsta = true;
	                    break;
	                }
	            }
	            if (!yaEsta) {
	                artistasDelNumeroActual.add(artista);
	            }
	        }
	        ObservableList<String> items = FXCollections.observableArrayList();
	        for (Artista a : artistasDelNumeroActual) {
	            items.add(a.getId() + " - " + a.getNombre());
	        }
	        lvListadoArtistasParaNum.setItems(items);
	        btnCrearNum.setDisable(artistasDelNumeroActual.isEmpty());

	    } else if (numeroSeleccionado == null) {
	      
	        for (Integer idx : indices) {
	            Artista artista = artistas.get(idx);
	            boolean yaEsta = false;
	            for (Artista a : artistasDelNumeroActual) {
	                if (a.getId().equals(artista.getId())) {
	                    yaEsta = true;
	                    break;
	                }
	            }
	            if (!yaEsta) {
	                artistasDelNumeroActual.add(artista);
	            }
	        }
	        ObservableList<String> items = FXCollections.observableArrayList();
	        for (Artista a : artistasDelNumeroActual) {
	            items.add(a.getId() + " - " + a.getNombre());
	        }
	        lvListadoArtistasParaNum.setItems(items);
	        btnCrearNum.setDisable(artistasDelNumeroActual.isEmpty());

	    } else {
	       
	        try {
	            List<Artista> actuales = new ArrayList<>(
	                    numeroSeleccionado.getArtistas());
	            for (Integer idx : indices) {
	                Artista artista = artistas.get(idx);
	                boolean yaEsta = false;
	                for (Artista a : actuales) {
	                    if (a.getId().equals(artista.getId())) {
	                        yaEsta = true;
	                        break;
	                    }
	                }
	                if (!yaEsta) {
	                    actuales.add(artista);
	                }
	            }
	            espectaculoService.asignarArtistas(
	                    numeroSeleccionado.getId(), actuales);
	            mostrarInfo("Artistas asignados correctamente");
	            cargarEspectaculoSeleccionado(espectaculoSeleccionado);
	        } catch (ValidacionExcepcion e) {
	            mostrarError(e.getMessage());
	        }
	    }
	}

	@FXML
	private void atras(ActionEvent event) {

		if (espectaculoSeleccionado == null && !numerosEnMemoria.isEmpty()
				&& numerosEnMemoria.size() < 3) {
			mostrarError("No puedes salir. El espectaculo necesita "
					+ "al menos 3 numeros. Tienes: " + numerosEnMemoria.size());
			return;
		}
		if (sesionService.isAdmin()) {
			stageManager.switchScene(FxmlView.MENU_ADMIN);
		} else {
			stageManager.switchScene(FxmlView.MENU_COORDINADOR);
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
		if (idx < 0 || coordinadores == null)
			return null;
		return coordinadores.get(idx);
	}

	private void limpiarFormularioNumero() {
		txtNombreNum.clear();
		txtOrden.clear();
		txtDuracion.clear();
		artistasDelNumeroActual.clear();
		lvListadoArtistasParaNum.getItems().clear();
		lvArtistas.getSelectionModel().clearSelection();
		btnCrearNum.setDisable(true);
	}

	private void limpiarTodo() {
		txtNombreEsp.clear();
		dpFechaini.setValue(null);
		dpFechafin.setValue(null);
		cbCoordinador.setValue(null);
		lvNumeros.getItems().clear();
		numerosEnMemoria.clear();

		limpiarFormularioNumero();
	}

	private double validarDuracion(String texto) {
		if (texto == null || texto.isBlank()) {
			throw new ValidacionExcepcion("La duracion no puede estar vacia");
		}
		try {
			double duracion = Double.parseDouble(texto.replace(",", "."));
			if (duracion <= 0) {
				throw new ValidacionExcepcion(
						"La duracion debe ser mayor que 0");
			}
			double decimal = duracion - Math.floor(duracion);
			if (Math.abs(decimal - 0.0) > 0.001
					&& Math.abs(decimal - 0.5) > 0.001) {
				throw new ValidacionExcepcion(
						"La duracion solo puede tener ,0 o ,5 ");
			}
			return duracion;
		} catch (NumberFormatException e) {
			throw new ValidacionExcepcion(
					"La duracion debe ser un numero valido");
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