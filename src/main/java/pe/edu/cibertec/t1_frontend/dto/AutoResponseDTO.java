package pe.edu.cibertec.t1_frontend.dto;

public record AutoResponseDTO(
        String codigo,
        String mensaje,
        String marca,
        String modelo,
        String nroAsientos,
        String precio,
        String color) {
}
