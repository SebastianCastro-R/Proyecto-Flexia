/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.flexia;
import Login.login;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

/**
 *
 * @author Karol
 */
public class FlexIA {

    public static void main(String[] args) {
        
        FlatLightLaf.setup();
        UIManager.put("Button.arc", 30);
        UIManager.put("ComboBox.arc", 30);
        new login().setVisible(true);

    }
}
