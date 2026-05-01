package de.tum.in.ase.eist.service;

import de.tum.in.ase.eist.model.Person;
import de.tum.in.ase.eist.util.PersonSortingOptions;
import org.springframework.stereotype.Service;

import java.util.*;

import static de.tum.in.ase.eist.util.PersonSortingOptions.SortingOrder.ASCENDING;

@Service
public class PersonService {
    // do not change this
    private final List<Person> persons;

    public PersonService() {
        this.persons = new ArrayList<>();
    }

    public Person savePerson(Person person) {
        var optionalPerson = persons.stream().filter(existingPerson -> existingPerson.getId().equals(person.getId())).findFirst();
        if (optionalPerson.isEmpty()) {
            person.setId(UUID.randomUUID());
            persons.add(person);
            return person;
        } else {
            var existingPerson = optionalPerson.get();
            existingPerson.setFirstName(person.getFirstName());
            existingPerson.setLastName(person.getLastName());
            existingPerson.setBirthday(person.getBirthday());
            return existingPerson;
        }
    }

    public void deletePerson(UUID personId) {
        this.persons.removeIf(person -> person.getId().equals(personId));
    }

    public List<Person> getAllPersons(PersonSortingOptions sortingOptions) {
        // TODO Part 3: Add sorting here
        PersonSortingOptions.SortField sortField = sortingOptions.getSortField();
        if (sortField == null) {
            sortField = PersonSortingOptions.SortField.ID;
        }
        PersonSortingOptions.SortingOrder sortingOrder = sortingOptions.getSortingOrder();
        if (sortingOrder == null) {
            sortingOrder = ASCENDING;
        }
        Comparator<Person> personComparator = null;
        switch (sortField) {
            case ID:
                personComparator = Comparator.comparing(Person::getId);
                break;
            case BIRTHDAY:
                personComparator = Comparator.comparing(Person::getBirthday);
                break;
            case LAST_NAME:
                personComparator = Comparator.comparing(Person::getLastName);
                break;
            case FIRST_NAME:
                personComparator = Comparator.comparing(Person::getFirstName);
                break;
        }
        if (sortingOrder == PersonSortingOptions.SortingOrder.DESCENDING) {
            personComparator = personComparator.reversed();
        }

        return persons.stream().sorted(personComparator).toList();

    }
}
