/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.flexia;
import com.formdev.flatlaf.FlatLightLaf;

import Front_End.login;

import javax.swing.UIManager;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;

/**
 *
 * @author Karol
 */
public class FlexIA {

    public static void main(String[] args) {

        // Inicializar JavaFX Toolkit UNA vez y evitar que se apague al cerrar ventanas con JFXPanel
        // (si JavaFX se apaga, luego el WebView queda en blanco y no vuelve a cargar el video)
        new JFXPanel();
        Platform.setImplicitExit(false);

        FlatLightLaf.setup();
        UIManager.put("Button.arc", 30);
        UIManager.put("ComboBox.arc", 30);
        new login().setVisible(true);

    }
}
