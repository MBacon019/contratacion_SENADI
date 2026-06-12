package com.example.sistema_contratacion.service;

import com.example.sistema_contratacion.entity.ConfiguracionUmbral;
import com.example.sistema_contratacion.repository.ConfiguracionUmbralRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ConfiguracionUmbralService {

    private final ConfiguracionUmbralRepository repo;

    public ConfiguracionUmbralService(ConfiguracionUmbralRepository repo) {
        this.repo = repo;
    }

    public List<ConfiguracionUmbral> listarTodos() {
        return repo.findAll();
    }

    public Optional<ConfiguracionUmbral> obtenerActivo() {
        return repo.findByActivoTrue();
    }

    @Transactional
    public ConfiguracionUmbral crear(ConfiguracionUmbral umbral) {
        return repo.save(umbral);
    }

    @Transactional
    public Optional<ConfiguracionUmbral> actualizar(Long id, ConfiguracionUmbral datos) {
        return repo.findById(id).map(u -> {
            u.setAnio(datos.getAnio());
            u.setMontoUmbral(datos.getMontoUmbral());
            if (datos.getMontoSubastaMinimo() != null) {
                u.setMontoSubastaMinimo(datos.getMontoSubastaMinimo());
            }
            u.setDescripcion(datos.getDescripcion());
            return repo.save(u);
        });
    }

    @Transactional
    public boolean eliminar(Long id) {
        return repo.findById(id).map(u -> {
            repo.delete(u);
            return true;
        }).orElse(false);
    }

    @Transactional
    public Optional<ConfiguracionUmbral> activar(Long id) {
        // Desactiva todos primero, luego activa el seleccionado
        repo.findAll().forEach(u -> { u.setActivo(false); repo.save(u); });
        return repo.findById(id).map(u -> {
            u.setActivo(true);
            return repo.save(u);
        });
    }
}
