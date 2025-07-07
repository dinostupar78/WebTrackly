package hr.javafx.webtrackly.app.model;

/**
 * Klasa koja predstavlja osobu u aplikaciji WebTrackly.
 * Nasljeđuje klasu Entity i sadrži osobne podatke osobe.
 */

public class Person extends Entity {
    private String firstName;
    private String lastName;
    private PersonalData personalData;

    protected Person() {
        super();
    }

    public Person(Long id, String firstName, String lastName, PersonalData personalData) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.personalData = personalData;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setSurname(String lastName) {
        this.lastName = lastName;
    }

    public PersonalData getPersonalData() {
        return personalData;
    }

    public void setPersonalData(PersonalData personalData) {
        this.personalData = personalData;
    }
}
