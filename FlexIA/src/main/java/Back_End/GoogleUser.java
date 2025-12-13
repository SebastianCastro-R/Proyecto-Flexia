package Back_End;

import com.google.api.client.util.Key;

public class GoogleUser {

    @Key
    public String sub;   // ✅ ID real de Google (OBLIGATORIO)

    @Key
    public String email;

    @Key("email_verified")
    public Boolean verifiedEmail;

    @Key
    public String name;          // Nombre completo

    @Key("given_name")
    public String givenName;     // Nombre

    @Key("family_name")
    public String familyName;    // Apellido

    @Key
    public String picture;       // URL foto perfil

    @Key
    public String locale;

    // 🔥 Método de compatibilidad
    public String getId() {
        return sub;
    }
}
