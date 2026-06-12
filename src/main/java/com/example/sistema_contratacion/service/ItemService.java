package com.example.sistema_contratacion.service;

import com.example.sistema_contratacion.entity.Item;
import com.example.sistema_contratacion.repository.ItemRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> buscar(String termino) {
        if (termino == null || termino.trim().length() < 2) {
            return List.of();
        }
        return itemRepository.buscarPorTermino(termino.trim(), PageRequest.of(0, 25));
    }

    public long contarRegistros() {
        return itemRepository.count();
    }
}
