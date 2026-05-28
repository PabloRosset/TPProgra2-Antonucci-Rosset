public class Transferencia extends Actividad {

    private Cuenta cuentaDestino;
    private boolean aprobada;

    public Transferencia(double monto, Cuenta cuentaOrigen, Cuenta cuentaDestino, boolean aprobada) {

        super(monto, cuentaOrigen);

        this.cuentaDestino = cuentaDestino;
        this.aprobada = aprobada;
    }

    public Cuenta getCuentaDestino() {
        return cuentaDestino;
    }

    public boolean isAprobada() {
        return aprobada;
    }
}