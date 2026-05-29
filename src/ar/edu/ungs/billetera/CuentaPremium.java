package ar.edu.ungs.billetera;

public class CuentaPremium extends Cuenta {

    public CuentaPremium(String cvu, String alias, Usuario titular, double depositoInicial) {
        super(cvu, alias, titular);
        if (depositoInicial < 500000.0) {
            throw new IllegalArgumentException("El depósito inicial mínimo para una cuenta Premium debe ser de $500.000");
        }
        this.saldo = depositoInicial;
    }

    @Override
    public String getTipo() {
        return "Premium";
    }
}
