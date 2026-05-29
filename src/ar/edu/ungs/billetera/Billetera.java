package ar.edu.ungs.billetera;

import java.util.*;

public class Billetera implements IBilletera {

    private Map<String, Usuario> usuarios;
    private Map<String, Empresa> empresas;
    private Map<String, Cuenta> cuentasByCvu;
    private Map<String, Cuenta> cuentasByAlias;
    private List<Actividad> historialGlobal;
    private List<Cuenta> mayoresTransacciones;

    public Billetera() {
        this.usuarios = new HashMap<>();
        this.empresas = new HashMap<>();
        this.cuentasByCvu = new HashMap<>();
        this.cuentasByAlias = new HashMap<>();
        this.historialGlobal = new ArrayList<>();
        this.mayoresTransacciones = new ArrayList<>();
    }

    @Override
    public void registrarEmpresa(String cuit, String nombreFantasia, String telefono, String email, String nombreContacto) {
        if (cuit == null || cuit.trim().isEmpty() ||
            nombreFantasia == null || nombreFantasia.trim().isEmpty() ||
            telefono == null || telefono.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            nombreContacto == null || nombreContacto.trim().isEmpty()) {
            throw new IllegalArgumentException("Campos de empresa inválidos");
        }
        if (empresas.containsKey(cuit)) {
            throw new IllegalArgumentException("La empresa ya está registrada");
        }

        Empresa empresa = new Empresa(cuit, nombreFantasia, telefono, email, nombreContacto);
        empresas.put(cuit, empresa);
    }

    @Override
    public void agregarPersonaAutorizada(String cuitEmpresa, String dniAutorizado) {
        if (cuitEmpresa == null || dniAutorizado == null) {
            throw new IllegalArgumentException("Cuit o DNI nulos");
        }
        Empresa empresa = empresas.get(cuitEmpresa);
        if (empresa == null) {
            throw new IllegalArgumentException("La empresa no existe");
        }
        if (empresa.estaAutorizada(dniAutorizado)) {
            throw new IllegalArgumentException("La persona ya está autorizada");
        }
        empresa.agregarPersonaAutorizada(dniAutorizado);
    }

    @Override
    public void registrarUsuario(String dni, String nombre, String telefono, String email) {
        if (dni == null || dni.trim().isEmpty() ||
            nombre == null || nombre.trim().isEmpty() ||
            telefono == null || telefono.trim().isEmpty() ||
            email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Campos de usuario inválidos");
        }
        if (usuarios.containsKey(dni)) {
            throw new IllegalArgumentException("El usuario ya está registrado");
        }

        Usuario usuario = new Usuario(dni, nombre, telefono, email);
        usuarios.put(dni, usuario);
    }

    @Override
    public String crearCuentaRegular(String dniUsuario, String alias) {
        Usuario usuario = usuarios.get(dniUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }
        if (alias == null || alias.trim().isEmpty()) {
            throw new IllegalArgumentException("Alias inválido");
        }
        if (cuentasByAlias.containsKey(alias)) {
            throw new IllegalArgumentException("El alias ya está registrado");
        }

        String cvu = Utilitarios.generarSiguienteCvu();
        CuentaRegular cuenta = new CuentaRegular(cvu, alias, usuario);

        cuentasByCvu.put(cvu, cuenta);
        cuentasByAlias.put(alias, cuenta);
        usuario.agregarCuenta(cuenta);

        return cvu;
    }

    @Override
    public String crearCuentaPremium(String dniUsuario, String alias, double depositoInicial) {
        Usuario usuario = usuarios.get(dniUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }
        if (alias == null || alias.trim().isEmpty()) {
            throw new IllegalArgumentException("Alias inválido");
        }
        if (cuentasByAlias.containsKey(alias)) {
            throw new IllegalArgumentException("El alias ya está registrado");
        }
        if (depositoInicial < 500000.0) {
            throw new IllegalArgumentException("El depósito inicial mínimo debe ser de $500.000");
        }

        String cvu = Utilitarios.generarSiguienteCvu();
        CuentaPremium cuenta = new CuentaPremium(cvu, alias, usuario, depositoInicial);

        cuentasByCvu.put(cvu, cuenta);
        cuentasByAlias.put(alias, cuenta);
        usuario.agregarCuenta(cuenta);

        return cvu;
    }

    @Override
    public String crearCuentaCorporativa(String dniUsuario, String alias, String cuitEmpresa) {
        Usuario usuario = usuarios.get(dniUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }
        Empresa empresa = empresas.get(cuitEmpresa);
        if (empresa == null) {
            throw new IllegalArgumentException("La empresa no existe");
        }
        if (!empresa.estaAutorizada(dniUsuario)) {
            throw new IllegalArgumentException("El usuario no está autorizado para operar en nombre de esta empresa");
        }
        if (alias == null || alias.trim().isEmpty()) {
            throw new IllegalArgumentException("Alias inválido");
        }
        if (cuentasByAlias.containsKey(alias)) {
            throw new IllegalArgumentException("El alias ya está registrado");
        }

        String cvu = Utilitarios.generarSiguienteCvu();
        CuentaCorporativa cuenta = new CuentaCorporativa(cvu, alias, usuario, empresa);

        cuentasByCvu.put(cvu, cuenta);
        cuentasByAlias.put(alias, cuenta);
        usuario.agregarCuenta(cuenta);

        return cvu;
    }

    @Override
    public List<String> obtenerCuentas(String dniUsuario) {
        Usuario usuario = usuarios.get(dniUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }
        List<String> res = new ArrayList<>();
        for (Cuenta c : usuario.getCuentas()) {
            res.add(c.toString());
        }
        return res;
    }

    @Override
    public double obtenerSaldoDisponible(String cvu) {
        Cuenta cuenta = cuentasByCvu.get(cvu);
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no existe");
        }
        return cuenta.getSaldo();
    }

    @Override
    public void realizarTransferencia(String cvuOrigen, String cvuDestino, double monto) {
        Cuenta origen = cuentasByCvu.get(cvuOrigen);
        Cuenta destino = cuentasByCvu.get(cvuDestino);
        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Alguna de las cuentas no existe");
        }
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto a transferir debe ser mayor a 0");
        }

        Transferencia t = origen.transferir(destino, monto);
        historialGlobal.add(t);
    }

    @Override
    public int realizarInversionRentaFija(String dni, String cvu, double monto, int plazoDias) {
        Usuario usuario = usuarios.get(dni);
        Cuenta cuenta = cuentasByCvu.get(cvu);
        if (usuario == null || cuenta == null) {
            throw new IllegalArgumentException("El usuario o la cuenta no existe");
        }
        if (!usuario.getCuentas().contains(cuenta)) {
            throw new IllegalArgumentException("La cuenta no pertenece al usuario");
        }
        if (monto <= 0 || plazoDias <= 0) {
            throw new IllegalArgumentException("Datos de inversión inválidos");
        }

        boolean aprobada = cuenta.getSaldo() >= monto;
        InversionRentaFija inv = new InversionRentaFija(monto, plazoDias, cuenta, aprobada);

        cuenta.invertir(inv);
        historialGlobal.add(inv);

        return inv.getIdInversion();
    }

    @Override
    public int realizarInversionDivisa(String dni, String cvu, double monto, int plazoDias, String divisa, double tasa) {
        Usuario usuario = usuarios.get(dni);
        Cuenta cuenta = cuentasByCvu.get(cvu);
        if (usuario == null || cuenta == null) {
            throw new IllegalArgumentException("El usuario o la cuenta no existe");
        }
        if (!usuario.getCuentas().contains(cuenta)) {
            throw new IllegalArgumentException("La cuenta no pertenece al usuario");
        }
        if (monto <= 0 || plazoDias <= 0 || divisa == null || tasa <= 0) {
            throw new IllegalArgumentException("Datos de inversión inválidos");
        }

        boolean aprobada = cuenta.getSaldo() >= monto;
        InversionDivisa inv = new InversionDivisa(monto, plazoDias, divisa, tasa, cuenta, aprobada);

        cuenta.invertir(inv);
        historialGlobal.add(inv);

        return inv.getIdInversion();
    }

    @Override
    public int realizarInversionLiquidez(String dni, String cvu, double monto, int plazoDias) {
        Usuario usuario = usuarios.get(dni);
        Cuenta cuenta = cuentasByCvu.get(cvu);
        if (usuario == null || cuenta == null) {
            throw new IllegalArgumentException("El usuario o la cuenta no existe");
        }
        if (!usuario.getCuentas().contains(cuenta)) {
            throw new IllegalArgumentException("La cuenta no pertenece al usuario");
        }
        if (!(cuenta instanceof CuentaCorporativa)) {
            throw new IllegalArgumentException("La cuenta no es de tipo Corporativa");
        }
        if (monto < 20000000.0) {
            throw new IllegalArgumentException("El monto mínimo para Fondos de Liquidez es de $20.000.000");
        }
        if (plazoDias <= 0) {
            throw new IllegalArgumentException("Datos de inversión inválidos");
        }

        boolean aprobada = cuenta.getSaldo() >= monto;
        InversionFondoLiquidez inv = new InversionFondoLiquidez(monto, plazoDias, cuenta, aprobada);

        cuenta.invertir(inv);
        historialGlobal.add(inv);

        return inv.getIdInversion();
    }

    @Override
    public void precancelarInversion(String dni, String cvu, int idInversion) {
        Usuario usuario = usuarios.get(dni);
        Cuenta cuenta = cuentasByCvu.get(cvu);
        if (usuario == null || cuenta == null) {
            throw new IllegalArgumentException("El usuario o la cuenta no existe");
        }
        if (!usuario.getCuentas().contains(cuenta)) {
            throw new IllegalArgumentException("La cuenta no pertenece al usuario");
        }

        Inversion inversionSeleccionada = null;
        for (Inversion inv : cuenta.getInversiones()) {
            if (inv.getIdInversion() == idInversion) {
                inversionSeleccionada = inv;
                break;
            }
        }

        if (inversionSeleccionada == null || !inversionSeleccionada.isActiva()) {
            throw new IllegalArgumentException("La inversión no existe o no está activa");
        }

        double montoRetornado = inversionSeleccionada.calcularMontoPrecancelado();
        inversionSeleccionada.desactivar();
        cuenta.saldo += montoRetornado;
        cuenta.saldoInvertido -= inversionSeleccionada.getMontoInvertido();
    }

    @Override
    public String consultarCvu(String alias) {
        Cuenta cuenta = cuentasByAlias.get(alias);
        if (cuenta == null) {
            throw new IllegalArgumentException("El alias no está registrado");
        }
        return cuenta.getCvu();
    }

    @Override
    public List<String> consultarHistorialGlobal() {
        List<String> res = new ArrayList<>();
        for (Actividad act : historialGlobal) {
            res.add(act.toHistorialString());
        }
        return res;
    }

    @Override
    public List<String> consultarHistorialCuenta(String cvu) {
        Cuenta cuenta = cuentasByCvu.get(cvu);
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no existe");
        }
        List<String> res = new ArrayList<>();
        for (Actividad act : cuenta.consultarActividad()) {
            res.add(act.toHistorialString());
        }
        return res;
    }

    @Override
    public List<String> consultarHistorialUsuario(String dniUsuario) {
        Usuario usuario = usuarios.get(dniUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }
        List<String> res = new ArrayList<>();
        for (Actividad act : historialGlobal) {
            if (usuario.getCuentas().contains(act.getCuentaOrigen())) {
                res.add(act.toHistorialString());
            } else if (act instanceof Transferencia) {
                Transferencia t = (Transferencia) act;
                if (usuario.getCuentas().contains(t.getCuentaDestino())) {
                    res.add(t.toHistorialString());
                }
            }
        }
        return res;
    }

    @Override
    public double obtenerTotalInvertido(String dniUsuario) {
        Usuario usuario = usuarios.get(dniUsuario);
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no existe");
        }
        double total = 0;
        for (Cuenta cuenta : usuario.getCuentas()) {
            for (Inversion inv : cuenta.getInversiones()) {
                if (inv.isActiva()) {
                    total += inv.getMontoInvertido();
                }
            }
        }
        return total;
    }

    @Override
    public List<String> cuentasConMayorVolumen(int cantidadTop) {
        if (cantidadTop <= 0) {
            throw new IllegalArgumentException("La cantidad top debe ser positiva");
        }

        List<Cuenta> allCuentas = new ArrayList<>(cuentasByCvu.values());
        allCuentas.sort(new Comparator<Cuenta>() {
            @Override
            public int compare(Cuenta c1, Cuenta c2) {
                return Integer.compare(c2.consultarActividad().size(), c1.consultarActividad().size());
            }
        });

        int limit = Math.min(cantidadTop, allCuentas.size());
        mayoresTransacciones = new ArrayList<>();
        List<String> res = new ArrayList<>();
        for (int i = 0; i < limit; i++) {
            mayoresTransacciones.add(allCuentas.get(i));
            res.add(allCuentas.get(i).toString());
        }
        return res;
    }

    public void procesarInversionesQueVencenHoy() {
        for (Cuenta cuenta : cuentasByCvu.values()) {
            for (Inversion inv : cuenta.getInversiones()) {
                if (inv.isActiva() && (Utilitarios.hoy().isEqual(inv.getFechaCierre()) || Utilitarios.hoy().isAfter(inv.getFechaCierre()))) {
                    double finalAmount = inv.calcularMontoFinal();
                    inv.desactivar();
                    cuenta.saldo += finalAmount;
                    cuenta.saldoInvertido -= inv.getMontoInvertido();
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Usuarios ===\n");
        if (usuarios.isEmpty()) {
            sb.append("Aún no hay usuarios\n");
        } else {
            for (Usuario u : usuarios.values()) {
                sb.append("  DNI: ").append(u.getDni()).append(" | Nombre: ").append(u.getNombre()).append("\n");
            }
        }
        sb.append("=== Empresas ===\n");
        if (empresas.isEmpty()) {
            sb.append("Aún no hay empresas\n");
        } else {
            for (Empresa e : empresas.values()) {
                sb.append("  CUIT: ").append(e.getCuit()).append(" | Nombre: ").append(e.getNombreFantasia()).append("\n");
            }
        }
        sb.append("=== Cuentas ===\n");
        if (cuentasByCvu.isEmpty()) {
            sb.append("Aún no hay cuentas\n");
        } else {
            for (Cuenta c : cuentasByCvu.values()) {
                sb.append("  ").append(c.toString()).append(" | Saldo: $").append(c.getSaldo()).append("\n");
            }
        }
        return sb.toString();
    }
}
