package com.mykhailotiutiun.repcounterbot.util;

import com.mykhailotiutiun.repcounterbot.util.impl.LocalDateWeekUtilImpl;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class LocalDateWeekUtilImplTest {

    private final LocalDateWeekUtilImpl localDateWeekUtil = new LocalDateWeekUtilImpl();

    @Test
    public void getFirstDateOfWeekFromDate(){
        LocalDate firstDoW = LocalDate.of(2024, 8, 12);
        LocalDate middleDoW = LocalDate.of(2024, 8, 15);
        LocalDate lastDow = LocalDate.of(2024, 8, 18);

        assertEquals(firstDoW, localDateWeekUtil.getFirstDateOfWeekFromDate(firstDoW));
        assertEquals(firstDoW, localDateWeekUtil.getFirstDateOfWeekFromDate(middleDoW));
        assertEquals(firstDoW, localDateWeekUtil.getFirstDateOfWeekFromDate(lastDow));
    }

    @Test
    public void getLastDateOfWeekFromDate(){
        LocalDate firstDoW = LocalDate.of(2024, 8, 12);
        LocalDate middleDoW = LocalDate.of(2024, 8, 15);
        LocalDate lastDow = LocalDate.of(2024, 8, 18);

        assertEquals(lastDow, localDateWeekUtil.getLastDateOfWeekFromDate(firstDoW));
        assertEquals(lastDow, localDateWeekUtil.getLastDateOfWeekFromDate(middleDoW));
        assertEquals(lastDow, localDateWeekUtil.getLastDateOfWeekFromDate(lastDow));
    }

    @Test
    public void isCurrentWeek(){
        assertFalse(localDateWeekUtil.isCurrentWeek(
                localDateWeekUtil.getFirstDateOfWeekFromDate(LocalDate.now().minusDays(10)),
                localDateWeekUtil.getLastDateOfWeekFromDate(LocalDate.now().minusDays(10))
        ));
        assertTrue(localDateWeekUtil.isCurrentWeek(
                localDateWeekUtil.getFirstDateOfWeekFromDate(LocalDate.now()),
                localDateWeekUtil.getLastDateOfWeekFromDate(LocalDate.now())
        ));
        assertFalse(localDateWeekUtil.isCurrentWeek(
                localDateWeekUtil.getFirstDateOfWeekFromDate(LocalDate.now().plusDays(10)),
                localDateWeekUtil.getLastDateOfWeekFromDate(LocalDate.now().plusDays(10))
        ));
    }
}
