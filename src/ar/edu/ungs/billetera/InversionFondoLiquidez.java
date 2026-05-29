package ar.edu.ungs.billetera;

public class InversionFondoLiquidez extends Inversion {

    private double cotizacionConstitucion;

    public InversionFondoLiquidez(double montoInvertido, int plazoDias, Cuenta cuentaOrigen, boolean aprobada) {
        super(montoInvertido, 0.0, plazoDias, cuentaOrigen, aprobada);
        this.cotizacionConstitucion = Utilitarios.consultarCotizacion("FLE");
    }

    @Override
    public String getTipoInversion() {
        return "Fondo Liquidez";
    }

    @Override
    public double calcularMontoFinal() {
        double unidades = montoInvertido / cotizacionConstitucion;
        double cotizacionActual = Utilitarios.consultarCotizacion("FLE");
        return unidades * cotizacionActual;
    }

    @Override
    public double calcularMontoPrecancelado() {
        return calcularMontoFinal();
    }
}
