import java.time.LocalDate;

public abstract class Inversion {

    protected static int contadorInversiones = 1;

    protected int nroInversion;

    protected double montoInvertido;
    protected double tasaInteres;

    protected LocalDate fechaConstitucion;
    protected LocalDate fechaCierre;

    protected boolean activa;

    public Inversion(double montoInvertido,
                     double tasa,
                     int plazoDias) {

        this.nroInversion = contadorInversiones++;

        this.montoInvertido = montoInvertido;
        this.tasaInteres = tasa;

        this.fechaConstitucion = Utilitarios.hoy();

        this.fechaCierre =
                fechaConstitucion.plusDays(plazoDias);

        this.activa = true;
    }

    public int getIdInversion() {
        return nroInversion;
    }

    public double getMontoInvertido() {
        return montoInvertido;
    }

    public boolean isActiva() {
        return activa;
    }

    public void desactivar() {
        activa = false;
    }

    public abstract double calcularMontoFinal();

}