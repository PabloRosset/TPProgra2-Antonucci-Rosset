package ar.edu.ungs.billetera;

import java.util.ArrayList;
import java.util.List;

public abstract class Cuenta {

    protected String cvu;
    protected String alias;
    protected double saldo;
    protected double saldoInvertido;
    protected double totalTransferido;
    protected Usuario titular;

    protected List<Inversion> inversiones;
    protected List<Actividad> listaActividades;

    public Cuenta(String cvu, String alias, Usuario titular) {
        this.cvu = cvu;
        this.alias = alias;
        this.titular = titular;
        this.saldo = 0;
        this.saldoInvertido = 0;
        this.totalTransferido = 0;
        this.inversiones = new ArrayList<>();
        this.listaActividades = new ArrayList<>();
    }

    public Transferencia transferir(Cuenta destino, double monto) {
        this.verificarLimiteSaliente(monto);
        destino.verificarLimiteEntrante(monto);

        boolean aprobada = false;
        if (saldo >= monto) {
            saldo -= monto;
            destino.saldo += monto;
            totalTransferido += monto;
            aprobada = true;
        }

        Transferencia t = new Transferencia(
                monto,
                this,
                destino,
                aprobada
        );

        listaActividades.add(t);
        destino.listaActividades.add(t);

        return t;
    }

    public void invertir(Inversion inversion) {
        if (inversion.isAprobada()) {
            saldo -= inversion.getMontoInvertido();
            saldoInvertido += inversion.getMontoInvertido();
            inversiones.add(inversion);
        }
        listaActividades.add(inversion);
    }

    public void registrarInversionFallida(Inversion inversion) {
        listaActividades.add(inversion);
    }

    public List<Actividad> consultarActividad() {
        return listaActividades;
    }

    public String getCvu() {
        return cvu;
    }

    public String getAlias() {
        return alias;
    }

    public double getSaldo() {
        return saldo;
    }

    public double getSaldoInvertido() {
        return saldoInvertido;
    }

    public double getTotalTransferido() {
        return totalTransferido;
    }

    public Usuario getTitular() {
        return titular;
    }

    public List<Inversion> getInversiones() {
        return inversiones;
    }

    public abstract String getTipo();

    public void verificarLimiteEntrante(double monto) throws IllegalStateException {
        // Default does nothing
    }

    public void verificarLimiteSaliente(double monto) throws IllegalStateException {
        // Default does nothing
    }

    @Override
    public String toString() {
        return getTipo() + ": " + alias + " (" + cvu + ")";
    }
}
