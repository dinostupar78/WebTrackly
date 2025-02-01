package hr.javafx.webtrackly.app.model;

public class Person extends Entity {
    private String name;
    private String surname;
    private PersonalData personalData;

    public Person(Long id, String name, String surname, PersonalData personalData) {
        super(id);
        this.name = name;
        this.surname = surname;
        this.personalData = personalData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }
}
