package com.nahwasa.practice.javateststartjunit5.study;

import com.nahwasa.practice.javateststartjunit5.SpringBootTestStudyApplication;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import jakarta.persistence.Entity;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packagesOf = SpringBootTestStudyApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class DependencyTest {

    /**
     * StudyController는 StudyService와 StudyRepository를 사용할 수 있다.
     * Study로 시작하는 클래스는 ..study.. 패키지에 있어야 한다. (도메인 등에 대한 예외 필요)
     * StudyRepository는 StudyService의 StudyController를 사용할 수 있다.
     */

    @ArchTest
    ArchRule controllerClassRule = classes().that().haveSimpleNameEndingWith("Controller")
            .should().accessClassesThat().haveSimpleNameEndingWith("Service")
            .orShould().accessClassesThat().haveSimpleNameEndingWith("Repository");

    @ArchTest
    ArchRule studyClassRule = classes().that().haveSimpleNameStartingWith("Study")
            .and().areNotEnums()    // 이런식으로 예외 걸 수 있음.
            .and().areNotAnnotatedWith(Entity.class)
            .should().resideInAPackage("..study..");

    @ArchTest
    ArchRule repositoryClassRule = noClasses().that().haveSimpleNameEndingWith("Repository")
            .should().accessClassesThat().haveSimpleNameEndingWith("Service");

}
