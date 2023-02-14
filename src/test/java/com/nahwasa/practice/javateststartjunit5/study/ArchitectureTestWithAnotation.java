package com.nahwasa.practice.javateststartjunit5.study;

import com.nahwasa.practice.javateststartjunit5.SpringBootTestStudyApplication;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packagesOf = SpringBootTestStudyApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTestWithAnotation {

    /**
     * [ 원하는 패키지 구조 ]
     * domain 패키지에 있는 클래스는 study, member, domain에서 참조 가능
     * member 패키지에 잇는 클래스는 study와 member에서만 참조 가능
     * domain패키지는 member패키지를 참조하지 못한다.
     * study 패키지에 있는 클래스는 study에서만 참조 가능
     * 순환 참조 없어야 함
     *
     * ArchitectureTest 대신 이런식으로도 가능.
     * 단점 : DisplayName을 못줌.
     */
    @ArchTest
    ArchRule domainPackageRule = classes()
            .that().resideInAPackage("..domain..")
            .should().onlyBeAccessed().byAnyPackage("..study..", "..member..", "..domain..");

    @ArchTest
    ArchRule memberPackageRule = classes()
            .that().resideInAPackage("..member..")
            .should().onlyBeAccessed().byAnyPackage("..study..", "..member..");

    @ArchTest
    ArchRule memberPacakgeExceptRule = noClasses()
            .that().resideInAPackage("..domain..")
            .should().accessClassesThat().resideInAPackage("..member..");

    @ArchTest
    ArchRule studyPackageRule = noClasses()
            .that().resideOutsideOfPackage("..study..")
            .should().accessClassesThat().resideInAPackage("..study..");

    @ArchTest
    ArchRule freeOfCycles = slices()
            .matching("..javateststartjunit5.(*)..")
            .should().beFreeOfCycles();
}
