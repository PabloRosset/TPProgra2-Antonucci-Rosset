import ar.edu.ungs.billetera.Utilitarios;

public class InversionRentaFija extends Inversion {

    private static final double TASA_FIJA = 0.20;

    public InversionRentaFija(double montoInvertido,
                              int plazoDias,
                              Cuenta cuentaOrigen) {

        super(montoInvertido,
                TASA_FIJA,
                plazoDias,
                cuentaOrigen);
    }

    @Override
    public double calcularMontoFinal() {

        int dias =
                fechaCierre.getDayOfMonth()
                - fechaConstitucion.getDayOfMonth();

        double intereses =
                montoInvertido *
                (tasaInteres / 365)
                * dias;

        return montoInvertido + intereses;
    }

    public double calcularMontoPrecancelado() {

        int dias =
                Utilitarios.hoy().getDayOfMonth()
                - fechaConstitucion.getDayOfMonth();

        double intereses =
                montoInvertido *
                (tasaInteres / 365)
                * dias;

        intereses = intereses / 2;

        return montoInvertido + intereses;
    }

    @Override
    public String toString() {

        return "Inversion Renta Fija - $" +
                montoInvertido;
    }
}

