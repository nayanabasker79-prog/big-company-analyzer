package com.analyzer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class EmployeeAnalyzer {
    public Map<Integer, Employee> loadEmployees(String filePath) throws Exception {
        Map<Integer, Employee> employees = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {

            try{

                    String[] parts = line.split(",");
                    int id = Integer.parseInt(parts[0].trim());
                    String firstName = parts[1].trim();
                    String lastName = parts[2].trim();
                    double salary = Double.parseDouble(parts[3].trim());
                    Integer managerId = null;
                    if (parts.length > 4 && !parts[4].trim().isEmpty()) {
                        managerId = Integer.parseInt(parts[4].trim());
                    }
                    employees.put(id, new Employee(id, firstName, lastName, salary, managerId));
                } catch (NumberFormatException e) {
                    System.err.printf("Skipping row with invalid number format: %s (%s)%n", line, e.getMessage());
                } catch (Exception e) {
                    System.err.printf("Skipping row due to unexpected error: %s (%s)%n", line, e.getMessage());
                }

            }
        } catch (FileNotFoundException e) {
            System.err.println("Error: File not found at " + filePath);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        // To build the hierarchy
        for (Employee e : employees.values()) {
            if (e.getManagerId() != null) {
                Employee manager = employees.get(e.getManagerId());
                manager.addSubordinate(e);
            }
        }

        return employees;
    }

    public void analyze(Map<Integer, Employee> employees) {
        boolean depthViolationFlag = false;
        System.out.println("Salary violations:");
        for (Employee e : employees.values()) {
            if (!e.getSubordinates().isEmpty()) {
                double avg = e.getSubordinates().stream().mapToDouble(Employee::getSalary).average().orElse(0);
                double min = avg * 1.2;
                double max = avg * 1.5;
                if (e.getSalary() < min) {
                    System.out.printf("Manager %s %s earns %.2f LESS than minimum allowed%n", e.getFirstName(),e.getLastName(), min - e.getSalary());
                } else if (e.getSalary() > max) {
                    System.out.printf("Manager %s %s earns %.2f MORE than maximum allowed%n", e.getFirstName(),e.getLastName(), e.getSalary() - max);
                }
            }
        }

        System.out.println("\nReporting line violations:");
        for (Employee e : employees.values()) {
            int depth = getDepth(e, employees);
            if (depth > 4) {
                System.out.printf("Employee %s %s has reporting line too long by %d%n", e.getFirstName(),e.getLastName(), depth - 4);
                depthViolationFlag = true;
            }
        }
        if(!depthViolationFlag) {
            System.out.printf("There is no Violation");
        }
    }

    protected int getDepth(Employee e, Map<Integer, Employee> employees) {
        int depth = 0;
        Integer mgrId = e.getManagerId();
        while (mgrId != null) {
            depth++;
            mgrId = employees.get(mgrId).getManagerId();
        }
        return depth;
    }
}
