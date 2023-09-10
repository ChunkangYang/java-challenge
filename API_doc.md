# Employee API Documentation

## Introduction

This API documentation provides details about the Employee API endpoints and their usage.

## Base URL

All API endpoints are accessed under the base URL: `/api/v1`

## Endpoints

### 1. Get All Employees

- **Endpoint:** `/employees`
- **HTTP Method:** GET
- **Description:** Retrieves a list of all employees.
- **Response:** Returns a JSON array containing the list of employees.

### 2. Get Employee by ID

- **Endpoint:** `/employee/{employeeId}`
- **HTTP Method:** GET
- **Description:** Retrieves an employee by their ID.
- **Request Parameters:**
  - `employeeId` (PathVariable) - The unique ID of the employee to retrieve.
- **Response:** Returns a JSON object representing the employee if found, or a 404 error if the employee does not exist.

### 3. Create Employee

- **Endpoint:** `/employee`
- **HTTP Method:** POST
- **Description:** Creates a new employee.
- **Request Body:** JSON object representing the employee to be created.
- **Response:** No response body. Returns a status code indicating success (200 OK).

### 4. Delete Employee by ID

- **Endpoint:** `/employee/{employeeId}`
- **HTTP Method:** DELETE
- **Description:** Deletes an employee by their ID.
- **Request Parameters:**
  - `employeeId` (PathVariable) - The unique ID of the employee to delete.
- **Response:** No response body. Returns a status code indicating success (200 OK).

### 5. Update Employee by ID

- **Endpoint:** `/employee/{employeeId}`
- **HTTP Method:** PUT
- **Description:** Updates an employee's information by their ID.
- **Request Parameters:**
  - `employeeId` (PathVariable) - The unique ID of the employee to update.
- **Request Body:** JSON object representing the updated employee information.
- **Response:** Returns a status code indicating success (200 OK) if the employee is found and updated. Returns a 404 error if the employee does not exist.

## Security Endpoints (For Testing)

These endpoints are used for testing security configurations.

### 1. Public Resource

- **Endpoint:** `/public`
- **HTTP Method:** GET
- **Description:** Returns a message indicating that this is a public resource.

### 2. Private Resource

- **Endpoint:** `/private`
- **HTTP Method:** GET
- **Description:** Returns a message indicating that this is a private resource.

