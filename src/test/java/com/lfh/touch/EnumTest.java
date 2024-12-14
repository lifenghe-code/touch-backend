package com.lfh.touch;

import com.lfh.touch.model.enums.UserRoleEnum;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class EnumTest {
    @Test
    public void EnumTest1(){
        log.info(UserRoleEnum.USER.getValue());
    }
}
