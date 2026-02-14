package io.github.artsobol.fitnessclub.feature.user.dto.trainer;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "trainer_specializations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrainerSpecialization {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trainer_specialization")
    @SequenceGenerator(name = "seq_trainer_specialization", sequenceName = "seq_trainer_specialization", allocationSize = 1)
    private Long id;

    @NotBlank
    @Column(name = "specialization",length = 32, nullable = false, unique = true)
    private String specialization;
}
