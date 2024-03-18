import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        List<Department> departments = new ArrayList<>();
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            departments.add(new Department(
                    "Department #" + i
            ));
        }
        for (int i = 0; i < 10; i++) {
            persons.add(new Person(
                    "Person #" + random.nextInt(10, 100),
                    ThreadLocalRandom.current().nextInt(20, 61),
                    ThreadLocalRandom.current().nextInt(20_000, 100_000) * 1.0,
                    departments.get(ThreadLocalRandom.current().nextInt(departments.size()))
            ));
        }

        // Задание 1.
        printNamesOrdered(persons);
        System.out.println("-------------------------------------------------------");
        //Задание 2.
        System.out.println(printDepartmentOldestPerson(persons));
        System.out.println("-------------------------------------------------------");
        // Задание 3.
        System.out.println(findFirstPersons(persons));
        System.out.println("-------------------------------------------------------");
        // Задание 4.
        System.out.println(showDepsAndMaxSalary(persons));
        System.out.println(findTopDepartment(persons));

    }

    // отсортировать сотрудников в алфавитном порядке
    static public void printNamesOrdered(List<Person> persons) {
        persons.stream()
                .sorted(Comparator.comparing(Person::getName))
                .forEach(person -> System.out.println(person.getName()));
    }

    // В каждом департаменте найти самого взрослого сотрудника.
    //   * Вывести на консоль мапипнг department -> personName
    static public Map<Department, Person> printDepartmentOldestPerson(List<Person> persons) {
        Comparator<Person> ageComparator = Comparator.comparing(person -> person.getAge());
        Map<Department,Person> oldestInDeps = persons.stream()
                .collect(Collectors.toMap(Person::getDepartment, person -> person, (first, second) -> {
                    if(ageComparator.compare(first, second) > 0)
                        return first;
                    else return second;
                }));
        return oldestInDeps;
    }

    // Найти 10 первых сотрудников, младше 30 лет, у которых зарплата выше 50_000
    static public List<Person> findFirstPersons(List<Person> persons) {
        return persons.stream()
                .filter(person -> person.getSalary() > 50_000)
                .limit(10)
                .collect(Collectors.toList());
    }

    //  Найти депаратмент, чья суммарная зарплата всех сотрудников максимальна
    static public Optional<Department> findTopDepartment(List<Person> persons) {
                Map<Department, Double> map = persons.stream()
                        .collect(Collectors.groupingBy(Person::getDepartment,
                                Collectors.summingDouble(Person::getSalary)));
                return Optional.of(map.entrySet().stream()
                        .max(Comparator.comparing(Map.Entry::getValue))
                        .map(Map.Entry::getKey)
                        .get());

    }

    static Map<Department, Double> showDepsAndMaxSalary(List<Person> persons){
        Map<Department, Double> map = persons.stream()
                .collect(Collectors.groupingBy(Person::getDepartment,
                        Collectors.summingDouble(Person::getSalary)));
        return map;
    }

}
