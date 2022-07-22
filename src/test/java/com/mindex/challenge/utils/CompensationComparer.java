package com.mindex.challenge.utils;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.dto.CompensationDto;
import com.mindex.challenge.dto.Compensations;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CompensationComparer {
    public static void compareEntityAndDto(Compensation entity, CompensationDto dto) {
        assertAll("Should match compensation entity and DTO",
                () -> assertEquals("Salary match", entity.getSalary(), dto.salary),
                () -> assertEquals("Effective date match", entity.getEffectiveDate(), dto.effectiveDate)
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
                () -> assertEquals("Salary match", dto1.salary, dto2.salary),
                () -> assertEquals("Effective date match", dto1.effectiveDate, dto2.effectiveDate)
        );
    }

    public static void compareCompensationsWithCompensationList(Compensations compensations, List<Compensation> compensationList) {
        assertEquals(compensations.employee, compensationList.get(0).getEmployeeId());
        assertEquals(compensations.compensations.size(), compensationList.size());

        for (int i = 0; i < compensationList.size(); i++) {
            CompensationComparer.compareEntityAndDto(compensationList.get(i), compensations.compensations.get(i));
        }
    }

    public static void compareCompensationsWithCompensationDtoList(String employeeId, Compensations compensations, List<CompensationDto> compensationList) {
        assertEquals(compensations.employee, employeeId);
        assertEquals(compensations.compensations.size(), compensationList.size());

        for (int i = 0; i < compensationList.size(); i++) {
            CompensationComparer.compareDtos(compensationList.get(i), compensations.compensations.get(i));
        }
    }
}
