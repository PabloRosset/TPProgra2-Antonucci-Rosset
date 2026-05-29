package ar.edu.ungs.billetera;

import java.util.ArrayList;
import java.util.List;

public class Usuario {
    private String dni;
    private String nombre;
    private String telefono;
    private String email;
    private List<Cuenta> cuentas;

    public Usuario(String dni, String nombre, String telefono, String email) {
        this.dni = dni;
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.cuentas = new ArrayList<>();
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void agregarCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
    }

    public double saldoTotal() {
        double total = 0;
        for (Cuenta c : cuentas) {
            total += c.getSaldo();
        }
        return total;
    }

    public List<Actividad> consultarActividad() {
        List<Actividad> actividades = new ArrayList<>();
        for (Cuenta c : cuentas) {
            actividades.addAll(c.consultarActividad());
        }
        return actividades;
    }

    public List<Cuenta> cuentasExistentes() {
        return cuentas;
    }
}
