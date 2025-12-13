package Back_End;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class GoogleImageUtil {

    public static byte[] descargarImagen(String imageUrl) {
        if (imageUrl == null || imageUrl.isEmpty()) {
            return null;
        }

        try (InputStream in = new URL(imageUrl).openStream();
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            byte[] data = new byte[4096];
            int nRead;

            while ((nRead = in.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            return buffer.toByteArray();

        } catch (Exception e) {
            System.err.println("⚠️ No se pudo descargar imagen de Google");
            return null;
        }
    }
}
