package jp.co.axa.apidemo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;

@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();
        return employees;
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<?> getEmployee(@PathVariable(name="employeeId")Long employeeId) {
		Employee emp = employeeService.getEmployee(employeeId);
		if(emp == null) {
			// Dispatch an error message when there is no employee with the queried employee ID in the database
			String errorMessageString = "getEmployee: Employee not found with ID: " + employeeId;
			return new ResponseEntity<>(errorMessageString, HttpStatus.NOT_FOUND); // 404
		}
		return new ResponseEntity<>(emp, HttpStatus.OK);
    }

    @PostMapping("/employee")
    public void saveEmployee(@RequestBody Employee employee){
        employeeService.saveEmployee(employee);
        String empInfoString = "ID:" + employee.getId() + " Name:" + employee.getName();
        System.out.println("Employee: " + empInfoString + " Saved Successfully");
    }

    @DeleteMapping("/employee/{employeeId}")
    public void deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
        employeeService.deleteEmployee(employeeId);
        System.out.println("Employee:" + employeeId + " Deleted Successfully");
    }

    @PutMapping("/employee/{employeeId}")
    public ResponseEntity<?> updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId){
        Employee emp = employeeService.getEmployee(employeeId);
        if(emp == null) {
			// Dispatch an error message when there is no employee with the queried employee ID in the database
			String errorMessageString = "updateEmployee: Employee not found with ID: " + employeeId;
			return new ResponseEntity<>(errorMessageString, HttpStatus.NOT_FOUND); // 404
        }
        employeeService.updateEmployee(employee);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Security test
    @GetMapping("/public")
    public String publicResource() {
        return "This is a public resource.";
    }
    // Security test
    @GetMapping("/private")
    public String privateResource() {
        return "This is a private resource.";
    }

}
