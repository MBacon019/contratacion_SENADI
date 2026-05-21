package com.example.sistema_contratacion.config;

import com.example.sistema_contratacion.entity.*;
import com.example.sistema_contratacion.repository.*;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoContratoRepository tipoContratoRepository;

    public DataInitializer(RoleRepository roleRepository, UsuarioRepository usuarioRepository, TipoContratoRepository tipoContratoRepository) {
        this.roleRepository = roleRepository;
        this.usuarioRepository = usuarioRepository;
        this.tipoContratoRepository = tipoContratoRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Inicializar Rol y Usuario Admin (Lo que ya tenías)
        Optional<Role> roleOpt = roleRepository.findByNombre("ROLE_ADMIN");
        Role adminRole = roleOpt.orElseGet(() -> {
            Role nuevoRol = new Role();
            nuevoRol.setNombre("ROLE_ADMIN");
            return roleRepository.save(nuevoRol);
        });

        if (usuarioRepository.findByEmail("admin@correo.com").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setEmail("admin@correo.com");
            admin.setPassword(BCrypt.hashpw("admin123", BCrypt.gensalt()));
            admin.setNombreCompleto("Administrador del Sistema");
            admin.setActivo(true);
            admin.setRol(adminRole);
            usuarioRepository.save(admin);
        }

        // 2. Inicializar los 4 Tipos de Contrato con sus respectivos pasos secuenciales
        crearTipoContratoSiNoExiste("Contrato ÍNFIMA CUANTÍA", "Contrato estándar de relación laboral fija.", 
            Arrays.asList("Recepción de CV", "Entrevista RRHH", "Evaluación Técnica", "Firma de Contrato e Inducción"));

        crearTipoContratoSiNoExiste("Contrato CATÁLOGO ELECTRÓNICO", "Por necesidades circunstanciales o reemplazos temporales.", 
            Arrays.asList("Revisión de Perfil", "Entrevista Rápida", "Validación Legal", "Firma de Contrato Temporal"));

        crearTipoContratoSiNoExiste("Contrato SUBASTA INVERSA", "Vinculado directamente a la duración de un proyecto u obra específica.", 
            Arrays.asList("Evaluación de Portafolio/Experiencia", "Entrevista de Operaciones", "Asignación de Obra y Firma"));

        crearTipoContratoSiNoExiste("Contrato RÉGIMEN ESPECIAL", "Para actividades que se ejecutan en menos de 8 horas diarias.", 
            Arrays.asList("Filtro de Disponibilidad Horaria", "Entrevista", "Firma y Registro en el Ministerio"));
    }

    private void crearTipoContratoSiNoExiste(String nombre, String descripcion, java.util.List<String> nombresPasos) {
        if (tipoContratoRepository.findByNombre(nombre).isEmpty()) {
            TipoContrato tc = new TipoContrato();
            tc.setNombre(nombre);
            tc.setDescripcion(descripcion);

            // Añadir los pasos en orden secuencial
            for (int i = 0; i < nombresPasos.size(); i++) {
                PasoContrato paso = new PasoContrato(nombresPasos.get(i), i + 1, tc);
                tc.getPasos().add(paso);
            }
            tipoContratoRepository.save(tc);
            System.out.println("======> Inicializado: " + nombre + " con " + nombresPasos.size() + " pasos.");
        }
    }
}