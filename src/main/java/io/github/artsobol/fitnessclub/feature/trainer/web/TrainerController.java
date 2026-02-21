package io.github.artsobol.fitnessclub.feature.trainer.web;

import io.github.artsobol.fitnessclub.feature.trainer.dto.TrainerCreateRequest;
import io.github.artsobol.fitnessclub.feature.trainer.dto.TrainerResponse;
import io.github.artsobol.fitnessclub.feature.trainer.service.TrainerUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/trainers", produces = MediaType.APPLICATION_JSON_VALUE)
public class TrainerController {

    private final TrainerUseCase service;

    @PostMapping
    public ResponseEntity<TrainerResponse> create(@Valid @RequestBody TrainerCreateRequest request){
        TrainerResponse response = service.createTrainer(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrainerResponse> getById(@PathVariable("id") UUID trainerId){
        TrainerResponse response = service.getTrainerById(trainerId);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TrainerResponse>> getAll(){
        List<TrainerResponse> response = service.getTrainers();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
