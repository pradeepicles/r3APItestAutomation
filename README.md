Prerequisites 
Java Development Kit (JDK) 
installed Maven build tool 
installed Setup Clone the repository to your local machine. 
Open the project in your preferred IDE.
Make sure the necessary dependencies mentioned in the pom.xml file are downloaded.

Usage 
To run the tests, navigate to the project's root directory and run the following command in the terminal:
mvn clean test The tests will be executed, and the results will be displayed in the console.

Project Structure

├── src 
│ └── test
│   ├── java │ 
│     └── APITestRunner 
│     └── APITestSteps
│ ├── resources 
│ │ ├── featureFile.feature 
│ └── pom.xml
└── README.md
