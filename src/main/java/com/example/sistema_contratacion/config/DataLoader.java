package com.example.sistema_contratacion.config;

import com.example.sistema_contratacion.entity.Cpc;
import com.example.sistema_contratacion.entity.Item;
import com.example.sistema_contratacion.repository.CpcRepository;
import com.example.sistema_contratacion.repository.ItemRepository;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    private final CpcRepository cpcRepository;
    private final ItemRepository itemRepository;

    @Autowired
    public DataLoader(CpcRepository cpcRepository, ItemRepository itemRepository) {
        this.cpcRepository = cpcRepository;
        this.itemRepository = itemRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        cargarCpc();
        cargarItem();
    }

    // ── CPC (umbral_vae.csv) ─────────────────────────────────────────────────

    private void cargarCpc() {
        if (cpcRepository.count() > 0) {
            log.info("Tabla CPC ya contiene {} registros — se omite la carga inicial.", cpcRepository.count());
            return;
        }

        ClassPathResource resource = new ClassPathResource("umbral_vae.csv");
        if (!resource.exists()) {
            log.warn("umbral_vae.csv no encontrado en src/main/resources/ — tabla CPC quedará vacía.");
            return;
        }

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(false)
                .build();

        List<Cpc> registros = new ArrayList<>();
        int omitidos = 0;

        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
             CSVReader csv = new CSVReaderBuilder(reader)
                     .withCSVParser(parser)
                     .withSkipLines(1)
                     .build()) {

            String[] fila;
            int lineaNum = 1;
            while ((fila = csv.readNext()) != null) {
                lineaNum++;
                if (fila.length < 1 || fila[0].trim().isEmpty()) {
                    omitidos++;
                    continue;
                }

                Cpc cpc = new Cpc();
                cpc.setCodigo(fila[0].trim().replace("﻿", ""));
                cpc.setDescripcion(fila.length > 1 ? fila[1].trim() : "");

                if (fila.length > 2 && !fila[2].trim().isEmpty()) {
                    try {
                        String val = fila[2].trim().replace(",", ".").replaceAll("[^\\d.]", "");
                        cpc.setUmbralVae(val.isEmpty() ? null : Double.parseDouble(val));
                    } catch (NumberFormatException ex) {
                        log.warn("CPC línea {}: umbral_vae='{}' no numérico, se guarda null.", lineaNum, fila[2]);
                        cpc.setUmbralVae(null);
                    }
                }
                registros.add(cpc);
            }

            cpcRepository.saveAll(registros);
            log.info("Carga inicial CPC completada: {} registros, {} omitidos.", registros.size(), omitidos);

        } catch (CsvValidationException | IOException e) {
            log.error("Error al procesar umbral_vae.csv: {}", e.getMessage(), e);
        }
    }

    // ── ITEM (clasificador_presupuestario.csv) ───────────────────────────────
    // Columnas: codigo_completo, naturaleza, grupo, subgrupo, codigo, nombre, descripcion

    private void cargarItem() {
        if (itemRepository.count() > 0) {
            log.info("Tabla ITEM ya contiene {} registros — se omite la carga inicial.", itemRepository.count());
            return;
        }

        ClassPathResource resource = new ClassPathResource("clasificador_presupuestario.csv");
        if (!resource.exists()) {
            log.warn("clasificador_presupuestario.csv no encontrado en src/main/resources/ — tabla ITEM quedará vacía.");
            return;
        }

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(',')
                .withIgnoreQuotations(false)
                .build();

        List<Item> registros = new ArrayList<>();
        int omitidos = 0;

        try (Reader reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
             CSVReader csv = new CSVReaderBuilder(reader)
                     .withCSVParser(parser)
                     .withSkipLines(1)
                     .build()) {

            String[] fila;
            int lineaNum = 1;
            while ((fila = csv.readNext()) != null) {
                lineaNum++;
                if (fila.length < 1 || fila[0].trim().isEmpty()) {
                    omitidos++;
                    continue;
                }

                Item item = new Item();
                item.setCodigoCompleto(fila[0].trim().replace("﻿", ""));
                item.setNaturaleza(  fila.length > 1 ? fila[1].trim() : "");
                item.setGrupo(       fila.length > 2 ? fila[2].trim() : "");
                item.setSubgrupo(    fila.length > 3 ? fila[3].trim() : "");
                item.setCodigo(      fila.length > 4 ? fila[4].trim() : "");
                item.setNombre(      fila.length > 5 ? fila[5].trim() : "");
                item.setDescripcion( fila.length > 6 ? fila[6].trim() : "");

                if (item.getCodigoCompleto().isEmpty() && item.getNombre().isEmpty()) {
                    omitidos++;
                    continue;
                }
                registros.add(item);
            }

            itemRepository.saveAll(registros);
            log.info("Carga inicial ITEM completada: {} registros, {} omitidos.", registros.size(), omitidos);

        } catch (CsvValidationException | IOException e) {
            log.error("Error al procesar clasificador_presupuestario.csv: {}", e.getMessage(), e);
        }
    }
}
