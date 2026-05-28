import java.util.ArrayList;

public abstract class Cuenta {

    protected int cvu;
    protected String alias;

    protected double saldo;
    protected double saldoInvertido;
    protected double totalTransferido;

    protected ArrayList<Inversion> inversiones;
    protected ArrayList<Actividad> listaActividades;

    public Cuenta(int cvu, String alias) {

        this.cvu = cvu;
        this.alias = alias;

        saldo = 0;
        saldoInvertido = 0;
        totalTransferido = 0;

        inversiones = new ArrayList<>();
        listaActividades = new ArrayList<>();
    }

    public Transferencia transferir(Cuenta destino, double monto) {

        boolean aprobada = false;

        if(saldo >= monto) {

            saldo -= monto;
            destino.saldo += monto;

            totalTransferido += monto;

            aprobada = true;
        }

        Transferencia t = new Transferencia(
                monto,
                0,
                this,
                destino,
                aprobada
        );

        listaActividades.add(t);
        destino.listaActividades.add(t);

        return t;
    }

    public void invertir(Inversion inversion) {

        if(saldo >= inversion.getMontoInvertido()) {

            saldo -= inversion.getMontoInvertido();

            saldoInvertido += inversion.getMontoInvertido();

            inversiones.add(inversion);
        }
    }

    public ArrayList<Actividad> consultarActividad() {
        return listaActividades;
    }

    public int getCvu() {
        return cvu;
    }

    public String getAlias() {
        return alias;
    }

    public double getSaldo() {
        return saldo;
    }
}