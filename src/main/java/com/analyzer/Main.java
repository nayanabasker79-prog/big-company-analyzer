package com.analyzer;

import com.analyzer.EmployeeAnalyzer;

import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {

        if (args.length != 1) {
            System.out.println("Usage: java -jar big-company-analyzer.jar <employees.csv>");
            return;
        }

        EmployeeAnalyzer analyzer = new EmployeeAnalyzer();
        //load employees from csv to map
        Map<Integer, Employee> employees = analyzer.loadEmployees(args[0]);
        // analyse the map
        analyzer.analyze(employees);
    }
}
