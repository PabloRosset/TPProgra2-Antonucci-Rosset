package ar.edu.ungs.billetera;

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

    @Override
    public String toHistorialString() {
        return "- transferencia:\n" +
               "```\n" +
               "origen: " + cuentaOrigen.getTitular().getDni() + " (" + cuentaOrigen.getCvu() + ")\n" +
               "destino: " + cuentaDestino.getTitular().getDni() + " (" + cuentaDestino.getCvu() + ")\n" +
               "monto: " + monto + "\n" +
               (aprobada ? "Aprobado" : "Rechazado") + "\n" +
               "```";
    }
}
