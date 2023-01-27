package com.nahwasa.practice.javateststartjunit5.study;

import com.nahwasa.practice.javateststartjunit5.domain.Study;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyRepository extends JpaRepository<Study, Long> {

}