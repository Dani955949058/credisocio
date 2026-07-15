package com.cajaarequipa.credisocio.controller;

import com.cajaarequipa.credisocio.model.Prestamo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    // Cambiamos la lista a pública o accesible para que el LoginController pueda leerla o interactuar
    public static final List<Prestamo> listaPrestamos = new ArrayList<>();
    private static int contadorId = 6; // Para auto-incrementar IDs como REQ-006, REQ-007...

    static {
        // Datos iniciales
        listaPrestamos.add(new Prestamo("REQ-001", "72839401", 12500.0, "Verde", "Pendiente"));
        listaPrestamos.add(new Prestamo("REQ-002", "40381922", 45000.0, "Rojo", "Pendiente"));
        listaPrestamos.add(new Prestamo("REQ-003", "10293847", 8500.0, "Ambar", "Pendiente"));
        listaPrestamos.add(new Prestamo("REQ-004", "44556677", 22000.0, "Verde", "Aprobado"));
        listaPrestamos.add(new Prestamo("REQ-005", "11223344", 15000.0, "Rojo", "Rechazado"));
    }

    // Retornar lista completa de préstamos para otras partes de la app
    public static List<Prestamo> getListaPrestamos() {
        return listaPrestamos;
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        double montoTotal = 0;
        int pendientes = 0;

        for (Prestamo p : listaPrestamos) {
            if (p.getEstado().equalsIgnoreCase("Pendiente")) {
                montoTotal += p.getMonto();
                pendientes++;
            }
        }

        model.addAttribute("prestamos", listaPrestamos);
        model.addAttribute("totalSolicitudes", listaPrestamos.size());
        model.addAttribute("pendientes", pendientes);
        model.addAttribute("montoTotal", montoTotal);

        return "admin";
    }

    @PostMapping("/decidir")
    public String procesarDecision(@RequestParam String id, @RequestParam String accion) {
        for (Prestamo p : listaPrestamos) {
            if (p.getId().equals(id)) {
                p.setEstado(accion);
                break;
            }
        }
        return "redirect:/admin/dashboard";
    }
    // Ruta que procesa el formulario extendido del cliente
    @PostMapping("/solicitar")
    public String recibirSolicitud(
            @RequestParam String dni,
            @RequestParam String nombres,
            @RequestParam String proposito,
            @RequestParam double monto,
            @RequestParam int meses,
            @RequestParam String codigoIngresado) {

        // Validar si el código ingresado coincide con el activo
        if (!codigoIngresado.equals(LoginController.getCodigoVerificacion())) {
            // Si no coincide, devolvemos al home con un error
            return "redirect:/cliente/home?error=codigo_incorrecto";
        }

        // Definimos el riesgo según el monto
        String semaforo = "Verde";
        if (monto > 35000) {
            semaforo = "Rojo";
        } else if (monto > 15000) {
            semaforo = "Ambar";
        }

        // Crear la nueva solicitud
        String nuevoId = "REQ-0" + (contadorId++);

        // Agregamos el propósito al ID o al historial de forma visual para el administrador
        Prestamo nuevoPrestamo = new Prestamo(nuevoId, dni, monto, semaforo, "Pendiente");
        listaPrestamos.add(nuevoPrestamo);

        // Quemamos el código usado y generamos uno nuevo para la próxima transacción
        LoginController.generarNuevoCodigo();

        return "redirect:/cliente/home?success=true";
    }

}