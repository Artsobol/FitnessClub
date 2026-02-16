package io.github.artsobol.fitnessclub.feature.trainerspecialization.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "trainer_specializations")
@NoArgsConstructor
public class TrainerSpecialization {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_trainer_specialization")
    @SequenceGenerator(name = "seq_trainer_specialization", sequenceName = "seq_trainer_specialization", allocationSize = 1)
    private Long id;

    @NotBlank
    @Column(name = "title",length = 32, nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private boolean active = true;

    public void deactivate() {
        this.active = false;
    }

}
