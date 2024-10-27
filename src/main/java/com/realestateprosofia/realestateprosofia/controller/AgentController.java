package com.realestateprosofia.realestateprosofia.controller;

import com.realestateprosofia.realestateprosofia.controller.dto.AgentDTO;
import com.realestateprosofia.realestateprosofia.service.AgentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/agents")
public class AgentController {

    private final AgentService agentService;

    @GetMapping
    public ResponseEntity<List<AgentDTO>> getAllAgents() {
        final List<AgentDTO> agents = agentService.getAllAgentsFromDTO();
        return ResponseEntity.ok(agents);
    }

    @PostMapping
    public ResponseEntity<AgentDTO> addAgent(@Valid @RequestBody final AgentDTO agentDTO) {
        agentService.addAgent(agentDTO);
        return ResponseEntity.status(201).body(agentDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgentDTO> getAgentById(@PathVariable final Long id) {
        final AgentDTO agent = agentService.getAgentDTOById(id);
        return ResponseEntity.ok(agent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgentDTO> updateAgent(@PathVariable final Long id,
                                                @Valid @RequestBody final AgentDTO agentDTO) {
        final AgentDTO updatedAgent = agentService.updateAgent(id, agentDTO);
        return ResponseEntity.ok(updatedAgent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgent(@PathVariable final Long id) {
        agentService.deleteAgent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/earnings")
    public ResponseEntity<List<AgentDTO>> getAgentsByEarnings() {
        final List<AgentDTO> agents = agentService.getAgentsSortedByEarnings();
        return ResponseEntity.ok(agents);
    }
}