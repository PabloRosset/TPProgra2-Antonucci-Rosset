package ar.edu.ungs.billetera;

import java.time.temporal.ChronoUnit;

public class InversionDivisa extends Inversion {

    private String divisa;
    private double cotizacionConstitucion;

    public InversionDivisa(double montoInvertido, int plazoDias, String divisa, double tasa, Cuenta cuentaOrigen, boolean aprobada) {
        super(montoInvertido, tasa, plazoDias, cuentaOrigen, aprobada);
        this.divisa = divisa;
        this.cotizacionConstitucion = Utilitarios.consultarCotizacion(divisa);
    }

    public String getDivisa() {
        return divisa;
    }

    @Override
    public String getTipoInversion() {
        return "Divisa";
    }

    @Override
    public double calcularMontoFinal() {
        double montoDivisa = montoInvertido / cotizacionConstitucion;
        double interesesDivisa = montoDivisa * (tasaInteres / 365.0) * plazoDias;
        double cotizacionActual = Utilitarios.consultarCotizacion(divisa);
        return (montoDivisa + interesesDivisa) * cotizacionActual;
    }

    @Override
    public double calcularMontoPrecancelado() {
        long dias = ChronoUnit.DAYS.between(fechaConstitucion, Utilitarios.hoy());
        double montoDivisa = montoInvertido / cotizacionConstitucion;
        double interesesDivisa = (montoDivisa * (tasaInteres / 365.0) * dias) / 2.0;
        double cotizacionActual = Utilitarios.consultarCotizacion(divisa);
        return (montoDivisa + interesesDivisa) * cotizacionActual;
    }
}
