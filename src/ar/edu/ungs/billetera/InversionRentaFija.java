package ar.edu.ungs.billetera;

import java.time.temporal.ChronoUnit;

public class InversionRentaFija extends Inversion {

    private static final double TASA_FIJA = 0.20;

    public InversionRentaFija(double montoInvertido, int plazoDias, Cuenta cuentaOrigen, boolean aprobada) {
        super(montoInvertido, TASA_FIJA, plazoDias, cuentaOrigen, aprobada);
    }

    @Override
    public String getTipoInversion() {
        return "Renta Fija";
    }

    @Override
    public double calcularMontoFinal() {
        double intereses = montoInvertido * (tasaInteres / 365.0) * plazoDias;
        return montoInvertido + intereses;
    }

    @Override
    public double calcularMontoPrecancelado() {
        long dias = ChronoUnit.DAYS.between(fechaConstitucion, Utilitarios.hoy());
        double intereses = montoInvertido * (tasaInteres / 365.0) * dias;
        intereses = intereses / 2.0;
        return montoInvertido + intereses;
    }
}
