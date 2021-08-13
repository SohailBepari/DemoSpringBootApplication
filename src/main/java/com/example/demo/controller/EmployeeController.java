package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.ErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final ErrorService errorService;

    @Autowired
    public EmployeeController(EmployeeService employeeService, ErrorService errorService) {
        this.employeeService = employeeService;
        this.errorService = errorService;
    }

    @GetMapping("")
    public ResponseEntity<List<Employee>> getAllEmployees(){
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getAllEmployees());
    }

    //returns 400 status code in case of error, returns inserted employee with 200 status code otherwise
    @PostMapping("/insert")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity insertEmployee(@Valid @RequestBody Employee employee, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            List<String> errorMessages = errorService.getErrorMessagesFromBindingResult(bindingResult);
            return ResponseEntity.badRequest().body(errorMessages);
        }

        Optional<Employee> employeeInsertedOptional = employeeService.insertEmployee(employee);
        if(employeeInsertedOptional.isEmpty()){
            return ResponseEntity.badRequest().body("[\"Employee already present\"]");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //returns 400 status code in case of error, returns updated employee with 200 status code otherwise
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity updateEmployee(@Valid @RequestBody Employee employee, BindingResult bindingResult){

        if(bindingResult.hasErrors()){
            List<String> errorMessages = errorService.getErrorMessagesFromBindingResult(bindingResult);
            return ResponseEntity.badRequest().body(errorMessages);
        }

        Optional<Employee> employeeUpdatedOptional = employeeService.updateEmployee(employee);
        if(employeeUpdatedOptional.isEmpty()){
            return ResponseEntity.badRequest().body("[\"Employee not found\"]");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //returns 400 status code in case of error, returns deleted employee with 200 status code otherwise
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity deleteEmployee(@RequestBody Employee employee){

        Optional<Employee> employeeDeletedOptional = employeeService.deleteEmployee(employee);
        if(employeeDeletedOptional.isEmpty()){
            return ResponseEntity.badRequest().body("[\"Employee not found\"]");
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
