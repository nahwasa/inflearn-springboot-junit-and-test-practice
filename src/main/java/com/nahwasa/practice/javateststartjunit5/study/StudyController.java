package com.nahwasa.practice.javateststartjunit5.study;

import com.nahwasa.practice.javateststartjunit5.domain.Study;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class StudyController {

    private final StudyRepository repository;

    @GetMapping("/study/{id}")
    public Study getStudy(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Study not found for '"+id+"'"));
    }

    @PostMapping("/study")
    public Study createStudy(@RequestBody Study study) {
        return repository.save(study);
    }
}
