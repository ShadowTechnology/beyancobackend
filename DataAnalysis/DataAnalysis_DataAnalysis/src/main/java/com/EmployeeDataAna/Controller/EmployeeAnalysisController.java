package com.EmployeeDataAna.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.EmployeeDataAna.Services.Employee;
import com.EmployeeDataAna.Services.ExcelFileService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/employee")
public class EmployeeAnalysisController {
    @Autowired
    private ExcelFileService employeeService;

    @GetMapping("/analyze")
    public ResponseEntity<List<Employee>> analyzeEmployees() {
        try {
            List<Employee> employees = employeeService.readExcelData();

            // Implement your analysis logic based on the given criteria
            // Print the names and positions of employees who meet the criteria
         // Create lists to store employees who meet each criterion
            List<Employee> consecutiveDaysEmployees = new ArrayList<>();
            List<Employee> lessThan10HoursEmployees = new ArrayList<>();
            List<Employee> moreThan14HoursEmployees = new ArrayList<>();

            // Define your analysis logic
            for (int i = 0; i < employees.size(); i++) {
                Employee currentEmployee = employees.get(i);
                
                // Check for 7 consecutive days
                if (checkForConsecutiveDays(employees, i, 7)) {
                    consecutiveDaysEmployees.add(currentEmployee);
                }
                
                // Check for less than 10 hours between shifts
                if (i < employees.size() - 1) {
                    Employee nextEmployee = employees.get(i + 1);
                    if (checkTimeBetweenShifts(currentEmployee, nextEmployee, 1, 10)) {
                        lessThan10HoursEmployees.add(currentEmployee);
                    }
                }
                
                // Check for more than 14 hours in a single shift
                if (parseTimeToMinutes(currentEmployee.getTimecardHours()) > 840) {
                    moreThan14HoursEmployees.add(currentEmployee);
                }
            }

         // Print or return these lists as needed
            System.out.println("Employees with 7 consecutive days: ");
            for (Employee employee : consecutiveDaysEmployees) {
                System.out.println("Employee Name: " + employee.getEmployeeName());
                System.out.println("Position: " + employee.getPositionId());
                System.out.println("------------");
            }

            System.out.println("Employees with less than 10 hours between shifts: ");
            for (Employee employee : lessThan10HoursEmployees) {
                System.out.println("Employee Name: " + employee.getEmployeeName());
                System.out.println("Position: " + employee.getPositionId());
                System.out.println("------------");
            }

            System.out.println("Employees with more than 14 hours in a single shift: ");
            for (Employee employee : moreThan14HoursEmployees) {
                System.out.println("Employee Name: " + employee.getEmployeeName());
                System.out.println("Position: " + employee.getPositionId());
                System.out.println("------------");
            }

            return ResponseEntity.ok(employees);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // Helper method to check for consecutive days
    private boolean checkForConsecutiveDays(List<Employee> employees, int currentIndex, int consecutiveDays) {
        // Implement logic to check for consecutive days
    	if (currentIndex < consecutiveDays - 1) {
            // Not enough data to check for consecutive days
            return false;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        try {
            for (int i = 0; i < consecutiveDays; i++) {
                Employee currentEmployee = employees.get(currentIndex - i);
                String payCycleStartDate = currentEmployee.getPayCycleStartDate();

                if (payCycleStartDate == null || payCycleStartDate.isEmpty()) {
                    // Handle empty date values
                    return false;
                }

                Date currentDate = dateFormat.parse(payCycleStartDate);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(currentDate);

                for (int j = 1; j < consecutiveDays; j++) {
                    // Move to the next day
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                    Date nextDate = calendar.getTime();

                    String nextDateStr = dateFormat.format(nextDate);
                    String nextEmployeeStartDate = employees.get(currentIndex - j).getPayCycleStartDate();

                    if (!nextDateStr.equals(nextEmployeeStartDate)) {
                        // Consecutive days are not met
                        return false;
                    }
                }
            }

            // Consecutive days are met
            return true;
        } catch (ParseException e) {
            // Handle date parsing exception
            e.printStackTrace();
            return false;
        }
        
        
    }

    // Helper method to check time between shifts
    private boolean checkTimeBetweenShifts(Employee first, Employee second, int minHours, int maxHours) {
        // Implement logic to check the time between shifts
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

        try {
            String firstTimeOutStr = first.getTimeOut();
            String secondTimeInStr = second.getTimeIn();

            if (firstTimeOutStr.isEmpty() || secondTimeInStr.isEmpty()) {
                // Handle empty time values
                return false;
            }

            Date firstTimeOut = dateFormat.parse(firstTimeOutStr);
            Date secondTimeIn = dateFormat.parse(secondTimeInStr);

            long timeDifferenceMillis = secondTimeIn.getTime() - firstTimeOut.getTime();

            // Calculate time difference in hours
            long timeDifferenceHours = timeDifferenceMillis / (60 * 60 * 1000);

            return timeDifferenceHours >= minHours && timeDifferenceHours <= maxHours;
        } catch (ParseException e) {
            // Handle time parsing exception
            e.printStackTrace();
            return false;
        }
    }

    // Helper method to convert time in HH:mm format to minutes
    private int parseTimeToMinutes(String time) {
        // Implement logic to parse time to minutes
    	 if (time == null || time.isEmpty()) {
    	        return 0; // Return 0 if the time is missing or empty
    	    }

    	    String[] parts = time.split(":");
    	    if (parts.length != 2) {
    	        // Handle invalid time format
    	        return 0;
    	    }

    	    try {
    	        int hours = Integer.parseInt(parts[0]);
    	        int minutes = Integer.parseInt(parts[1]);
    	        return hours * 60 + minutes; // Convert to total minutes
    	    } catch (NumberFormatException e) {
    	        // Handle parsing exception
    	        e.printStackTrace();
    	        return 0;
    	    }
    }
}


