package ar.edu.ungs.billetera;

import java.time.LocalDate;

public abstract class Actividad {

    private static int contadorMovimientos = 1;

    protected LocalDate fecha;
    protected double monto;
    protected int nroMovimiento;
    protected Cuenta cuentaOrigen;

    public Actividad(double monto, Cuenta cuentaOrigen) {
        this.nroMovimiento = contadorMovimientos++;
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

    public int getNroMovimiento() {
        return nroMovimiento;
    }

    public Cuenta getCuentaOrigen() {
        return cuentaOrigen;
    }

    public abstract String toHistorialString();
}
