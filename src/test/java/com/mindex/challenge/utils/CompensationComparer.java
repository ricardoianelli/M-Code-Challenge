package com.mindex.challenge.utils;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.dto.CompensationDto;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CompensationComparer {
    public static void compareEntityAndDto(Compensation entity, CompensationDto dto) {
        assertAll("Should match compensation entity and DTO",
                () -> assertEquals("Employee id match", entity.getEmployeeId(), dto.employeeId),
                () -> assertEquals("Salary match", entity.getSalary().toString(), dto.salary),
                () -> assertEquals("Effective date match", entity.getEffectiveDate().toString(), dto.effectiveDate)
        );
    }

    public static void compareEntities(Compensation entity1, Compensation entity2) {
        assertAll("Should match two compensation entities",
                () -> assertEquals("Employee id match", entity1.getEmployeeId(), entity2.getEmployeeId()),
                () -> assertEquals("Salary match", entity1.getSalary(), entity2.getSalary()),
                () -> assertEquals("Effective date match", entity1.getEffectiveDate(), entity2.getEffectiveDate())
        );
    }

    public static void compareDtos(CompensationDto dto1, CompensationDto dto2) {
        assertAll("Should match compensation entity and DTO",
                () -> assertEquals("Employee id match", dto1.employeeId, dto2.employeeId),
                () -> assertEquals("Salary match", dto1.salary, dto2.salary),
                () -> assertEquals("Effective date match", dto1.effectiveDate, dto2.effectiveDate)
        );
    }
}
