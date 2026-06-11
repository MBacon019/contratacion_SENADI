package com.example.sistema_contratacion.controller;

import com.example.sistema_contratacion.entity.Item;
import com.example.sistema_contratacion.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/item")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/buscar")
    public List<Item> buscar(@RequestParam("q") String q) {
        return itemService.buscar(q);
    }

    @GetMapping("/total")
    public ResponseEntity<Map<String, Long>> total() {
        return ResponseEntity.ok(Map.of("total", itemService.contarRegistros()));
    }
}
