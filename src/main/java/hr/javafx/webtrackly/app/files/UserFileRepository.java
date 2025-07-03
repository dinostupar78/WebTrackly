package hr.javafx.webtrackly.app.files;

import hr.javafx.webtrackly.app.enums.GenderType;
import hr.javafx.webtrackly.app.exception.RepositoryException;
import hr.javafx.webtrackly.app.model.*;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class UserFileRepository<T extends User> extends AbstractFileRepository<T> {

    private static final String USERS_FILE_PATH = "dat/users.txt";
    private static final Integer NUMBER_OF_ROWS_PER_USER = 9;

    @Override
    public T findById(Long id) {
        return findAll().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<T> findAll() {
        List<T> users = new ArrayList<>();
        try(Stream<String> stream = Files.lines(Path.of(USERS_FILE_PATH))){
            List<String> fileRows = stream.toList();

            for(Integer i = 0; i < (fileRows.size() / NUMBER_OF_ROWS_PER_USER); i++){
                Long id = Long.parseLong(fileRows.get(i * NUMBER_OF_ROWS_PER_USER));
                String firstName = fileRows.get(i * NUMBER_OF_ROWS_PER_USER + 1);
                String lastName = fileRows.get(i * NUMBER_OF_ROWS_PER_USER + 2);

                LocalDate dateOfBirth = LocalDate.parse(fileRows.get(i * NUMBER_OF_ROWS_PER_USER + 3));
                String nationality = fileRows.get(i * NUMBER_OF_ROWS_PER_USER + 4);
                GenderType gender = GenderType.valueOf(fileRows.get(i * NUMBER_OF_ROWS_PER_USER + 5));

                PersonalData personalData = new PersonalData(dateOfBirth, nationality, gender);

                String username = fileRows.get(i * NUMBER_OF_ROWS_PER_USER + 6);
                String hashedPassword = fileRows.get(i * NUMBER_OF_ROWS_PER_USER + 7);
                String roleString = fileRows.get(i * NUMBER_OF_ROWS_PER_USER + 8);

                Role role;
                if ("AdminRole".equals(roleString)) {
                    role = new AdminRole();
                } else if ("MarketingRole".equals(roleString)) {
                    role = new MarketingRole();
                } else {
                    throw new IllegalArgumentException("Unknown role: " + roleString);
                }

                User user = new User.Builder()
                        .setId(id)
                        .setName(firstName)
                        .setSurname(lastName)
                        .setPersonalData(personalData)
                        .setUsername(username)
                        .setPassword(hashedPassword)
                        .setRole(role)
                        .build();

                users.add((T) user);
            }
        } catch (Exception e) {
            throw new RepositoryException("Error while reading users from file", e);
        }

        return users;
    }

    @Override
    public void save(List<T> entities) {
        try(PrintWriter writer = new PrintWriter(USERS_FILE_PATH);) {
            for(T entity : entities){
                writer.println(entity.getId());
                writer.println(entity.getFirstName());
                writer.println(entity.getLastName());
                writer.println(entity.getPersonalData().dateOfBirth());
                writer.println(entity.getPersonalData().nationality());
                writer.println(entity.getPersonalData().gender());
                writer.println(entity.getUsername());
                writer.println(entity.getPassword());
                writer.println(entity.getRole().getClass().getSimpleName());
            }
            writer.flush();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void save(T entity) {
        List<T> entities = findAll();
        if(Optional.ofNullable(entity.getId()).isEmpty()){
            entity.setId(generateNewId());
        }

        entities.add(entity);
        save(entities);

    }

    private Long generateNewId() {
        return findAll().stream()
                .map(Entity::getId)
                .max(Long::compareTo)
                .orElse(1L) + 1;
    }


}
