![](https://drive.google.com/uc?id=19vd8Ta1zTrwO_FlDGp8uPYDGeWl_S_9Y)

# 1. Requirements
## 1.1. Functional
- System administrators should be able to register to the system.
- It should be possible to update the vehicles, change the price, etc.
- System administrators should be able to see all leases.
- Vehicles should be able to be sent for maintenance.
- New tools can be added to the system.
- The current condition of the vehicles should be checked before making a rental transaction.
- Users should be able to rent the vehicle they want.
- Users should be able to filter and search vehicles according to the features they want.
- Users should be able to view current and previous rental transactions.
- Users should be able to view their current and previous invoices.
- Users should be able to pay by any method they want.

## 1.2. Non-functional
- Services should have low latency
- It should be highly available and consistent, a user should be able to rent a vehicle immediately and receive transaction confirmation immediately.
- Should be scalable, multiple car rentals can be performed in major cities
- Multiple users may want to rent a vehicle at the same time, these competitive situations should be addressed.

# 2. Pre-information

- Microservices were designed and developed implementing the Decompose by Business Capability practice due to non-functional requirements.
- Great emphasis was placed on writing clean codes and project hierarchy.
- SOLID and GRASP patterns were given importance.
- Emphasis was placed on using existing structures of the Java language (such as validator and enum) effectively.

# 3. Technology Stack

- Config Server was made with Spring Cloud Config. This is for accessing yaml files of microservices over a port. It can pull different yaml files for Production and Development. These yaml files can be retrieved from Config Server github.
- API Gateway with Spring Cloud Gateway and Service Discovery structures with Eureka Service Registry were provided. These were used to gather all microservices under one roof over a single port. Since random ports of microservices will be created and may change, their ports are registered.
- Kafka was used for asynchronous communications. It has been used in scenarios such as for leisurely data insertions. For example, as a result of the lease transaction, data is sent to the invoice service by sending an event with Kafka, the invoice service receives the data and adds a record.
- Docker used for data of databases and Kafka.
- MySQL, PostgreSQL and Mongodb are used in databases.
- Lombok used to use constructors and getters/setters without writing them (lombok implements the methos on compile time).
- Openfeign is used for rest calls. Resillience4j was used to implement the Retry pattern.
- SLF4J was used to keep logs.
- Spring Data was used for the use of JPA (Object Relational Mapping).
- Added Keycloak for Authorization and Authentication.
- Zipkin has been added to visually display calls and interactions of services.

# 4. Explanation of Project Modules

- The **api-gateway module** uses Spring Cloud Gateway. The purpose of the module is to provide API Gateway functionality only. The yaml file has port information (9010) and information for Eureka to register.
- The **common-package module** contains libraries and classes that microservices will use in common. Spring's validation includes devtols, config or libraries like Lombok, modelmapper, eureka, kafka, openfeign, resilience4j. It contains structures such as custom exception, exceptionhandler, kafka's settings, events to be used in kafka, modelmapper's settings, self-annotations, and common DTOs. It includes security mechanism, it also uses Keycloak for this.
- The **config-server module** only uses Spring Cloud Config technology and provides that functionality. In the yaml file, it is determined from where to pull the yaml files.
- The **discovery-servery module** uses only Eureka Service Registry technology and provides that functionality. The port (8761) to be used in the Yaml file is determined.
- The **filter-service module** is specific to the filter microservice. It uses NoSQL (Mongodb) database. It is made so that ordinary users can quickly see the available cars. Only two methods are exported (getAll and getById). Yaml file has database settings, settings for Kafka and settings for saving to Eureka. There is setting available to specify random port.
- The **inventory-service module** is a microservice to be used by admins. It will be used to add brands, models and cars, new cars added here will be reflected in the filter microservice. It uses SQL (PostgreSQL) database. Yaml file has database settings, settings for Kafka and settings for saving to Eureka. There is setting available to specify random port. It offers export CRUD methods. In addition, it offers methods to provide detailed information of the car for billing processes to check whether the car is suitable for outsourcing.
- The **invoice-service module** is used to store the invoices of the rental transactions realized in the rent microservice. It uses NoSQL (Mongodb). Only the getAll method is exported. Yaml file has database settings, settings for Kafka and settings for saving to Eureka. There is setting available to specify random port.
- The **maintenance-service module** is a microservice to be used by admins. It will be used to send the cars for maintenance, the car records added here will be reflected in the records in the inventory and filter microservices. It uses SQL (MySQL) database. Yaml file has database settings, settings for Kafka and settings for saving to Eureka. There is setting available to specify random port. It offers export CRUD methods. The availability of the car that is requested to be sent for maintenance is checked by sending a REST request to the inventory microservice.
- The **payment-service module** is used to keep the records of the payments of the rental transactions realized in the rent microservice. It uses SQL (PostgreSQL). Exported CRUD operations are offered. In addition, it has a payment method for the rent microservice to process the payments. It is used to verify the adequacy of the balances of stored fake bank accounts and to provide balance reductions. There are database settings in the yaml file and settings for saving to Eureka. There is setting available to specify random port.
- The **rental-service module** is a microservice to be used by normal users. It will be used for the rental of cars, the car records added here will be reflected in the records in the inventory and filter microservices. It uses SQL (PostgreSQL) database. Export methods are provided for CRUD operations. The availability of the car requested to be rented is checked by sending a REST request to the inventory microservice. If the car is available, the payment is processed with the account information given to the payment service, and finally, the invoice is sent to add the invoice to the invoice service with the vehicle and account information. Here, it is experientially called the Retry structure. This build is called from the Resillience4j library. Even if the inventory service is closed, it is checked whether the service is really closed by waiting N times for M seconds and sending a request. If it is closed, a special exception is thrown. Yaml file has database settings, settings for Kafka and settings for saving to Eureka. There is setting available to specify random port. There are settings for the use of the Retry pattern.

# 5. Layered Architecture Of Microservices

Entities and repository packages belong to the data layer. First Code approach was applied in Entities. The attributes and relations that the direct assets will have are written. Added necessary annotations so that they can work with JPA. Implements the Repository JPA class. Written needed methods that are not available in JPA (for example, updating car status by car id for cars in inventory service).

Business package fulfills business layer functionality. The Abstracts package contains the classes that the controllers will use. These classes are of type Interface. The reason is that in case it is desired to integrate different technologies in the future, it is possible to write the necessary codes by applying these Interfaces without breaking the existing classes. It also makes it possible to apply the Liskov, Inteface Segregation and Dependency Inversion principles in the SOLID principles. The classes in the Concretes package will implement the Interfaces in the abstracts package. Thus, it is ensured that all defined methods must be applied. The classes will pull data from the repository classes (by providing dependency injection) and additionally provide business rules, dispatch the required event and make REST calls, and return Data Transfer Objects as a result. Modelmapper instances are used for transformations of DTOs (comes from common-package). The Rules package contains business rules. For example, if a non-existent entity is searched when the getById request is thrown, the business rules check for the existence of the entity to avoid a database error, otherwise a custom-written exception is thrown. Or, for example, when the car is to be rented, the business rules in the rental service apply the payment for payment rule, a REST request is thrown in it, and a special exception is thrown if the payment is negative. In short, this package is used to throw self error classes for possible errors and business logic glitches. It is costly, provides extra database reads, but greatly improves code readability.

The API package counts as the UI layer. Here are the controllers. These include the methods we export. In order to ensure the functionality of the methods, we inject the classes in the business layer by injecting dependency. Injected classes are added so that they can work with singleton logic so that they are not recreated with Spring's annotations. The Clients package contains the classes required to provide REST calls. These are two classes. The class to use to provide the invocation (includes the openfeing and retry notations). There is also an error class for a possible error. The error classes contain what should be done if possible REST calls fail. There logs are kept (with SLF4J just by printing to the console - it's experiential) and special exceptions are thrown. Since this package contains API functionality, it has been included in a package called API, so we do not call API package exactly the user interface layer. It is included here for ease of readability.

# 6. Summary Of Studies Done
- Used Docker Compose for databases and Kafka data.
- Codes of all mentioned scenarios have been written. All functionality is provided.
- Layered architecture has been implemented in microservices. SOLID and GRASP principles were followed. In short, each class in each layer does its own task. Thanks to the Spring technology, GRASP's principles such as Creator, High Cohesion, Low Coupling and Controller have been fulfilled. The written code complies with SOLID principles. As said, classes obeyed the Single Responsibility principle, also methods obeyed. Emphasis was placed on the Open/Closed principle. Thanks to interface separations in the business layer, Liskov, Dependency Inversion and Interface segregation principles were followed. If you ask why, the classes that implement these interfaces should behave the same. In addition, as the methods in the Interface multiply, they will be able to create new interfaces by taking advantage of the OOP paradigm and move only the methods to be used to them and the classes will be able to fit into new Interfaces with more than one.
- Patterns such as Config server, API Gateway and Service Discovery were used.
- Synchronous and asynchronous communications are provided. REST calls were made with openfeign for syncs, Retry pattern was applied with resillience4j. Kafka was used for asynchronous (Event Driven Design). Log simulations were made in case of possible failures of synchronous calls, this was achieved with SLF4J.
- Lombok is used for constructors and getter/setter methods of each class.
- Response Requese pattern applied. ModelMapper was used to prepare Data Transfer Objects.
- Many constructs in Java (like Enums and Validators) are well demonstrated to be used and learned.
- Added Keycloak for Authorization and Authentication.
- Zipkin has been added to visually display calls and interactions of services.

![](https://drive.google.com/uc?id=19vd8Ta1zTrwO_FlDGp8uPYDGeWl_S_9Y)
