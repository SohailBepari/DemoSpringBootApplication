package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ErrorService {

    public List<String> getErrorMessagesFromBindingResult(BindingResult bindingResult){
        return bindingResult.getAllErrors().stream()
                .map(err -> ((FieldError)err).getField() + " " + err.getDefaultMessage())
                .collect(Collectors.toList());
    }
}
