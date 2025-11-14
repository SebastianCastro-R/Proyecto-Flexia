/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Back_End;

import java.awt.Font;
import java.awt.GraphicsEnvironment;

/**
 *
 * @author kfkdm
 */
public class FuenteUtil {
    public static Font cargarFuente(String nombreArchivo, float tamaño) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT,
                    FuenteUtil.class.getResourceAsStream("/fonts/" + nombreArchivo))
                    .deriveFont(tamaño);

            // Registrar la fuente
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);

            return font;
        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error, retorna una fuente por defecto
            return new Font("SansSerif", Font.PLAIN, (int) tamaño);
        }
    }
}
