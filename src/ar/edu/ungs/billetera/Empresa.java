package ar.edu.ungs.billetera;

import java.util.HashSet;
import java.util.Set;

public class Empresa {
    private String cuit;
    private String nombreFantasia;
    private String telefono;
    private String email;
    private String nombreContacto;
    private Set<String> personasAutorizadas;

    public Empresa(String cuit, String nombreFantasia, String telefono, String email, String nombreContacto) {
        this.cuit = cuit;
        this.nombreFantasia = nombreFantasia;
        this.telefono = telefono;
        this.email = email;
        this.nombreContacto = nombreContacto;
        this.personasAutorizadas = new HashSet<>();
    }

    public String getCuit() {
        return cuit;
    }

    public String getNombreFantasia() {
        return nombreFantasia;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public Set<String> getPersonasAutorizadas() {
        return personasAutorizadas;
    }

    public void agregarPersonaAutorizada(String dni) {
        this.personasAutorizadas.add(dni);
    }

    public boolean estaAutorizada(String dni) {
        return this.personasAutorizadas.contains(dni);
    }
}
