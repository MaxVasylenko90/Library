package ua.vasylenko.library.models;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class Person {
    private int id;
    @Pattern(regexp = "[A-Z]\\w+ [A-Z]\\w+", message = "Ex. \"Maks Vasylenko\"")
    @NotEmpty(message = "The name can't be empty!")
    private String name;
    @Min(value = 1900, message = "Year must be greater than 1900")
    @Max(value = 2023, message = "Year must be less than 2024")
    private int year;


    public Person(int id, String name, int year) {
        this.id = id;
        this.name = name;
        this.year = year;
    }

    public Person() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
