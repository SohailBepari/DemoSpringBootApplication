package com.example.demo.service;

import com.example.demo.model.Address;
import com.example.demo.model.Employee;
import com.example.demo.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService employeeService;

    private static Employee employee1;
    private static Employee employeeSameId1;
    private static Employee employee2;
    private static Employee employee3;
    private static Employee employeeIdBlank1;

    @BeforeAll
    public static void beforeAll(){
        employee1 = new Employee("123", "XYZ", 20000, "1234", new Address());
        employeeSameId1 = new Employee("123", "ABC", 30000,"1234", new Address());
        employee2 = new Employee("124", "ABC", 30000,"1234", new Address());
        employee3 = new Employee("125", "XYZ", 20000, "1234", new Address());
        employeeIdBlank1 = new Employee("   ", "XYZ", 20000,"1234",  new Address());
    }

    @Test
    public void practiceTest(){
        Employee employee = new Employee("123", null, 20000,"1234", new Address());
        when(employeeRepository.insert(employee))
                .thenReturn(new Employee("1ac34db123", "XYZ", 2000,"1234", new Address()));

        Optional<Employee> employeeInserted = employeeService.insertEmployee(employee);
        assertTrue(employeeInserted.isPresent());

    }
    @Test
    public void getAllEmployees_DifferentID_False(){
       when(employeeRepository.findAll()).thenReturn(List.of(
               employee1,
               employee2
       ));

       List<Employee> employees = employeeService.getAllEmployees();

        assertThat(employees.get(0)).isNotEqualTo(employees.get(1));
    }

    @Test
    public void getAllEmployees_SameID_True(){
        when(employeeRepository.findAll()).thenReturn(List.of(
                employee1,
                employeeSameId1
        ));

        List<Employee> employees = employeeService.getAllEmployees();

        assertThat(employees.get(0)).isEqualTo(employees.get(1));
    }

    @Test
    public void getAllEmployees_Length_2(){
        when(employeeRepository.findAll()).thenReturn(List.of(
                employee1,
                employee2
        ));

        List<Employee> employees = employeeService.getAllEmployees();

        assertThat(employees.size()).isLessThanOrEqualTo(2);
    }

    @Test
    public void insertEmployee_employeeIdAlreadyPresent_nullOptional(){
        when(employeeRepository.findById(employee1.getId())).thenReturn(Optional.of(employee1));

        Employee employeeIdAlreadyPresent = employee1;

        Optional<Employee> employeeOptional = employeeService.insertEmployee(employeeIdAlreadyPresent);

        assertTrue(employeeOptional.isEmpty());
    }

    @Test
    public void insertEmployee_employeeIdNotPresentInDB_employeeInserted(){

        Employee employeeIdNotPresent = employee1;


        when(employeeRepository.findById("123"))
                .thenReturn(Optional.empty());

        when(employeeRepository.insert(employeeIdNotPresent)).thenReturn(employeeIdNotPresent);

        Optional<Employee> employeeInsertedOptional = employeeService.insertEmployee(employeeIdNotPresent);

        assertTrue(employeeInsertedOptional.isPresent());
        assertEquals("123", employeeInsertedOptional.get().getId());
    }

    @Test
    public void insertEmployee_employeeIdIsNull_employeeInserted(){
        Employee employeeIdNotPresent = employee3;

        when(employeeRepository.insert(employeeIdNotPresent))
                .thenReturn(new Employee("1ac34db123", "XYZ", 2000,"1234", new Address()));

        Optional<Employee> employeeInsertedOptional = employeeService.insertEmployee(employeeIdNotPresent);

        assertTrue(employeeInsertedOptional.isPresent());
        assertThat(employeeInsertedOptional.get().getId()).startsWith("1ac34");
    }

    @Test
    public void updateEmployee_employeeIdIsNull_nullOptional(){

        Employee employeeIdNull = employee3;
        Optional<Employee> employeeUpdatedOptional = employeeService.updateEmployee(employeeIdNull);

        assertTrue(employeeUpdatedOptional.isEmpty());
    }

    @Test
    public void updateEmployee_employeeNotPresent_nullOptional(){

        Employee employeeIdPresent = employee1;

        when(employeeRepository.findById(employeeIdPresent.getId()))
                .thenReturn(Optional.empty());

        Optional<Employee> employeeUpdatedOptional = employeeService.updateEmployee(employeeIdPresent);

        assertTrue(employeeUpdatedOptional.isEmpty());
    }

    @Test
    public void updateEmployee_employeePresent_employeeUpdated(){

        Employee employeeIdPresent = employee1;

        Employee employeeAlreadyPresent = new Employee("123", "ABC", 10000,"1234", new Address());

        when(employeeRepository.findById(employeeIdPresent.getId()))
                .thenReturn(Optional.of(employeeAlreadyPresent));

        when(employeeRepository.save(any())).then(returnsFirstArg());

        Optional<Employee> employeeUpdatedOptional = employeeService.updateEmployee(employeeIdPresent);

        assertTrue(employeeUpdatedOptional.isPresent());
        assertEquals("XYZ", employeeUpdatedOptional.get().getName());
    }
}
