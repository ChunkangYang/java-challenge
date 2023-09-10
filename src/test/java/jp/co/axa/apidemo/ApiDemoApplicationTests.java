package jp.co.axa.apidemo;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jp.co.axa.apidemo.controllers.EmployeeController;
import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApiDemoApplicationTests {

	private final String apiUrl = "/api/v1";
	private static List<Employee> employeesList;
	private static Boolean isInit = false;
	@Autowired
	private EmployeeController controller;
	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private MockMvc mockMvc;

	// Initial Data for Test
	private void initTest() {
		System.out.println("initTest Called");
		List<Object[]> objList = new ArrayList<>();
		employeesList = new ArrayList<>();

		objList.add(new Object[] { 1L, "Jack Peralta", "Detective", 500000 });
		objList.add(new Object[] { 2L, "Amy Santiago", "Sergeant", 600000 });
		objList.add(new Object[] { 3L, "Terry Jeffords", "Sergeant", 700000 });
		objList.add(new Object[] { 4L, "Charles Boyle", "Detective", 550000 });
		objList.add(new Object[] { 5L, "Rosa Diaz", "Detective", 650000 });

		for (Object[] obj : objList) {
			Employee employee = new Employee();
			employee.setId((Long) obj[0]);
			employee.setName((String) obj[1]);
			employee.setDepartment((String) obj[2]);
			employee.setSalary((Integer) obj[3]);
			repository.save(employee);
			// Also, store employee data in a private member variable
			employeesList.add(employee);
		}
		try {
			System.out.println("Check initTest() print all epmloyees");
			showAllEmployees();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Show All Employees
	private void showAllEmployees() throws Exception {
		System.out.println("Show All Employees");
		ObjectMapper objectMapper = new ObjectMapper();

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(apiUrl + "/employees")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		String responseContent = result.getResponse().getContentAsString();

		List<Employee> employees = objectMapper.readValue(responseContent, new TypeReference<List<Employee>>() {
		});
		for (Employee e : employees) {
			System.out.println(e.getId());
			System.out.println(e.getName());
			System.out.println(e.getDepartment());
			System.out.println(e.getSalary());
		}
	}

	private void removeTestEmployee(Long employeeId) {
		Iterator<Employee> iterator = employeesList.iterator();
		while (iterator.hasNext()) {
			Employee employee = iterator.next();
			if (employee.getId() == employeeId) {
				iterator.remove();
			}
		}
	}

	private Employee getRandomEmployee() {
		// Get random employee form employeesList
		Random random = new Random();
		int randomIndex = random.nextInt(employeesList.size());
		return employeesList.get(randomIndex);
	}

	@Before
	public void setUp() {
		// Initialization is only needed once
		if (!isInit) {
			initTest();
			isInit = true;
		}
	}

	@Test
	public void contextLoads() {
		assertThat(controller).isNotNull();
	}

	// Controller APIs Test
	@Test
	@WithMockUser
	public void testSaveEmployeeAPI() throws Exception {
		Object obj[] = { 6L, "Raymond Holt", "Captain", 1000000 };
		Employee employee = new Employee();
		employee.setId((Long) obj[0]);
		employee.setName((String) obj[1]);
		employee.setDepartment((String) obj[2]);
		employee.setSalary((Integer) obj[3]);
		employeesList.add(employee);

		ObjectMapper objectMapper = new ObjectMapper();
		String employeeJson = objectMapper.writeValueAsString(employee);

		mockMvc.perform(MockMvcRequestBuilders.post(apiUrl + "/employee").with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(employeeJson))
				.andExpect(status().isOk());

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(apiUrl + "/employee/{employeeId}", obj[0])
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String responseJson = result.getResponse().getContentAsString();
		employee = objectMapper.readValue(responseJson, Employee.class);

		assertThat(employee.getId()).isEqualTo(obj[0]);
		assertThat(employee.getName()).isEqualTo(obj[1]);
		assertThat(employee.getDepartment()).isEqualTo(obj[2]);
		assertThat(employee.getSalary()).isEqualTo(obj[3]);
	}

	@Test
	@WithMockUser
	public void testGetEmployeeAPI() throws Exception {
		Employee employee = new Employee();
		ObjectMapper objectMapper = new ObjectMapper();

		Employee testEmployee = getRandomEmployee();

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get(apiUrl + "/employee/{employeeId}", testEmployee.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String responseJson = result.getResponse().getContentAsString();
		employee = objectMapper.readValue(responseJson, Employee.class);

		assertThat(employee.getId()).isEqualTo(testEmployee.getId());
		assertThat(employee.getName()).isEqualTo(testEmployee.getName());
		assertThat(employee.getDepartment()).isEqualTo(testEmployee.getDepartment());
		assertThat(employee.getSalary()).isEqualTo(testEmployee.getSalary());
	}

	@Test
	@WithMockUser
	public void testDeleteEmployeeAPI() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();

		Employee testEmployee = getRandomEmployee();

		mockMvc.perform(
				MockMvcRequestBuilders.delete(apiUrl + "/employee/{employeeId}", testEmployee.getId()).with(csrf())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		mockMvc.perform(MockMvcRequestBuilders.get(apiUrl + "/employee/{employeeId}", testEmployee.getId())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(apiUrl + "/employees")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String responseContent = result.getResponse().getContentAsString();
		List<Employee> employees = objectMapper.readValue(responseContent, new TypeReference<List<Employee>>() {
		});

		// Check if the size of the data in the database is one less than the size of the test data
		assertThat(employees.size()).isEqualTo(employeesList.size() - 1);

		// Keep the database and local variable the same
		removeTestEmployee(testEmployee.getId());
	}

	@Test
	@WithMockUser
	public void testUpdaetEmployeeAPI() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		Object changeValues[] = { "New Guy", "Cleaning", 300000 };

		Employee testEmployee = getRandomEmployee();

		testEmployee.setName((String) changeValues[0]);
		testEmployee.setDepartment((String) changeValues[1]);
		testEmployee.setSalary((Integer) changeValues[2]);

		String employeeJson = objectMapper.writeValueAsString(testEmployee);

		mockMvc.perform(MockMvcRequestBuilders.put(apiUrl + "/employee/{employeeId}", testEmployee.getId()).with(csrf())
				.contentType(MediaType.APPLICATION_JSON)
				.content(employeeJson))
				.andExpect(status().isOk())
				.andReturn();

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get(apiUrl + "/employee/{employeeId}", testEmployee.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String responseJson = result.getResponse().getContentAsString();
		Employee employee = objectMapper.readValue(responseJson, Employee.class);

		assertThat(employee.getId()).isEqualTo(testEmployee.getId());
		assertThat(employee.getName()).isEqualTo(testEmployee.getName());
		assertThat(employee.getDepartment()).isEqualTo(testEmployee.getDepartment());
		assertThat(employee.getSalary()).isEqualTo(testEmployee.getSalary());
	}

	@Test
	@WithMockUser
	public void testGetEmployeesAPI() throws Exception {
		System.out.println("testGetEmployeesAPI Called");
		ObjectMapper objectMapper = new ObjectMapper();

		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(apiUrl + "/employees")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();

		String responseContent = result.getResponse().getContentAsString();

		List<Employee> employees = objectMapper.readValue(responseContent, new TypeReference<List<Employee>>() {
		});

		// Check if the size of the data in the database matches the size of the test data
		assertThat(employees.size()).isEqualTo(employeesList.size());
	}

	// Security test cases
	@Test
	public void testPublicResource() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(apiUrl + "/public"))
				.andExpect(status().isOk());
	}

	@Test
	public void testSecureResourceWithoutAuthentication() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(apiUrl + "/private"))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	public void testSecureResourceWithAuthentication() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get(apiUrl + "/private"))
				.andExpect(status().isOk());
	}
}
