package com.example.integration.data.builder;

import com.example.integration.dto.Results;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ConfigurableCsvBuilderTest {
    @Test
    public void testConfigurableCsvBuilder() throws InvocationTargetException, IllegalAccessException {
        ConfigurableCsvBuilder builder = new ConfigurableCsvBuilder();

        Results.Person p1 = createPerson("eka", "male", "email@test");
        Results.Person p2 = createPerson("toka", "female", "email2@test");
        Results.Person p3 = createPerson("kolmas", "male", "email3@test");
        List<Results.Person> persons = List.of(p1, p2, p3);

        for (Results.Person person : persons) {
            CsvBuilder csvBuilder = new CsvBuilder();
            String csv = builder.generateCsv(person, csvBuilder);
            System.out.println(csv);
        }
    }

    private Results.Person createPerson(String first, String gender, String email) {
        Results.Person person = new Results.Person();
        Results.Name name = new Results.Name();
        name.setFirst(first);
        person.setEmail(email);
        person.setGender(gender);
        person.setName(name);

        return person;
    }
}
