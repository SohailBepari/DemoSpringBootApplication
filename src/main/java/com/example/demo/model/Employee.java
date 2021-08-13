package com.example.demo.model;

import com.example.demo.validation.AddressConstraint;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;



@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"name","salary","phoneNumber", "address"})
@Document("employee")
public class Employee {
    @Id
    @NotNull
    @NotBlank
    private String id;

    @NotNull
    @Pattern(regexp ="^[a-zA-Z]{1,20}$")
    private String name;

    @NotNull
    private Integer salary;

    @NotNull
    @Pattern(regexp ="^[0-9]{10}$")
    @Size(min=10,max=10)
    private String phoneNumber;

    @NotNull
    @Valid
    @AddressConstraint
    private Address address;

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address=" + address  +
                '}';
    }
}
