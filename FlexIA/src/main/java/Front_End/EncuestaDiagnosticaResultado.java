package Front_End;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EncuestaDiagnosticaResultado {

    private final String nombre;
    private final String riesgo; // BAJO / MEDIO / ALTO
    private final int nivelDolor; // 1-10
    private final String horasComputadorTexto;
    private final List<String> factoresClave;
    private final List<String> recomendaciones;

    public EncuestaDiagnosticaResultado(
            String nombre,
            String riesgo,
            int nivelDolor,
            String horasComputadorTexto,
            List<String> factoresClave,
            List<String> recomendaciones
    ) {
        this.nombre = nombre;
        this.riesgo = riesgo;
        this.nivelDolor = nivelDolor;
        this.horasComputadorTexto = horasComputadorTexto;
        this.factoresClave = factoresClave != null ? new ArrayList<>(factoresClave) : new ArrayList<>();
        this.recomendaciones = recomendaciones != null ? new ArrayList<>(recomendaciones) : new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getRiesgo() {
        return riesgo;
    }

    public int getNivelDolor() {
        return nivelDolor;
    }

    public String getHorasComputadorTexto() {
        return horasComputadorTexto;
    }

    public List<String> getFactoresClave() {
        return Collections.unmodifiableList(factoresClave);
    }

    public List<String> getRecomendaciones() {
        return Collections.unmodifiableList(recomendaciones);
    }
}
