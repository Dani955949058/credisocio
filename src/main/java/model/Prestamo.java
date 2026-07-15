package com.cajaarequipa.credisocio.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Prestamo {
    private String id;
    private String dniCliente;
    private double monto;
    private String colorSemaforo;
    private String estado;
    private String fechaHora; // Para el historial

    // Datos Hipotéticos de Pago (calculados dinámicamente)
    private int cuotasTotales = 12; // Plazo de 12 meses por defecto
    private int cuotasPagadas;
    private double tasaInteres = 0.15; // 15% de interés anual hipotético

    public Prestamo(String id, String dniCliente, double monto, String colorSemaforo, String estado) {
        this.id = id;
        this.dniCliente = dniCliente;
        this.monto = monto;
        this.colorSemaforo = colorSemaforo;
        this.estado = estado;
        this.fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        // Asignamos cuotas pagadas hipotéticas de forma aleatoria para simular progreso
        this.cuotasPagadas = (int) (Math.random() * 5) + 2; // Entre 2 y 6 cuotas ya pagadas
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDniCliente() { return dniCliente; }
    public void setDniCliente(String dniCliente) { this.dniCliente = dniCliente; }
    public double getMonto() { return monto; }
    public void setMonto(double monto) { this.monto = monto; }
    public String getColorSemaforo() { return colorSemaforo; }
    public void setColorSemaforo(String colorSemaforo) { this.colorSemaforo = colorSemaforo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) {
        this.estado = estado;
        // Actualizamos la fecha y hora de la última decisión
        this.fechaHora = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
    public String getFechaHora() { return fechaHora; }
    public int getCuotasTotales() { return cuotasTotales; }
    public int getCuotasPagadas() { return cuotasPagadas; }

    // Métodos para cálculos financieros automáticos
    public double getMontoConInteres() {
        return monto * (1 + tasaInteres);
    }
    public double getValorCuota() {
        return getMontoConInteres() / cuotasTotales;
    }
    public double getSumaPagosHechos() {
        return getValorCuota() * cuotasPagadas;
    }
    public double getSaldoPendiente() {
        return getMontoConInteres() - getSumaPagosHechos();
    }
}