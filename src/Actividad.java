import java.time.LocalDate;

public abstract class Actividad {

    protected LocalDate fecha;
    protected double monto;
    protected Cuenta cuentaOrigen;

    public Actividad(double monto, Cuenta cuentaOrigen) {

        this.fecha = Utilitarios.hoy();

        this.monto = monto;
        this.cuentaOrigen = cuentaOrigen;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getMonto() {
        return monto;
    }

    public Cuenta getCuentaOrigen() {
        return cuentaOrigen;
    }
}