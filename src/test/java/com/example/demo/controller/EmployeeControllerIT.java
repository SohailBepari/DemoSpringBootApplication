package com.example.demo.controller;

import com.example.demo.model.Address;
import com.example.demo.model.AuthenticationRequest;
import com.example.demo.model.AuthenticationResponse;
import com.example.demo.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;

    private static Employee employee3Valid;
    private static Employee employee3Invalid1;
    private static Employee employee3Invalid2;
    private static AuthenticationRequest adminCredentials;
    private static String jwt;

    @BeforeAll
    public static void beforeAll() {
        employee3Valid = new Employee("125", "XYZ", 20000, "1234567890", new Address(
                "123","123", "XYZ", "M", "MH", "123456"
        ));
        employee3Invalid1 = new Employee("125", "XYZ", 20000, "1234567890", new Address(
                "123","123", "XYZ", "M", "MH", "1234"
        ));
        employee3Invalid2 = new Employee("125", "XYZ", 20000, "1234", new Address(
                "123","123 street name XYZ", "XYZ locality near ABC",
                "Mumbai City", "MH", "123456"
        ));
        adminCredentials = new AuthenticationRequest("admin", "1234567f");

    }

    @Test
    public void getEmployeeTest() throws JSONException {
        List<Employee> response = restTemplate.getForObject("/employee", List.class);
        assertThat(response.size()).isGreaterThanOrEqualTo(1);
    }

    @Test
    public void insertEmployeeTest_Fail() throws JSONException{

        loginToAdmin();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        HttpEntity<Employee> request = new HttpEntity<>(employee3Invalid1, headers);

        ResponseEntity response  = restTemplate.postForEntity(URI.create("/employee/insert"),
                request, Object.class);

        assertThat(response.getStatusCodeValue()).isBetween(400,404);

        request = new HttpEntity<>(employee3Invalid2, headers);

        response  = restTemplate.postForEntity(URI.create("/employee/insert"),
                request, Object.class);

        assertThat(response.getStatusCodeValue()).isBetween(400,404);
    }

    @Test
    public void insertEmployeeTest_Pass() throws JSONException{
        loginToAdmin();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);

        //to set new employee id everytime test runs
        UUID uuid = UUID.randomUUID();
        employee3Valid.setId(uuid.toString());

        HttpEntity<Employee> request = new HttpEntity<>(employee3Valid, headers);
        ResponseEntity response  = restTemplate.postForEntity(URI.create("/employee/insert"),
                request, Object.class);

        assertThat(response.getStatusCodeValue()).isBetween(200,204);
    }

    @Test
    public void updateEmployeeTest_Pass() throws JSONException{

        loginToAdmin();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Employee> entity = new HttpEntity<>(employee3Valid, headers);

        ResponseEntity response = restTemplate.exchange(URI.create("/employee/update"), HttpMethod.PUT, entity, Object.class);
        assertThat(response.getStatusCodeValue()).isBetween(200,204);
    }

    @Test
    public void deleteEmployeeTest_Pass() throws JSONException{
        loginToAdmin();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Employee> entity = new HttpEntity<>(employee3Valid, headers);

        ResponseEntity response = restTemplate.exchange(URI.create("/employee/delete"), HttpMethod.DELETE, entity, Object.class);
        assertThat(response.getStatusCodeValue()).isBetween(200,204);
    }

    public void loginToAdmin(){
        ResponseEntity response  = restTemplate.postForEntity(URI.create("/user/authenticate"),
                adminCredentials, Object.class);
        String responseBody = response.getBody().toString();
        jwt = responseBody.substring(5,responseBody.length()-1);
    }
}
