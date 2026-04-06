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
        @Override public String getTitle() {
            return getStringFromResourceBundle("menuAdmin.title");
        }
        @Override public String getFxmlFile() {
            return "/fxml/MenuAdmin.fxml";
        }
    },
	 MENU_ARTISTA {
        @Override public String getTitle() {
            return getStringFromResourceBundle("menuArtista.title");
        }
        @Override public String getFxmlFile() {
            return "/fxml/MenuArtista.fxml";
        }
    },
	 MENU_COORDINADOR {
        @Override public String getTitle() {
            return getStringFromResourceBundle("menuCoordinador.title");
        }
        @Override public String getFxmlFile() {
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
	};
	

	public abstract String getTitle();

	public abstract String getFxmlFile();

	String getStringFromResourceBundle(String key) {
		return ResourceBundle.getBundle("Bundle").getString(key);
	}
}
