package com.example.demo.service;

import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    //returns null if employee already exists or returns inserted employee if operation successful
    public Optional<Employee> insertEmployee(Employee employee){
        Optional<Employee> existingEmployee = employeeRepository.findById(employee.getId());
        if(existingEmployee.isPresent())
                return Optional.empty();

        return Optional.of(employeeRepository.insert(employee));
    }

    //returns null if employee specified doesn't already exists, returns updated employee otherwise
    public Optional<Employee> updateEmployee(Employee employee){
        Optional<Employee> existingEmployeeOptional = employeeRepository.findById(employee.getId());
        if(existingEmployeeOptional.isEmpty()){
            return Optional.empty();
        }

        return Optional.of(employeeRepository.save(employee));
    }

    //returns null if specified employee not found, returns deleted employee otherwise
    public Optional<Employee> deleteEmployee(Employee employee){
        Optional<Employee> existingEmployeeOptional = employeeRepository.findById(employee.getId());
        if(existingEmployeeOptional.isEmpty())
            return Optional.empty();

        Employee existingEmployee = existingEmployeeOptional.get();
        employeeRepository.delete(existingEmployee);
        return Optional.of(existingEmployee);
    }


}
