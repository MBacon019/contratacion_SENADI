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


        Optional<Role> roleOpt = roleRepository.findByNombre("ROLE_ADMIN");

        Role adminRole = roleOpt.orElseGet(() -> {

            Role nuevoRol = new Role();

            nuevoRol.setNombre("ROLE_ADMIN");

            return roleRepository.save(nuevoRol);
        });


        Optional<Role> userRoleOpt = roleRepository.findByNombre("ROLE_USER");

        Role userRole = userRoleOpt.orElseGet(() -> {

            Role nuevoRol = new Role();

            nuevoRol.setNombre("ROLE_USER");

            return roleRepository.save(nuevoRol);
        });


        if (usuarioRepository.findByEmail("admin@correo.com").isEmpty()) {

            Usuario admin = new Usuario();

            admin.setEmail("admin@correo.com");

            admin.setPassword(
                    BCrypt.hashpw("admin123", BCrypt.gensalt())
            );

            admin.setNombreCompleto("Administrador del Sistema");

            admin.setActivo(true);

            admin.setRol(adminRole);

            usuarioRepository.save(admin);
        }

        if (usuarioRepository.findByEmail("user@correo.com").isEmpty()) {

            Usuario user = new Usuario();

            user.setEmail("user@correo.com");

            user.setPassword(
                    BCrypt.hashpw("user123", BCrypt.gensalt())
            );

            user.setNombreCompleto("Usuario del Sistema");

            user.setActivo(true);

            user.setRol(userRole);

            usuarioRepository.save(user);
        }

        inicializarInfimaCuantia();

        // 3. Inicializar Régimen Especial con los 41 pasos completos
        inicializarRegimenEspecial();
    }

    private void inicializarRegimenEspecial() {
        Optional<TipoContrato> existente = tipoContratoRepository.findByNombre("Contrato Régimen Especial");
        TipoContrato re;

        if (existente.isPresent()) {
            re = existente.get();
            if (re.getPasos().size() > 10) return; // Ya tiene datos completos
            re.getPasos().clear();
            tipoContratoRepository.saveAndFlush(re);
        } else {
            re = new TipoContrato();
            re.setNombre("Contrato Régimen Especial");
            re.setDescripcion("Contratación bajo normas especiales para entidades exceptuadas.");
        }

        // ── FASE PREPARATORIA ──
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "Solicitud de reunión previa para coordinación, análisis preliminar y directrices del proceso a ejecutarse", "A", "UNIDAD / DIRECCIÓN REQUIRIENTE", "ZIMBRA", "UNIDAD REQUIRIENTE / DGI / DF / DA", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "Solicitar revisión de stock de bodega en el caso de bienes", "1", "RESPONSABLE UNIDAD / DIRECCIÓN REQUIRIENTE", "QUIPUX", "RESPONSABLE DA", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "1.1 – Emisión de Certificación stock de bodega", "1.1", "RESPONSABLE UNIDAD / ÁREA REQUIRIENTE", "QUIPUX", "RESPONSABLE UNIDAD/ÁREA REQUIRIENTE", "Certificación Stock"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "Revisión de la actividad en POA", "2", "RESPONSABLE UNIDAD / DIRECCIÓN REQUIRIENTE", "REGISTROS INTERNOS", "", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "2.1 – Solicitud de inclusión o modificación en POA (EN CASO DE REQUERIRSE)", "2.1", "RESPONSABLE UNIDAD / DIRECCIÓN REQUIRIENTE", "QUIPUX", "DGI", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "2.1.1 – Autorización y reasignación a Planificación", "2.1.1", "DGI", "COMENTARIO ELECTRÓNICO VÍA QUIPUX", "PLANIFICACIÓN", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "Elaboración de informe de necesidad para revisión y aprobación (Previa revisión de Compras Públicas y Control Previo).", "3", "UNIDAD/DIRECCIÓN REQUIRIENTE (TÉCNICO)", "ZIMBRA", "RESPONSABLE DE UNIDAD / ÁREA REQUIRIENTE", "Proyecto de Informe de Necesidad"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "3.1 – Revisa y aprueba Informe de Necesidad, como insumo para iniciar proceso de contratación.", "3.1", "RESPONSABLE DE UNIDAD / ÁREA REQUIRIENTE", "ZIMBRA", "UNIDAD/ÁREA REQUIRIENTE (TÉCNICO)", "Informe de necesidad aprobado"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "Solicitud de Certificación (Verificación Catálogo Electrónico)", "4", "RESPONSABLE DA", "QUIPUX", "RESPONSABLE DA", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "4.1 – Entrega Certificación CATE (Verificación Catálogo Electrónico)", "4.1", "RESPONSABLE DA", "QUIPUX", "UNIDAD REQUIRIENTE", "Certificación CATE"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "Envío de solicitud de informe de pertinencia a Contraloría General del Estado. (De ser el caso).", "5", "RESPONSABLE DA", "VENTANILLA ELECTRÓNICA DE LA CGE", "CGE", "Art. 61 y 65 del Reglamento de la LOSNCP"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "5.1 – RESPUESTA DE CONTRALORÍA GENERAL DEL ESTADO (3 opciones)", "5.1", "OGE", "MEDIOS ELECTRÓNICOS Y PÁGINA WEB INSTITUCIONAL", "DG o DGI", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "5.1.1 – Con respuesta de informe de pertinencia favorable se continúa con el proceso", "5.1.1", "OGE", "MEDIOS ELECTRÓNICOS Y PÁGINA WEB INSTITUCIONAL", "DG o DGI", "Informe de pertinencia favorable."));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "5.1.2 – Con respuesta devuelto se realizarán las correcciones solicitadas y se remite nuevamente a la CGE", "5.1.2", "OGE", "MEDIOS ELECTRÓNICOS Y PÁGINA WEB INSTITUCIONAL", "DG o DGI", "Art. 62 Reglamento LOSNCP."));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "5.1.3 – Con respuesta desfavorable, se finaliza el proceso y no se realiza la contratación.", "5.1.3", "OGE", "MEDIOS ELECTRÓNICOS Y PÁGINA WEB INSTITUCIONAL", "DG o DGI", "Informe de pertinencia desfavorable."));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "Solicitud de autorización para inicio de la etapa preparatoria", "6", "RESPONSABLE UNIDAD/DIRECCIÓN REQUIRIENTE", "QUIPUX", "DGI", "Informe de necesidad aprobado"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "6.1 – Sumilla electrónica autorización de inicio etapa preparatoria.", "6.1", "DGI", "QUIPUX", "RESPONSABLE UNIDAD/ÁREA REQUIRIENTE", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "Solicitar obtención de proformas y procesos similares.", "7", "RESPONSABLE UNIDAD / DIRECCIÓN REQUIRIENTE", "ZIMBRA", "COMPRAS PÚBLICAS", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "Envío de correo solicitando proformas y Publicaciones a la herramienta Necesidades de Contratación y Recepción de Proformas.", "8", "COMPRAS PÚBLICAS", "CORREO Y PORTAL", "SERCOP", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "Revisión y entrega de proformas (cotizaciones) y Verificación de Inhabilidades del procedimiento a contratos.", "9", "COMPRAS PÚBLICAS", "QUIPUX", "RESPONSABLE UNIDAD/ÁREA REQUIRIENTE", "Proformas / Verificación de Inhabilidades"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "Consulta de procesos similares en el portal de compras públicas y entrega de los resultados.", "10", "UNIDAD/DIRECCIÓN REQUIRIENTE (TÉCNICO)", "ZIMBRA", "RESPONSABLE UNIDAD/ÁREA REQUIRIENTE", "Informe de Proceso Similares"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "Elaboración de estudio de mercado (Previa revisión de Compras Públicas y Control Previo).", "11", "UNIDAD/DIRECCIÓN REQUIRIENTE (TÉCNICO)", "QUIPUX", "RESPONSABLE UNIDAD/ÁREA REQUIRIENTE", "Estudio de mercado debidamente legalizado"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "Elaboración y aprobación de términos de referencia o especificaciones técnicas. (Incluye presupuesto referencial, recomendación del delegado/a o comisión técnica y administrador del contrato.)", "12", "UNIDAD/DIRECCIÓN REQUIRIENTE (TÉCNICO)", "QUIPUX", "RESPONSABLE UNIDAD/ÁREA REQUIRIENTE", "Términos de referencia o especificaciones técnicas debidamente legalizadas"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "Solicitud para análisis de viabilidad para asignación, incremento-disminución presupuestaria. (EN CASO DE REQUERIRSE)", "13", "RESPONSABLE UNIDAD / DIRECCIÓN REQUIRIENTE", "QUIPUX", "DGI", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "13.1 – Informe de viabilidad favorable o no favorable. De requerirse, solicita autorización para modificación presupuestaria.", "13.1", "FINANCIERO/PLANIFICACIÓN", "QUIPUX", "DGI", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "13.2 – Traslada informe de viabilidad a la Unidad Requiriente para trámite correspondiente.", "13.2", "DGI", "QUIPUX", "FINANCIERO / DAI / UNIDAD REQUIRIENTE", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "13.3 – Solicitar Certificaciones POA, Presupuestaria y (PAC SI NO HAY MODIFICACIÓN)", "13.3", "UNIDAD / DIRECCIÓN REQUIRIENTE", "COMENTARIO ELECTRÓNICO VÍA QUIPUX", "RESPONSABLE UNIDAD/ÁREA REQUIRIENTE", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "13.4 – Reasigna pedido de Certificación a Control Interno para revisión previa.", "13.4", "UNIDAD / DIRECCIÓN REQUIRIENTE", "QUIPUX", "PLANIFICACIÓN-DP-DA", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "13.5.1 – Entrega de Certificación POA", "13.5.1", "PLANIFICACIÓN", "QUIPUX", "RESPONSABLE UNIDAD/ÁREA REQUIRIENTE", "Certificación POA"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "13.5.2 – Entrega de Certificación Presupuestaria", "13.5.2", "FINANCIERO", "QUIPUX", "RESPONSABLE UNIDAD/ÁREA REQUIRIENTE", "Certificación Presupuestaria"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "13.5.3 – Entrega de Certificación PAC (SI NO HUBO MODIFICACIÓN)", "13.5.3", "RESPONSABLE DA", "QUIPUX", "RESPONSABLE UNIDAD/ÁREA REQUIRIENTE", "Certificación PAC"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "13.6 – Procedimiento de Reforma al PAC para entrega de certificación PAC", "13.6", "UNIDAD / DIRECCIÓN REQUIRIENTE", "QUIPUX", "DA-DGI", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "13.6.1 – Solicitud de Reforma al PAC (EN CASO DE REQUERIRSE) Certificación PAC", "13.6.1", "RESPONSABLE UNIDAD/DIRECCIÓN REQUIRIENTE", "QUIPUX", "RESPONSABLE DA", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "13.6.2 – Revisa Reforma y reasigna solicitud de Resolución de Reforma a PAC hacia DGI con matriz suscrita.", "13.6.2", "RESPONSABLE DA", "QUIPUX", "DGI", "Matriz reforma suscrita y anexos de lo actuado."));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "13.6.3 – Solicitud de elaboración de Resolución de Reforma a PAC", "13.6.3", "DGI", "QUIPUX", "RESPONSABLE DAJ", "Documentación habilitante"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "13.6.4 – Elaboración y envío de Resolución Reforma PAC suscrita por DGI", "13.6.4", "RESPONSABLE DAJ", "QUIPUX", "DGI", "Resolución reforma PAC"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "13.6.5 – Emisión y entrega de Resolución Reforma PAC debidamente suscrita", "13.6.5", "DGI", "COMENTARIO ELECTRÓNICO VÍA QUIPUX", "RESPONSABLE DA", "Resolución reforma PAC"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "13.6.6 – Reasigna a Compras Públicas para publicación y ejecución de Reforma en el SOCE.", "13.6.6", "RESPONSABLE DA", "COMENTARIO ELECTRÓNICO VÍA QUIPUX", "COMPRAS PÚBLICAS", "Resolución reforma PAC"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "13.6.7 – Registro Reforma PAC en portal y elaboración de certificación PAC", "13.6.7", "COMPRAS PÚBLICAS", "PORTAL", "SERCOP", "Matriz Reforma suscrita y Resolución Reforma PAC"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "13.6.8 – Entrega Certificación PAC, luego de Reforma", "13.6.8", "RESPONSABLE DA", "QUIPUX", "UNIDAD REQUIRIENTE", "Certificación"));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "Solicitar autorización de contratación y su inicio al Portal de Compras Públicas y recomendación de delegado o miembros de la comisión técnica.", "14", "RESPONSABLE UNIDAD / DIRECCIÓN REQUIRIENTE", "QUIPUX", "UNIDAD REQUIRIENTE/ÁREA REQUIRIENTE", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "14.1 – Autorización de la contratación mediante sumilla electrónica.", "14.1", "DGI", "QUIPUX", "UNIDAD REQUIRIENTE/ÁREA REQUIRIENTE", ""));
        re.addPaso(new PasoContrato("FASE PREPARATORIA", "Entregar expediente fase preparatoria.", "15", "RESPONSABLE UNIDAD / DIRECCIÓN REQUIRIENTE", "QUIPUX", "RESPONSABLE DA", "Documentación habilitante"));

        // ── FASE PRECONTRACTUAL ──
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Reasigna expediente y solicita elaboración de Pliegos - Convocatoria.", "16", "RESPONSABLE DA", "QUIPUX", "COMPRAS PÚBLICAS", "Documentos habilitantes más Pliegos - Convocatoria"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Elaboración SOCE - Cargar Formato SOCE - Pliegos para envío forma y firma DGI", "17", "COMPRAS PÚBLICAS", "SOCE / ZIMBRA", "DGI/RESPONSABLE DA", ""));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Reasigna a la DGI solicitud para elaboración de Resolución de Inicio", "18", "RESPONSABLE DA", "QUIPUX", "DGI", "Documentación habilitante"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "18.1 – Remite solicitud de elaboración de Resolución de Inicio", "18.1", "DGI", "QUIPUX", "RESPONSABLE DAJ", "Documentación habilitante"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "18.2 – Elaboración y envío de Resolución de Inicio", "18.2", "RESPONSABLE DIRECCIÓN JURÍDICA", "QUIPUX", "DGI", "Resolución de Inicio"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "18.3 – Emisión y entrega de Resolución de Inicio debidamente suscrita", "18.3", "DGI", "COMENTARIO ELECTRÓNICO VÍA QUIPUX", "RESPONSABLE DA", "Resolución de Inicio"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "18.4 – Reasigna Resolución de Inicio a Compras Públicas", "18.4", "RESPONSABLE DA", "COMENTARIO ELECTRÓNICO VÍA QUIPUX", "COMPRAS PÚBLICAS", "Resolución de Inicio"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Publicación del proceso de contratación en el SOCE: Resolución de inicio, pliegos y demás información relevante para el proceso.", "19", "COMPRAS PÚBLICAS", "PORTAL", "COMPRAS PÚBLICAS", "Documentación habilitante"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Notificar publicación y entregar acta de contratación al servidor designado para iniciar y llevar a cabo la fase precontractual.", "20", "COMPRAS PÚBLICAS", "QUIPUX", "DELEGADO FASE PRECONTRACTUAL", "Documentación habilitante"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Audiencia de preguntas, respuestas y aclaraciones", "21", "DELEGADO/COMISIÓN TÉCNICA DE LA FASE PRECONTRACTUAL / COMPRAS PÚBLICAS", "SOCE", "", "Documentación habilitante"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Elaboración y Publicación del Acta de preguntas y respuestas", "22", "DELEGADO/COMISIÓN TÉCNICA DE LA FASE PRECONTRACTUAL / COMPRAS PÚBLICAS", "SOCE", "SERCOP", ""));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Elaboración y Publicación del Acta de Apertura de Ofertas", "23", "DELEGADO/COMISIÓN TÉCNICA DE LA FASE PRECONTRACTUAL / COMPRAS PÚBLICAS", "SOCE", "", "Documentación habilitante"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Elaboración y Publicación del Acta de convalidación de Ofertas", "24", "DELEGADO/COMISIÓN TÉCNICA DE LA FASE PRECONTRACTUAL / COMPRAS PÚBLICAS", "SOCE", "", ""));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Elaboración y Publicación de Acta de calificación de Ofertas", "25", "DELEGADO", "SOCE", "", ""));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Entrega de Informe de Recomendación de Adjudicación (con sello) para aprobación", "26", "DELEGADO PRECONTRACTUAL/COMISIÓN TÉCNICA", "QUIPUX", "DGI", "Informe de Recomendación de Adjudicación"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Aprueba el Informe de Recomendación de Adjudicación y lo reasigna para continuar con el trámite pertinente.", "27", "DGI", "QUIPUX", "RESPONSABLE DA", ""));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Reasignación del Informe de Recomendación de Adjudicación. Incluye aceptación de la recomendación del Administrador de Contrato emitida en las EETT o TDRs.", "28", "RESPONSABLE DA", "ZIMBRA", "DGI", "Documentación habilitante"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "28.1 – Solicitud de elaboración de Resolución de Adjudicación", "28.1", "DGI", "QUIPUX", "RESPONSABLE DAJ", "Documentación habilitante"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "28.2 – Elaboración y envío de Resolución de Adjudicación", "28.2", "RESPONSABLE DIRECCIÓN JURÍDICA", "QUIPUX", "DGI", "Resolución de Adjudicación"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Emisión y entrega de Resolución de Adjudicación debidamente suscrita, incluye designación del Administrador del Contrato.", "29", "DGI", "COMENTARIO ELECTRÓNICO VÍA QUIPUX", "RESPONSABLE DA", "Resolución de Inicio"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "29.1 – Reasigna Resolución de Adjudicación a Compras Públicas", "29.1", "RESPONSABLE DA", "COMENTARIO ELECTRÓNICO VÍA QUIPUX", "COMPRAS PÚBLICAS", "Resolución de Inicio"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Adjudicación en el Portal y publicación de Resolución.", "30", "COMPRAS PÚBLICAS", "PORTAL", "", ""));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Notificación de Adjudicación.", "31", "COMPRAS PÚBLICAS", "QUIPUX", "DGI", ""));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Revisión de documentación habilitante: Tabla de precios y pólizas (de ser el caso).", "32", "UNIDAD REQUIRIENTE/DA", "ZIMBRA", "OFERENTE ADJUDICADO", ""));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Solicitar la elaboración de contrato.", "33", "RESPONSABLE DA", "QUIPUX", "RESPONSABLE DAJ", ""));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "33.1 – Entrega de contrato.", "33.1", "RESPONSABLE DIRECCIÓN JURÍDICA", "QUIPUX", "DGI - AREA REQUIRIENTE", "Contrato"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "Inscripción de contrato", "34", "DGI", "ZIMBRA", "PROVEEDOR ADJUDICADO", "Contrato"));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "34.1 – Publicar: contrato y garantías", "34.1", "COMPRAS PÚBLICAS", "PORTAL", "", ""));
        re.addPaso(new PasoContrato("FASE PRECONTRACTUAL", "34.2 – Notificar Publicación de contrato", "34.2", "COMPRAS PÚBLICAS", "QUIPUX", "DGI", "Respaldo de Publicación de Contrato"));

        // ── FASE CONTRACTUAL ──
        re.addPaso(new PasoContrato("FASE CONTRACTUAL", "Notificación designación de Administrador de Contrato", "35", "DGI", "QUIPUX", "ADMINISTRADOR DE CONTRATO", "Contrato suscrito"));
        re.addPaso(new PasoContrato("FASE CONTRACTUAL", "35.1 – Entregar expediente físico-digital al Administrador de Contrato designado.", "35.1", "COMPRAS PÚBLICAS", "QUIPUX", "ADMINISTRADOR DE CONTRATO", "Expediente"));
        re.addPaso(new PasoContrato("FASE CONTRACTUAL", "Recomendación del Técnico no interviniente.", "36", "ADMINISTRADOR DE CONTRATO", "QUIPUX", "DGI", ""));
        re.addPaso(new PasoContrato("FASE CONTRACTUAL", "36.1 – Notificación designación Técnico no interviniente", "36.1", "DGI", "QUIPUX", "SERVIDOR DESIGNADO COMO TÉCNICO INTERVINIENTE", ""));
        re.addPaso(new PasoContrato("FASE CONTRACTUAL", "Supervisión de cumplimiento de contrato", "37", "ADMINISTRADOR DE CONTRATO", "CANALES DIGITALES O FÍSICOS", "PROVEEDOR", ""));
        re.addPaso(new PasoContrato("FASE CONTRACTUAL", "Elaboración y suscripción de acta entrega recepción conforme el Capítulo V del RLOSNCP", "38", "ADMINISTRADOR DE CONTRATO", "CANALES DIGITALES", "PROVEEDOR", "Acta entrega recepción"));

        // ── FASE DE EVALUACIÓN ──
        re.addPaso(new PasoContrato("FASE DE EVALUACIÓN", "Solicitud de pago.", "39", "ADMINISTRADOR DE CONTRATO", "QUIPUX", "DGI", "Acta entrega recepción, Informe de satisfacción, ingreso a bodega (en caso de bienes)"));
        re.addPaso(new PasoContrato("FASE DE EVALUACIÓN", "Verificación de la información relevante: Art. 14 del RLOSNCP y Art. 9 de la Resolución No. RE-SERCOP-2016-0000072 y su codificación", "40", "COMPRAS PÚBLICAS", "SOCE", "COMPRAS PÚBLICAS", ""));
        re.addPaso(new PasoContrato("FASE DE EVALUACIÓN", "Finalización del proceso en el SOCE", "41", "COMPRAS PÚBLICAS", "SOCE", "COMPRAS PÚBLICAS", ""));

        tipoContratoRepository.save(re);
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