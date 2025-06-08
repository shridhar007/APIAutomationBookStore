# APIAutomationBookStore

## Overview
This API testing framework uses restAssured api with Java programming and testNG testing framework with maven as build tool.  
Framework includes feature like,
Allure report integration, Configurable test environments parameters, json schema validations, github actions workflow file etc.

## Features
- **CI CD Integration**: Provision for integration with CI/CD pipeline using Github Actions workflow file with GH Pages to host allure reports.
- **Allure Reports**: Sharable single file Allure reports are generated at the end of execution.
- **Configurable Environments**: Tests can be executed against new environment by adding its details in 'config_data.json' and passing environment tag as an argument to pytest command
- **Custom Arguments**: As of now framework accepts 2 custom command line arguments with testNG command. [-DsuiteXmlFile=negativeTests.xml, -Denv=test]
  - Sample command: mvn clean test -DsuiteXmlFile=negativeTests.xml -Denv=test

## Technologies
- **OpenJDK 21**: Java as programming language. 
- **TestNG**: Testing framework for test execution management.
- **RestAssured**: Library to execute REST API calls. 

### Prerequisites
- OpenJDK 21
- Maven build tool [3.9.9]
- Allure 2.24.0

### Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/shridhar007/APIAutomationBookStore.git
    ```

2. Navigate to project directory
    ```bash
    cd APIAutomationBookStore
    ```
3. Open the project in Intellij.    
4. Open pom.xml and sync it to download external dependencies.
5. Setup allure home in system environments to ensure allure.bat file is accessible for report generation 

## Running the Application
- **In code repo, search for commands.txt file**
- **Open it in editor, copy paste commands based on required test suite in terminal**
- **Post execution check for 'allure-report' folder for index.html file for execution report**

## Note
- For GitHub Actions workflow, 'execution_workflow.yml' file is added in codebase. 
- If user wants to host the execution results on Github pages, then a seperate orphan branch by name 'gh-pages' needs to be created. 
- Also, user needs to generate a secrets.CLASSIC_PAT token for workflow file to push generated report on gh-pages branch

## Testing Strategy
- After successfully running fast api locally, I worked on creating a post man collection to test APIs manually.
- Ensured APIs are up by hitting health check api.
- Request payload for signup api was not provided, used online took to check schema of *.db file to get column entries.
- Prepared a sample request payload by adding column name as key, registration/signup api worked.
- Post that generated bearer token by using creds from step above and login api.
- Same bearer token is used for remaining book management api.
- All above steps are done using POSTMAN.
- Once postman collection got working, I started developing automation framework.
- During automated test development, postman collection helped a lot in putting correct asserts [Positive + Negative]
- Postman collection is included in utils->postman_collection folder in repository for reference.  

## Challenges Faced: 
- Challenges in beautification of Allure report. [Adding required tags and removing not required ones]
- Java-TestNG framework is not easy to work with for someone coming from python-pytest background. Code grows exponentially compared to python. 
- Always keep track of right kind of object and finding associated methods for casting purpose was tedious. 