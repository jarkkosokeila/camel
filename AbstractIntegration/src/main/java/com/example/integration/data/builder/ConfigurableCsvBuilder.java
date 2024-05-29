package com.example.integration.data.builder;

import com.example.integration.dto.Results;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class ConfigurableCsvBuilder {
    private String config = "firstname;gender;email";

    public String generateCsv(Results.Person person, CsvBuilder csvBuilder) throws InvocationTargetException, IllegalAccessException {
        String[] fields = config.split(";");
        List<Method> allMethods = Arrays.asList(CsvBuilder.class.getDeclaredMethods()).stream().filter(method -> method.getName().startsWith("add")).toList();

        for(String configField : fields) {
            String fieldName = "add" + configField;
            Optional<Method> method = allMethods.stream().filter(method1 -> method1.getName().equalsIgnoreCase(fieldName)).findFirst();
            if(method.isPresent()) {
                method.get().setAccessible(true);
                method.get().invoke(csvBuilder, person);
            }
        }

        return csvBuilder.createCsv();
    }
}
