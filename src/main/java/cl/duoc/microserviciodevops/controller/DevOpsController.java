package cl.duoc.microserviciodevops.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class DevOpsController {

    // Endpoint exitoso (IE1: Monitorear Disponibilidad)
    @GetMapping("/status")
    public ResponseEntity<Map<String, String>> getStatus() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Microservicio DevOps corriendo en Spring Boot");
        return ResponseEntity.ok(response);
    }

    // Endpoint de Falla (IE1 e IE6: Para simular errores críticos)
    @GetMapping("/error-critico")
    public ResponseEntity<Map<String, String>> triggerError() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ERROR");
        response.put("message", "Falla crítica simulada en el entorno");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}