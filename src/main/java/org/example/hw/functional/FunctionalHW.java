package org.example.hw.functional;


import org.example.hw.functional.model.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

public class FunctionalHW {

    public Optional<Employee> findEmployeeById(List<Employee> employees, int id) {
        return Optional.of(employees.get(id));
    }

    public List<Employee> sortEmployeesByName(List<Employee> employees) {
        return employees.stream().sorted(Comparator.comparing(Employee::getName)).toList();
    }

    public long findNumberJobsWithMaxSalaryMoreThan(List<Job> jobs, double maxSalary) {
        return jobs.stream().filter(job ->job.maxSalary > maxSalary).count();
    }

    public Set<String> getEmployeeNamesFromDepartment(List<Employee> employees, Department department) {
        return employees.stream().collect(
                Collectors.groupingBy(Employee::getDepartmentId, Collectors.mapping(
                        Employee::getName, Collectors.toSet()))).get(department.getDepartmentId());
    }

    //employeeId, Employee
    public Map<Integer, Employee> buildEmployeeMap(List<Employee> employees) {
        return employees.stream().collect(Collectors.toMap(
                Employee::getId,
                employee -> employee,
                (existing, replacement) -> replacement)); //Решил добавить третий аргумент для случая если id дублируется
    }

    //find employee with the closest birthdate
    public Employee findByClosestBirthDate(List<Employee> employees, LocalDate birthDate) {
        return employees.stream().min(Comparator.comparingLong( //ищем мнимальное расстояние (модуль) между датой рождения сотрудника и заданной в аргументе birthDate
                emp -> Math.abs((ChronoUnit.DAYS.between(emp.getBirthDate(), birthDate)))))
                .orElse(null);
    }

    //make map where key is department name and long is total amount of employees working in that department
    public Map<String, Long> totalSalaryReport(List<Employee> employees, List<Department> departments) {
        Map<Integer, Long> departmentsMap = employees.stream().collect(Collectors.groupingBy(Employee::getDepartmentId, Collectors.counting())); //Группируем сотрудников по id департаменту и сразу считаем кол-во в каждоый группе
        return departments.stream().collect(Collectors.toMap(Department::getDepartmentName, dep -> departmentsMap.getOrDefault(dep.getDepartmentId(), 0L))); //маппим по названию департамента, значение получаем из предыдущей мапы
    }

    public Optional<Employee> findRichestEmployee(List<Employee> employees) {
        return employees.stream().max(Comparator.comparing(Employee::getSalary));
    }

}
