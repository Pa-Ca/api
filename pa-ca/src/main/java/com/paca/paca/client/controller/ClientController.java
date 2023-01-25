package com.paca.paca.client.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.paca.paca.client.model.Client;
import com.paca.paca.client.dto.ClientDTO;
import com.paca.paca.client.service.ClientService;
import com.paca.paca.exception.exceptions.ConflictException;
import com.paca.paca.exception.exceptions.NoContentException;

import java.util.Map;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/client")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public ResponseEntity<Map<String, List<Client>>> getAll() {
        return clientService.getAll();
    }

    @PostMapping
    public ResponseEntity<Map<String, Long>> save(@RequestBody ClientDTO client)
            throws NoContentException, ConflictException {
        return clientService.save(client);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Client>> getById(@PathVariable("id") Long id) throws NoContentException {
        return clientService.getById(id);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Map<String, Client>> getByEmail(@PathVariable("email") String email)
            throws NoContentException {
        return clientService.getByUserEmail(email);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable("id") Long id, @RequestBody ClientDTO client) throws NoContentException {
        clientService.update(id, client);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws NoContentException {
        clientService.delete(id);
    }
}
