/**
 *Clase ActualizarDossierController.java
 * 
 *@author Guillermo Martin Fueyo
 *@version 1.0
 */

package com.luisdbb.tarea3AD2024base.controller;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;

import com.luisdbb.tarea3AD2024base.config.StageManager;
import com.luisdbb.tarea3AD2024base.modelo.Artista;
import com.luisdbb.tarea3AD2024base.mongodb.DossierArtista;
import com.luisdbb.tarea3AD2024base.mongodb.DossierEspectaculo;
import com.luisdbb.tarea3AD2024base.mongodb.DossierEvaluacion;
import com.luisdbb.tarea3AD2024base.mongodb.DossierNumero;
import com.luisdbb.tarea3AD2024base.mongodb.DossierObservacion;
import com.luisdbb.tarea3AD2024base.services.DossierService;
import com.luisdbb.tarea3AD2024base.services.PersonaService;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

@Controller
public class ActualizarDossierController implements Initializable {

	@Lazy
	@Autowired
	private StageManager stageManager;

	@Autowired
	private DossierService dossierService;

	@Autowired
	private PersonaService personaService;

	@Autowired
	private SesionService sesionService;
	@FXML
	private Button btnAtras;
	
	@FXML
	private Button btnAñadirEvaluacion;
	
	@FXML
	private Button btnAñadirObservacion;
	
	@FXML
	private ListView<String> lvArtistas;
	@FXML
	private TextArea taDossierActual;
	@FXML
	private TextField txtComentario;
	@FXML
	private ComboBox<String> cbNivel;
	@FXML
	private TextField txtObservacion;

	private List<Artista> artistas;
	private Artista artistaSeleccionado;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cbNivel.setItems(FXCollections.observableArrayList("ALTO", "MEDIO", "BAJO"));
		cbNivel.getSelectionModel().selectFirst();

		
		artistas = personaService.listarArtistas();
		ObservableList<String> items = FXCollections.observableArrayList();
		for (Artista a : artistas) {
			items.add(a.getId() + " - " + a.getNombre());
		}
		lvArtistas.setItems(items);

		
		lvArtistas.getSelectionModel().selectedIndexProperty()
				.addListener((obs, o, n) -> {
					int idx = n.intValue();
					if (idx >= 0 && idx < artistas.size()) {
						artistaSeleccionado = artistas.get(idx);
						mostrarDossier(artistaSeleccionado.getId());
					}
				});

	}

	private void mostrarDossier(Long idArtista) {
		Optional<DossierArtista> opt = dossierService.obtenerDossier(idArtista);

		if (opt.isEmpty()) {
			taDossierActual.setText("No existe dossier para este artista");
			return;
		}

		DossierArtista d = opt.get();
		StringBuilder sb = new StringBuilder();

		sb.append("-- Datos básicos --\n");
		sb.append("Nombre: ").append(d.getNombre()).append("\n");
		sb.append("Email: ").append(d.getEmail()).append("\n");
		sb.append("Nacionalidad: ").append(d.getNacionalidad()).append("\n");
		sb.append("Especialidades: ").append(d.getEspecialidades()).append("\n");
		if (d.getApodo() != null && !d.getApodo().isBlank()) {
			sb.append("Apodo: ").append(d.getApodo()).append("\n");
		}

		sb.append("\n -- Trayectoria -- \n");
		if (d.getTrayectoria().isEmpty()) {
			sb.append("Sin trayectoria registrada\n");
		} else {
			for (DossierEspectaculo de : d.getTrayectoria()) {
				sb.append("Espectáculo: ").append(de.getNombreEspectaculo()).append(" (id:").append(de.getIdEspectaculo()).append(")\n");
				for (DossierNumero dn : de.getNumeros()) {
					sb.append("- Número: ").append(dn.getNombreNumero()).append(" (id:").append(dn.getIdNumero()).append(")\n");
				}
			}
		}

		sb.append("\n -- Evaluaciones -- \n");
		if (d.getEvaluaciones().isEmpty()) {
			sb.append("Sin evaluaciones \n");
		} else {
			for (DossierEvaluacion ev : d.getEvaluaciones()) {
				sb.append(ev.getFecha()).append(" | ").append(ev.getNivel()).append(" | ").append(ev.getComentario()).append("\n");
			}
		}

		sb.append("\n -- Observaciones -- \n");
		if (d.getObservaciones().isEmpty()) {
			sb.append("Sin observaciones \n");
		} else {
			for (DossierObservacion ob : d.getObservaciones()) {
				sb.append(ob.getFecha()).append(" | ").append(ob.getAutor()).append(": ").append(ob.getTexto()).append("\n");
			}
		}

		taDossierActual.setText(sb.toString());
	}

	@FXML
	private void añadirEvaluacion(ActionEvent event) {
		if (artistaSeleccionado == null) {
			mostrarError("Selecciona un artista primero");
			return;
		}
		if (txtComentario.getText().isBlank()) {
			mostrarError("El comentario no puede estar vacio");
			return;
		}

		String usuarioActual = sesionService.getUsuarioActual().getNombre();
		String rol = sesionService.getUsuarioActual().getPerfil().name();

		
		Long idPersona = null;
		var persona = personaService.buscarPersonaPorUsuario(usuarioActual);
		if (persona != null) {
			idPersona = persona.getId();
		} else {
			idPersona = 0L; 
		}

		dossierService.añadirEvaluacion(artistaSeleccionado.getId(),txtComentario.getText(),
				cbNivel.getSelectionModel().getSelectedItem(), idPersona, rol);

		mostrarInfo("Evaluación añadida correctamente");
		txtComentario.clear();
		mostrarDossier(artistaSeleccionado.getId());
	}

	@FXML
	private void añadirObservacion(ActionEvent event) {
		if (artistaSeleccionado == null) {
			mostrarError("Selecciona un artista primero");
			return;
		}
		if (txtObservacion.getText().isBlank()) {
			mostrarError("La observación no puede estar vacia");
			return;
		}

		String autor = sesionService.getUsuarioActual().getNombre();

		dossierService.añadirObservacion(artistaSeleccionado.getId(),
				txtObservacion.getText(), autor);

		mostrarInfo("Observacion añadida correctamente");
		txtObservacion.clear();
		mostrarDossier(artistaSeleccionado.getId());
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
