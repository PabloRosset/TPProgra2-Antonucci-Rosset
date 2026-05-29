package ar.edu.ungs.billetera;

import java.time.LocalDate;

public abstract class Inversion extends Actividad {

    protected static int contadorInversiones = 1;

    protected int nroInversion;
    protected double montoInvertido;
    protected double tasaInteres;
    protected int plazoDias;
    protected LocalDate fechaConstitucion;
    protected LocalDate fechaCierre;
    protected boolean activa;
    protected boolean aprobada;

    public Inversion(double montoInvertido, double tasa, int plazoDias, Cuenta cuentaOrigen, boolean aprobada) {
        super(montoInvertido, cuentaOrigen);
        this.nroInversion = contadorInversiones++;
        this.montoInvertido = montoInvertido;
        this.tasaInteres = tasa;
        this.plazoDias = plazoDias;
        this.fechaConstitucion = Utilitarios.hoy();
        this.fechaCierre = this.fechaConstitucion.plusDays(plazoDias);
        this.activa = aprobada;
        this.aprobada = aprobada;
    }

    public int getIdInversion() {
        return nroInversion;
    }

    public double getMontoInvertido() {
        return montoInvertido;
    }

    public double getTasaInteres() {
        return tasaInteres;
    }

    public int getPlazoDias() {
        return plazoDias;
    }

    public LocalDate getFechaConstitucion() {
        return fechaConstitucion;
    }

    public LocalDate getFechaCierre() {
        return fechaCierre;
    }

    public boolean isActiva() {
        return activa;
    }

    public boolean isAprobada() {
        return aprobada;
    }

    public void desactivar() {
        this.activa = false;
    }

    public abstract String getTipoInversion();

    public abstract double calcularMontoFinal();

    public abstract double calcularMontoPrecancelado();

    @Override
    public String toHistorialString() {
        return "- inversion:\n" +
               "```\n" +
               "origen: " + cuentaOrigen.getTitular().getDni() + " (" + cuentaOrigen.getCvu() + ")\n" +
               "desc: " + getTipoInversion() + "\n" +
               "monto: " + montoInvertido + "\n" +
               "plazo: " + plazoDias + "\n" +
               (aprobada ? "Aprobado" : "Rechazado") + "\n" +
               "```";
    }
}
