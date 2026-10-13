package org.example.hw.functional;


import org.example.hw.functional.model.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Transformers {
    private Employee findEmployeeBornAtThisYear(Collection<Employee> employees, int year){
        return employees.stream().filter(emp -> emp.getBirthDate().getYear() == year).findFirst().orElse(null);
    }

    private Collection<String> findAllJobTitles(Collection<Job> jobs,Collection<Job> jobs2){
        return Stream.of(jobs, jobs2).flatMap(Collection::stream)
                .map(Job::getJobTitle)
                .collect(Collectors.toList()); //Объединяем два стрима с flatmap, достаем заголовок и кладем его в list, оставляя дублирующие заголовки
    }

    private Collection<String> findAllNameWithoutDuplicatesFromBothList(Collection<Employee> employees,Collection<Employee> employees2){
        return Stream.of(employees, employees2).flatMap(Collection::stream).map(Employee::getName).collect(Collectors.toSet());
    }

    private Integer findAverageAgeOfEmployeesThatWorkInDepartment(Collection<Employee> employees, Department department){
        LocalDate currentDate = LocalDate.now();
        Map<Integer, Double> averageAgeByDepartmentId = employees.stream().collect(Collectors.groupingBy(
                Employee::getDepartmentId, Collectors.averagingInt(
                        emp -> Period.between(emp.getBirthDate(), currentDate).getYears())));
        Double averageAge = averageAgeByDepartmentId.get(department.getDepartmentId());
        if (averageAge == null) {
            throw new IllegalArgumentException(
                    "В переданной коллекции нет сотрудников департамента "
                            + department.getDepartmentId()
            );
        }
        return averageAge.intValue();
    }

    private Employee findEmployeeWithNameOrUseDefaultOne(Collection<Employee> employees, String name, Employee defaultUser){
        return employees.stream().filter(emp -> emp.getName().equals(name)).findFirst().orElse(defaultUser);
    }

    private Job findJobWithMinSalaryMoreThanOrThrowException(Collection<Job> jobs, Integer minSalary){
        return jobs.stream().filter(job -> job.getMinSalary() > minSalary)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("в коллекции нет работы, удовлетворяющей условиям"));
    }

    //try to find 2 options.
    //option 1
    private Collection<String> findAllLocationNamesButWithoutDuplicates(Collection<Location> locations){
        return  locations.stream().map(Location::getLocation).collect(Collectors.toSet()); // Set убирает дубли
    }
    //option 2
    private Collection<String> findAllLocationNamesButWithoutDuplicates2(Collection<Location> locations){
        return locations.stream().map(Location::getLocation).distinct().toList();
    }
}
