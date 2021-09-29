package com.example.Authentication.shared.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

    @Autowired
    Utils utils;

    @BeforeEach
    void setUp() throws Exception{

    }

    @Test
    void generateRandomId() {
        String userId = utils.generateRandomId(30);
        String userId2 = utils.generateRandomId(30);
        assertNotNull(userId);
        assertNotNull(userId2);
        assertTrue(userId.length() == 30);
        assertTrue(!userId.equals(userId2));
    }

    @Test
    @Disabled
    void generateAddressId() {
    }

    @Test
    void hasTokenNotExpired(){
        String token = utils.generateEmailVerificationToken("213dsqwvsada");
        assertNotNull(token);

        boolean hasTokenExpired =  Utils.hasTokenExpired(token);
        assertFalse(hasTokenExpired);
    }

    @Test
    final void testHasTokenExpired(){
        String expiredToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBa21hbGlAZ21haWwuY29tIiwiZXhwIjoxNjI1NTM2MzY4fQ.7qVHrL-GkQIMVYGYsScqzgw_caJeoolh51uBQjnMIvDHDepoCH96AL-CtpninRg2L0gc23m64OmPDc0W4ageYg";
        boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);

        assertTrue(hasTokenExpired);
    }
}