package com.zerogift.backend.acceptance;

import com.zerogift.backend.utils.DataCleanUp;
import io.restassured.RestAssured;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    private DataCleanUp dataCleanUp;

    public void setUp() throws IOException {
        RestAssured.port = port;
        dataCleanUp.execute();
    }

}
