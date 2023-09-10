## Bug Fix
- Fixed an issue in EmployeeController.java: `saveEmployee` method where the `@RequestBody` annotation was missing, causing the method to not parse JSON-formatted data correctly.

## Change Syntax
- Changed the API for operating on a single employee from `employees` to `employee`.

## Improvement
- Added logic to return an HTTP status of `NOT_FOUND (404)` with an appended error message when the API cannot retrieve data.
- Enhanced logging to include employee information, such as ID and Name, during operations.
- Implemented Spring Boot Security to secure Controller end points.
- Introduced Database Cache.

## Features Added
- Added two API tests for testing security in the Controller.
- Created API test cases and Security test cases in the `ApiDemoApplicationTests` class.

## If Time Permits (TODO)
- Plan to add additional APIs, such as querying employees within a specified salary range and listing employees by department, etc.

## My Java Experience
- I have approximately three years of development experience, primarily focused on desktop software and Android apps. My experience with backend development is limited.
