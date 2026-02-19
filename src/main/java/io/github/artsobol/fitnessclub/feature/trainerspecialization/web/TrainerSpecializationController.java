package io.github.artsobol.fitnessclub.feature.trainerspecialization.web;

import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.request.TrainerSpecializationCreateRequest;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.response.TrainerSpecializationResponse;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.dto.request.TrainerSpecializationUpdateRequest;
import io.github.artsobol.fitnessclub.feature.trainerspecialization.service.TrainerSpecializationUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/trainers/specializations", produces = MediaType.APPLICATION_JSON_VALUE)
public class TrainerSpecializationController {

    private final TrainerSpecializationUseCase useCase;

    @GetMapping("/{id}")
    public ResponseEntity<TrainerSpecializationResponse> getById(@PathVariable Long id) {
        TrainerSpecializationResponse response = useCase.getById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TrainerSpecializationResponse>> getAll() {
        List<TrainerSpecializationResponse> response = useCase.getAll();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<TrainerSpecializationResponse> create(@RequestBody @Valid TrainerSpecializationCreateRequest request) {
        TrainerSpecializationResponse response = useCase.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TrainerSpecializationResponse> update(
            @PathVariable Long id,
            @RequestBody TrainerSpecializationUpdateRequest request
    ) {
        TrainerSpecializationResponse response = useCase.update(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        useCase.delete(id);
        return ResponseEntity.noContent().build();
    }
}
