package com.example.sistema_contratacion.config;

import com.example.sistema_contratacion.entity.*;
import com.example.sistema_contratacion.repository.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoContratoRepository tipoContratoRepository;

    // Corrección: Inyectamos los 3 repositorios necesarios en el constructor
    public DataInitializer(RoleRepository roleRepository, UsuarioRepository usuarioRepository, TipoContratoRepository tipoContratoRepository) {
        this.roleRepository = roleRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoContratoRepository = tipoContratoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Inicializar Rol de forma segura usando el método exacto de tu entidad Role
        Optional<Role> roleOpt = roleRepository.findByNombre("ROLE_ADMIN");
        Role adminRole = roleOpt.orElseGet(() -> {
            Role nuevoRol = new Role();
            // Si tu método en Role.java cambia de nombre, ajústalo aquí:
            nuevoRol.setNombre("ROLE_ADMIN"); 
            return roleRepository.save(nuevoRol);
        });

        // Inicializar Administrador
        if (usuarioRepository.findByEmail("admin@correo.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setEmail("admin@correo.com");
            admin.setPassword(BCrypt.hashpw("admin123", BCrypt.gensalt()));
            admin.setNombreCompleto("Administrador del Sistema");
            admin.setActivo(true);
            admin.setRol(adminRole);
            usuarioRepository.save(admin);
        }

        // 2. Inicializar el contrato de ÍNFIMA CUANTÍA con los pasos reales de tu Excel
        inicializarInfimaCuantia();
    }

    private void inicializarInfimaCuantia() {
        if (tipoContratoRepository.findByNombre("Contrato ÍNFIMA CUANTÍA").isEmpty()) {
            TipoContrato ic = new TipoContrato();
            ic.setNombre("Contrato ÍNFIMA CUANTÍA");
            ic.setDescripcion("Procedimiento para la adquisición de bienes o prestación de servicios no normalizados.");

            // Pasos extraídos exactamente de tu matriz de Excel:
            ic.getPasos().add(new PasoContrato("Revisión de la actividad en POA", "2", "UNIDAD REQUIRENTE", "REGISTROS INTERNOS", "", ""));
            ic.getPasos().add(new PasoContrato("Solicitud de inclusión o modificación en POA", "2.1", "RESPONSABLE DE UNIDAD REQUIRENTE", "QUIPUX", "DGI", ""));
            ic.getPasos().add(new PasoContrato("Autorización y reasignación a Planificación", "2.2", "DGI", "COMENTARIO ELECTRÓNICO VÍA QUIPUX", "PLANIFICACIÓN", ""));
            ic.getPasos().add(new PasoContrato("Solicitud de Verificación Actividad en POA y disponibilidad presupuestaria", "2.3", "RESPONSABLE DE UNIDAD REQUIRENTE", "QUIPUX", "FINANCIERO / PLANIFICACIÓN", ""));
            ic.getPasos().add(new PasoContrato("Respuesta verificación de actividad y disponibilidad presupuestaria", "2.4", "DF / PLANIFICACIÓN", "QUIPUX", "FINANCIERO / PLANIFICACIÓN", ""));
            ic.getPasos().add(new PasoContrato("Elaboración de informe de necesidad para revisión y aprobación", "3", "UNIDAD/ÁREA REQUIRENTE (TÉCNICO)", "ZIMBRA", "RESPONSABLE DE UNIDAD / ÁREA REQUIRENTE", "Proyecto de Informe de Necesidad"));
            ic.getPasos().add(new PasoContrato("Revisa y aprueba Informe de Necesidad", "3.1", "RESPONSABLE DE UNIDAD / ÁREA REQUIRENTE", "ZIMBRA", "UNIDAD/ÁREA REQUIRENTE (TÉCNICO)", "Informe de necesidad aprobado"));
            ic.getPasos().add(new PasoContrato("Solicitud de Certificación (Verificación Catálogo Electrónico)", "4", "RESPONSABLE DE UNIDAD REQUIRENTE", "QUIPUX", "RESPONSABLE DA", ""));
            ic.getPasos().add(new PasoContrato("Entrega Certificación CATE (Verificación Catálogo Electrónico)", "4.1", "RESPONSABLE DA", "QUIPUX", "UNIDAD REQUIRENTE", "Certificación CATE"));
            ic.getPasos().add(new PasoContrato("Solicitud de autorización para Inicio de Etapa Preparatoria", "5", "RESPONSABLE DE UNIDAD REQUIRENTE", "QUIPUX", "DGI", "Certificación Stock / Informe de necesidad"));

            // Vinculación inversa de la relación
            for (PasoContrato p : ic.getPasos()) {
                p.setTipoContrato(ic);
            }

            tipoContratoRepository.save(ic);
        }
    }
}