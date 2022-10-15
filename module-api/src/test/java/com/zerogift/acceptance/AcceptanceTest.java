package com.zerogift.acceptance;

import io.restassured.RestAssured;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import com.zerogift.utils.DataCleanUp;

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