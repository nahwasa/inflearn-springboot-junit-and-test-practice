package com.nahwasa.practice.javateststartjunit5.study;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

class ArchitectureTest {

    /**
     * [ 원하는 패키지 구조 ]
     * domain 패키지에 있는 클래스는 study, member, domain에서 참조 가능
     * member 패키지에 잇는 클래스는 study와 member에서만 참조 가능
     * domain패키지는 member패키지를 참조하지 못한다.
     * study 패키지에 있는 클래스는 study에서만 참조 가능
     * 순환 참조 없어야 함
     */

    @Test
    void packageDependencyTests() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests()) // 테스트쪽은 체크에서 제외
                .importPackages("com.nahwasa.practice.javateststartjunit5");

        // domain 패키지에 있는 클래스는 study, member, domain에서 참조 가능
        ArchRule domainPackageRule = classes()
                .that().resideInAPackage("..domain..")
                .should().onlyBeAccessed().byAnyPackage("..study..", "..member..", "..domain..");

        // member 패키지에 있는 클래스는 study와 member에서만 참조 가능
        ArchRule memberPackageRule = classes()
                .that().resideInAPackage("..member..")
                .should().onlyBeAccessed().byAnyPackage("..study..", "..member..");

        // domain패키지는 member패키지를 참조하지 못한다.
        ArchRule memberPacakgeExceptRule = noClasses()
                .that().resideInAPackage("..domain..")
                .should().accessClassesThat().resideInAPackage("..member..");

        // study 패키지에 있는 클래스는 study에서만 참조 가능 (이걸로 찾아서 StudyStatus를 domain 패키지로 옮김)
        ArchRule studyPackageRule = noClasses()
                .that().resideOutsideOfPackage("..study..")
                .should().accessClassesThat().resideInAPackage("..study..");

        // 순환 참조 없어야 함
        ArchRule freeOfCycles = slices()
                .matching("..javateststartjunit5.(*)..")
                .should().beFreeOfCycles();

        domainPackageRule.check(classes);
        memberPackageRule.check(classes);
        memberPacakgeExceptRule.check(classes);
        studyPackageRule.check(classes);
        freeOfCycles.check(classes);
    }
}
