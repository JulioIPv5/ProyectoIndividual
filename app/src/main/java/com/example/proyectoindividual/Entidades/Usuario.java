package com.example.proyectoindividual.Entidades;

public class Usuario {

    private String nombre;
    private String correoPucp;
    private String contrasena;
    private String rol;
    private Archivo[] archivo;  //Aqui van sus favoritos

    public Archivo[] getArchivo() {
        return archivo;
    }

    public void setArchivo(Archivo[] archivo) {
        this.archivo = archivo;
    }

    public Usuario() {
    }

    public Usuario(String nombre, String correoPucp, String contrasena, String rol) {
        this.nombre = nombre;
        this.correoPucp = correoPucp;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreoPucp() {
        return correoPucp;
    }

    public void setCorreoPucp(String correoPucp) {
        this.correoPucp = correoPucp;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
