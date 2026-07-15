package com.cajaarequipa.credisocio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

@Controller
public class LoginController {

    // Variables de estado en memoria para simular la sesión
    private static String usuarioNombre = "Admin";
    private static String usuarioDni = "1234";
    private static String codigoVerificacion = "";

    // Constructor que autogenera un código inicial para el usuario por defecto
    public LoginController() {
        generarNuevoCodigo();
    }

    public static void generarNuevoCodigo() {
        Random random = new Random();
        int numero = 100 + random.nextInt(900); // Genera un número de 3 dígitos (100 - 999)
        codigoVerificacion = String.valueOf(numero);
    }

    public static String getCodigoVerificacion() {
        return codigoVerificacion;
    }

    public static String getUsuarioNombre() {
        return usuarioNombre;
    }

    public static String getUsuarioDni() {
        return usuarioDni;
    }

    @GetMapping("/")
    public String mostrarLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String procesarLogin(
            @RequestParam String dni,
            @RequestParam String password,
            Model model) {

        if ("1234".equals(dni) && "user".equals(password)) {
            usuarioDni = "1234";
            usuarioNombre = "Admin";
            return "redirect:/cliente/home";
        }

        if ("admin".equals(dni) && "admin".equals(password)) {
            return "redirect:/admin/dashboard";
        }

        model.addAttribute("error", "DNI o contraseña incorrectos. Inténtalo de nuevo.");
        return "login";
    }

    // Ruta para mostrar la vista de registro
    @GetMapping("/registro")
    public String mostrarRegistro() {
        return "registro";
    }

    // Procesar el registro y generar el código de 3 dígitos
    @PostMapping("/registro")
    public String procesarRegistro(
            @RequestParam String nombres,
            @RequestParam String apellidos,
            @RequestParam String dni,
            @RequestParam String correo,
            @RequestParam String password,
            Model model) {

        // Guardamos los datos del usuario registrado en sesión
        usuarioNombre = nombres + " " + apellidos;
        usuarioDni = dni;
        generarNuevoCodigo();

        // Enviamos el código recién generado a la vista de confirmación del registro
        model.addAttribute("nombres", nombres);
        model.addAttribute("dni", dni);
        model.addAttribute("codigo", codigoVerificacion);
        model.addAttribute("success", true);

        return "registro";
    }

    @GetMapping("/cliente/home")
    public String mostrarClienteHome(
            @RequestParam(required = false) String success,
            @RequestParam(required = false) String error,
            Model model) {

        model.addAttribute("success", success);
        model.addAttribute("error", error);

        // Pasamos la información dinámica del usuario logueado
        model.addAttribute("usuarioNombre", usuarioNombre);
        model.addAttribute("usuarioDni", usuarioDni);
        model.addAttribute("codigoVerificacion", codigoVerificacion);

        // EVITAR CAÍDA SI EL ADMIN CONTROLLER NO TIENE LISTA INICIALIZADA
        try {
            model.addAttribute("listaPrestamos", AdminController.getListaPrestamos());
        } catch (Exception e) {
            System.out.println("Error al cargar préstamos: " + e.getMessage());
            model.addAttribute("listaPrestamos", new java.util.ArrayList<>()); // Mandamos lista vacía para que no explote
        }

        return "cliente_home";
    }
}