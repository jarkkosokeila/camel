package com.example.integration.data.builder;

import com.example.integration.dto.Results;
import org.apache.commons.lang3.StringUtils;

import java.util.function.Function;

public class CsvBuilder {
    private StringBuilder stringBuilder = new StringBuilder();

    public CsvBuilder addGender(Results.Person person) {
        stringBuilder.append(addDynamicPersonColumn(person, Results.Person::getGender)).append(";");
        return this;
    }

    public CsvBuilder addEmail(Results.Person person) {
        stringBuilder.append(addDynamicPersonColumn(person, Results.Person::getEmail)).append(";");
        return this;
    }

    public CsvBuilder addFirstName(Results.Person person) {
        stringBuilder.append(addDynamicNameColumn(person.getName(), Results.Name::getFirst)).append(";");
        return this;
    }

    private String addDynamicPersonColumn(Results.Person person, Function<Results.Person, String> fun) {
        String value = fun.apply(person);
        if(StringUtils.isBlank(value)) {
            return "";
        }
        return value;
    }

    private String addDynamicNameColumn(Results.Name name, Function<Results.Name, String> fun) {
        String value = fun.apply(name);
        if(StringUtils.isBlank(value)) {
            return "";
        }
        return value;
    }

    public String createCsv() {
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
