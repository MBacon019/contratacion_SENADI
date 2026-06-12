package com.example.sistema_contratacion.service;

import com.example.sistema_contratacion.entity.Cpc;
import com.example.sistema_contratacion.repository.CpcRepository;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CpcService {

    private static final Logger log = LoggerFactory.getLogger(CpcService.class);
    private final CpcRepository cpcRepository;

    public CpcService(CpcRepository cpcRepository) {
        this.cpcRepository = cpcRepository;
    }

    @Transactional
    public Map<String, Object> cargarDesdeCSV(MultipartFile file) throws Exception {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("El archivo está vacío.");
        }
        String nombre = file.getOriginalFilename();
        if (nombre == null || !nombre.toLowerCase().endsWith(".csv")) {
            throw new IllegalArgumentException("Solo se aceptan archivos .csv");
        }

        CSVParser csvParser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(false)
                .build();

        List<Cpc> registros = new ArrayList<>();
        int filasOmitidas = 0;

        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
             CSVReader csvReader = new CSVReaderBuilder(reader)
                     .withCSVParser(csvParser)
                     .withSkipLines(1) // salta la cabecera
                     .build()) {

            String[] fila;
            int lineaNum = 1;
            while ((fila = csvReader.readNext()) != null) {
                lineaNum++;
                if (fila.length < 2 || fila[0].trim().isEmpty()) {
                    filasOmitidas++;
                    continue;
                }

                Cpc cpc = new Cpc();
                cpc.setCodigo(fila[0].trim());
                cpc.setDescripcion(fila.length > 1 ? fila[1].trim() : "");

                if (fila.length > 2 && !fila[2].trim().isEmpty()) {
                    try {
                        String val = fila[2].trim().replace(",", ".").replaceAll("[^\\d.]", "");
                        cpc.setUmbralVae(val.isEmpty() ? null : Double.parseDouble(val));
                    } catch (NumberFormatException e) {
                        log.warn("Línea {}: no se pudo parsear umbral_vae='{}', se omite.", lineaNum, fila[2]);
                        cpc.setUmbralVae(null);
                    }
                }
                registros.add(cpc);
            }
        }

        // Reemplaza todos los registros anteriores
        cpcRepository.deleteAll();
        cpcRepository.saveAll(registros);

        log.info("CPC cargados: {}, omitidos: {}", registros.size(), filasOmitidas);
        return Map.of(
                "cargados", registros.size(),
                "omitidos", filasOmitidas,
                "mensaje", "Carga completada correctamente."
        );
    }

    public List<Cpc> buscar(String termino) {
        if (termino == null || termino.trim().length() < 2) {
            return List.of();
        }
        return cpcRepository.buscarPorTermino(termino.trim(), PageRequest.of(0, 25));
    }

    public long contarRegistros() {
        return cpcRepository.count();
    }
}
