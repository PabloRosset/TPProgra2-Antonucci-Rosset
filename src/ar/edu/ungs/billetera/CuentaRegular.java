package ar.edu.ungs.billetera;

public class CuentaRegular extends Cuenta {

    public CuentaRegular(String cvu, String alias, Usuario titular) {
        super(cvu, alias, titular);
    }

    @Override
    public String getTipo() {
        return "Regular";
    }

    @Override
    public void verificarLimiteEntrante(double monto) throws IllegalStateException {
        if (this.saldo + this.saldoInvertido + monto > 5000000.0) {
            throw new IllegalStateException("La cuenta regular supera el límite máximo de $5.000.000");
        }
    }

    @Override
    public void verificarLimiteSaliente(double monto) throws IllegalStateException {
        if (monto > 5000000.0) {
            throw new IllegalStateException("La transferencia supera el límite permitido de $5.000.000");
        }
    }
}
