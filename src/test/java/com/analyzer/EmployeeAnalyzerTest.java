package com.analyzer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;


public class EmployeeAnalyzerTest {

    EmployeeAnalyzer employeeAnalyzer = new EmployeeAnalyzer();

    @Test
    public void testLoadEmployees() throws Exception {
        EmployeeAnalyzer analyzer = new EmployeeAnalyzer();
        Map<Integer, Employee> employees = analyzer.loadEmployees("src/test/resources/employees_test.csv");
        Assertions.assertEquals(5, employees.size());

    }

    @Test
    public void testDepthCalculation() throws Exception {
        EmployeeAnalyzer analyzer = new EmployeeAnalyzer();
        Map<Integer, Employee> employees = analyzer.loadEmployees("src/test/resources/employees_test.csv");
        Employee dave = employees.get(300);
        int depth = analyzer.getDepth(dave, employees);
        Assertions.assertTrue(depth > 0);
    }

    @Test
    void testAnalyzeSalaryViolations() {
        Employee manager = new Employee(123, "Alice", "Jane", 100.0,null);
        Employee e1 = new Employee(2, "Bob", "Jose", 50.0, 123);
        Employee e2 = new Employee(3, "John", "Jack", 50.0, 2);

        manager.addSubordinate(e1);
        manager.addSubordinate(e2);

        Map<Integer, Employee> employees = new HashMap<>();
        employees.put(123, manager);
        employees.put(2, e1);
        employees.put(3, e2);
        for (Employee e : employees.values()) {
            if (e.getManagerId() != null) {
                Employee mngr = employees.get(e.getManagerId());
                mngr.addSubordinate(e);
            }
        }

        employeeAnalyzer.analyze(employees);

    }
}
