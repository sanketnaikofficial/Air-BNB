package com.PropertyService.Service;

import com.PropertyService.Dto.APIResponse;  
import com.PropertyService.Dto.StateDto;
import com.PropertyService.Entity.State;
import com.PropertyService.Repository.StateRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Data
@Service
@RequiredArgsConstructor
public class StateService {
	@Autowired
    private final StateRepository stateRepository;

    public APIResponse<StateDto> createState(StateDto dto) {
    	
  
        
        State state = new State();
        BeanUtils.copyProperties(dto, state, "id");
        stateRepository.save(state);
        dto.setId(state.getId());

        return new APIResponse<>("State created", 201, dto);
    }

    public APIResponse<StateDto> getStateById(Long id) {
        Optional<State> state = stateRepository.findById(id);
        if (!state.isPresent()) {
            return new APIResponse<>("State not found", 404, null);
        }

        StateDto dto = new StateDto();
        BeanUtils.copyProperties(state.get(), dto);
        return new APIResponse<>("State fetched", 200, dto);
    }

    public APIResponse<List<StateDto>> getAllStates() {
        List<StateDto> dtos = stateRepository.findAll()
                .stream()
                .map(state -> {
                    StateDto dto = new StateDto();
                    BeanUtils.copyProperties(state, dto);
                    return dto;
                }).collect(Collectors.toList());
        return new APIResponse<>("States fetched", 200, dtos);
    }

    public APIResponse<String> deleteState(Long id) {
        Optional<State> state = stateRepository.findById(id);
        if (!state.isPresent()) {
            return new APIResponse<>("State not found", 404, null);
        }
        stateRepository.delete(state.get());
        return new APIResponse<>("State deleted", 200, "Deleted");
    }
}
