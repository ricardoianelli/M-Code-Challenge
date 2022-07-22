package com.mindex.challenge.adapter;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.dto.CompensationDto;
import com.mindex.challenge.utils.CompensationComparer;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.when;


@SpringBootTest
@RunWith(SpringRunner.class)
public class CompensationAdapterImplTests {

    @Autowired
    CompensationAdapter compensationAdapter;

    @MockBean
    CompensationRepository compensationRepository;

    static Compensation entity;

    @BeforeClass
    public static void prepareObjects() {
        String employeeId = "123";
        String salary = "24.90";
        LocalDate dateNow = LocalDate.now();

        entity = new Compensation(employeeId, new BigDecimal(salary), dateNow);
    }

    @Test
    @DisplayName("Convert Compensation DTO to Entity")
    public void dtoToEntity_givenAValidDto_ReturnsMatchingEntity() {
        final String employeeId = "1";
        CompensationDto dto = new CompensationDto(employeeId, entity.getSalary().toString(), entity.getEffectiveDate().toString());

        when(compensationRepository.findByEmployeeId(dto.employeeId)).thenReturn(entity);

        Compensation resultEntity = compensationAdapter.dtoToEntity(dto);

        CompensationComparer.compareEntityAndDto(resultEntity, dto);
    }

    @Test
    @DisplayName("Convert Compensation to DTO")
    public void entityToDto_givenAValidEntity_ReturnsMatchingDto() {
        CompensationDto resultDto = compensationAdapter.entityToDto(entity);
        CompensationComparer.compareEntityAndDto(entity, resultDto);
    }
}
