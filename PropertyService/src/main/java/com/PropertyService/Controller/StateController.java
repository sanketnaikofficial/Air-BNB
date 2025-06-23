package com.PropertyService.Controller;

import com.PropertyService.Dto.APIResponse;
import com.PropertyService.Dto.StateDto;
import com.PropertyService.Service.StateService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/property/state")
@RequiredArgsConstructor
public class StateController {
	@Autowired
    private final StateService stateService;

    @PostMapping("create-state")
    public ResponseEntity<APIResponse<StateDto>> createState(@RequestBody StateDto dto) {
        return ResponseEntity.ok(stateService.createState(dto));
    }

    @GetMapping("get-state/{id}")
    public ResponseEntity<APIResponse<StateDto>> getState(@PathVariable Long id) {
        return ResponseEntity.ok(stateService.getStateById(id));
    }

    @GetMapping("get-all-state")
    public ResponseEntity<APIResponse<List<StateDto>>> getAllStates() {
        return ResponseEntity.ok(stateService.getAllStates());
    }

    @DeleteMapping("delete-state/{id}")
    public ResponseEntity<APIResponse<String>> deleteState(@PathVariable Long id) {
        return ResponseEntity.ok(stateService.deleteState(id));
    }
}
