package com.luisdbb.tarea3AD2024base.view;

import java.util.ResourceBundle;

public enum FxmlView {
	LOGIN {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("login.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/Login.fxml";
		}
	},
	MENU_ADMIN {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("menuadmin.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/menuadmin.fxml";
		}
	}, 
	MENU_ARTISTA {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("menuArtista.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/MenuArtista.fxml";
		}
	},
	MENU_COORDINADOR {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("menuCoordinador.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/MenuCoordinador.fxml";
		}
	},
	REGISTRAR_PERSONA {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("registrarPersona.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/RegistrarPersona.fxml";
		}
	},
	REGISTRAR_ARTISTA {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("registrarArtista.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/RegistrarArtista.fxml";
		}
	},

	REGISTRAR_COORDINADOR {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("registrarCoordinador.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/RegistrarCoordinador.fxml";
		}
	},
	MODIFICAR_PERSONA {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("modificarPersona.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/ModificarPersona.fxml";
		}
	},
	GESTIONAR_ESPECTACULO {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("gestionarEspectaculo.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/GestionarEspectaculo.fxml";
		}
	},
	BUSCAR_ESPECTACULO {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("buscarEspectaculo.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/BuscarEspectaculo.fxml";
		}
	},
	VER_FICHA {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("verFicha.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/VerFicha.fxml";
		}
	},
	
	HISTORIAL_OPERACIONES {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("historialOperaciones.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/HistorialOperaciones.fxml";
		}
	},
	REGISTRAR_INCIDENCIA {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("registrarIncidencia.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/RegistrarIncidencia.fxml";
		}
	},
	GESTION_INCIDENCIAS {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("gestionIncidencias.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/GestionIncidencias.fxml";
		}
	},
	ACTUALIZAR_DOSSIER {
		@Override
		public String getTitle() {
			return getStringFromResourceBundle("actualizarDossier.title");
		}

		@Override
		public String getFxmlFile() {
			return "/fxml/ActualizarDossier.fxml";
		}
	};

	public abstract String getTitle();

	public abstract String getFxmlFile();

	String getStringFromResourceBundle(String key) {
		return ResourceBundle.getBundle("Bundle").getString(key);
	}
}
