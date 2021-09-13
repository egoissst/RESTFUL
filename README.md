# Building Restful Webservices

Banuprakash C

Full Stack Architect,

Co-founder Lucida Technologies Pvt Ltd.,

Corporate Trainer,

Email: banuprakashc@yahoo.co.in

https://www.linkedin.com/in/banu-prakash-50416019/


https://github.com/BanuPrakash/RESTFUL

===================================

Softwares Required:
1) Java 8+
	https://www.oracle.com/in/java/technologies/javase/javase-jdk8-downloads.html

2) Eclipse for JEE  
	https://www.eclipse.org/downloads/packages/release/2020-03/m1/eclipse-ide-enterprise-java-developers

3) MySQL  [ Prefer on Docker]

Install Docker Desktop

Docker steps:

a) docker pull mysql

b) docker run --name local-mysql –p 3306:3306 -e MYSQL_ROOT_PASSWORD=Welcome123 -d mysql

container name given here is "local-mysql"

For Mac:
docker run -p 3306:3306 -d --name local-mysql -e MYSQL_ROOT_PASSWORD=Welcome123 mysql


c) CONNECT TO A MYSQL RUNNING CONTAINER:

$ docker exec -t -i <container_name> /bin/bash

d) Run MySQL client:

bash terminal> mysql -u "root" -p

mysql> exit

=======================================

SOLID Design Principles

D ==> Dependency Injection ==> Inversion Of Control

Container ==> Layer on top of JVM with service providers

Web Container / Servlet engine ==> DI only for web related ==> request and response, servletcontext
EJB Container ==> SessionBean, EntityBean, MessageBean

Spring Container ==> Lifecycle of bean and wiring
	lightweight container for building enterprise application ==> provides DI as core module
	Any object managed by spring container is termed as "bean"

Guice
Play Framework

============

	Spring Container:
	Metadata ==> XML

	interface EmployeeDao {
		void addEmployee(Employee e);
	}

	public class EmployeeDaoJdbcImpl implements EmployeeDao {
			public void addEmployee(Employee e) {//}
	}

	public class SampleService {
		private EmployeeDao empDao;

		public void setEmployeeDao(EmployeeDao ed) { this.empDao = ed; }

		public void insert(Employee e) {
			empDao.addEmployee(e);
		}
	}

	beans.xml

	<beans>
    <bean id="jdbcimpl" class ="pkg.EmployeeDaoJdbcImpl" />
    <bean id="service" class ="pkg.SampleService">
      <property name="employeeDao" ref="jdbcimpl" />
   	</bean>
 	</beans>

 	new ClassPathXmlApplicationContext("beans.xml");
 	OR
	new FilePathXmlApplicationContext("/users/data/a.xml")

===================================================

Spring Annotations at class level:

1) @Component
==> Utility classes / helpers 
2) @Repository
==> DAO
https://github.com/spring-projects/spring-framework/blob/main/spring-jdbc/src/main/resources/org/springframework/jdbc/support/sql-error-codes.xml
3) @Service
4) @Controller
5) @RestController
6) @Configuration

============


	interface EmployeeDao {
		void addEmployee(Employee e);
	}

	@Repository
	public class EmployeeDaoJdbcImpl implements EmployeeDao {
			public void addEmployee(Employee e) {//}
	}

	@Service
	public class SampleService {
		@Autowired
		private EmployeeDao empDao;
 
 		public void insert(Employee e) {
			empDao.addEmployee(e);
		}
	}

Spring containers creates
*  "employeeDaoJdbcImpl" instance of "EmployeeDaoJdbcImpl"
* "sampleService" instance of "SampleService"

@Service("myservice")

========

@Autowired
private EmployeeDao empDao;
OR
@Inject
private EmployeeDao empDao;

@Autowired uses internally libraries like [ CGLib / JavaAssist / ByteBuddy] for bytecode instrumentation
EmployeeDao empDao = serviceProvider.getInstance(EmployeeDao.class);
OR
EmployeeDao empDao = new EmployeeDaoJdbcImpl();



try {
	...
} catch(SQLException ex) {
	ex.getErrorCode()
}

=============

Eclipse ==> Help ==> Eclipse Market Place ==> Search for "STS" ==> Install Spring tools 4 

=========================================


Spring Boot?
Spring framework with some pre-configuration done
==> highly Opiniated Framework
* for Database related ==> configures HikariCP as database connection pool [ C3p0/ DriverManagerDataSource]
* Tomcat as embedded Servlet Container for web application
* Hibernate as ORM framework

==> makes application container ready [ Docker]
https://spring.io/guides/gs/spring-boot-docker/

Dockerfile

FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]


Without spring boot:

FROM openjdk:8-jdk-alpine
FROM tomcat:tomcat-9
FROM maven:..

mvn clean intall
mvn tomcat:run

=====================

This line creates spring container:
SpringApplication.run(DemoApplication.class, args);

in Spring framework

ApplicationContext ctx  = new ClassPathXmlApplicationContext("beans.xml");
OR
ApplicationContext ctx  = new AnnotationConfigApplicationContext();

* SpringApplication.run(DemoApplication.class, args); uses @SpringBootApplication

@SpringBootApplication is 3 in one:
1) @ComponentScan
	scans for spring components having any of the above "6" mentioned annotations from "com.adobe.demo" and
	sub-packages [com.adobe.demo.service] and creates bean

	==> @ComponentScan(basePackages="{....}")
2) @EnableAutoConfiguration
	==> spring boot is highly opiniated and creates many things out of box [ Connection Pool / Tomcat/ ORM / 3rd party
	Jackson] if these are present in the for "jar" libraries 

	@EnableAutoConfiguration(exclude={HibernateJPAVendor.class})
	@EnableAutoConfiguration(excludeName={"nameOfBean"})

3) @Configuration class

=============================

BeanFactory and ApplicationContext are interfaces to access Spring Factory

================
* To resolve multiple beans implementing an interface
1) use @Primary on one of the bean

@Repository
@Primary
public class EmployeeDaoJpaImpl implements EmployeeDao {

	@Override
	public void addEmployee() {
		System.out.println("added using JPA!!!");
	}

}

package com.adobe.demo.dao;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDaoMongoImpl implements EmployeeDao {

	@Override
	public void addEmployee() {
		System.out.println("mongo store!!!");
	}

}

----------

2) using @Qualifier [ Remove @Primary]

@Service
public class SampleService {
	@Autowired
	@Qualifier("employeeDaoMongoImpl")
	private EmployeeDao empDao;
	
	public void insertEmployee() {
		empDao.addEmployee();
	}
}


@Service
public class OtherService {
	@Autowired
	@Qualifier("employeeDaoJdbcImpl")
	private EmployeeDao empDao;
	
	public void insertEmployee() {
		empDao.addEmployee();
	}
}

===============
3) using @Profile
@Repository
@Profile("dev")
public class EmployeeDaoMongoImpl implements EmployeeDao {

	@Override
	public void addEmployee() {
		System.out.println("mongo store!!!");
	}

}


@Repository
@Profile("prod")
public class EmployeeDaoJpaImpl implements EmployeeDao {

	@Override
	public void addEmployee() {
		System.out.println("added using JPA!!!");
	}
}


3.1) 
Program Arguments:
--spring.profiles.active=dev

3.2) 
application.properties
spring.profiles.active=prod

Command Line arguments [program arguments] ==> system.properties => application.properties


3.2)

application.properties
dao=mongo

@Repository
//@Profile("dev")
@ConditionalOnProperty(name = "dao", havingValue = "mongo")
public class EmployeeDaoMongoImpl implements EmployeeDao {

	@Override
	public void addEmployee() {
		System.out.println("mongo store!!!");
	}

}

=================

@ConditionalOnMissingBean("employeeDaoMongoImpl")

===========================================================

* Spring creates instances using default constructor

Fails to create the instance of below class:
@Component
public class Example {
	public Example(String name) {

	}
}


* We need Spring container to manage bean of classes which are programatically created using "new" keyword
==> 3rd party apis which doesn't have @component, @Repository, ..

@Configuration
public class MyConfig {

@Bean(name="c3p0")
public DataSource getDataSource() {
	ComboPooledDataSource cpds = new ComboPooledDataSource();
	cpds.setDriverClass( "org.postgresql.Driver" ); //loads the jdbc driver            
	cpds.setJdbcUrl( "jdbc:postgresql://localhost/testdb" );
	cpds.setUser("swaldman");                                  
	cpds.setPassword("test-password");                                  
	cpds.setMinPoolSize(5);                                     
	cpds.setAcquireIncrement(5);
	cpds.setMaxPoolSize(20);
	return cpds;
}

}

@Service
class SampleService {
	@Autowired
	DataSource ds;
}

=============


Day 2

JPA using Spring Data JPA

JPA ==> Java Persistence API => not a part of Spring ==> It's a specification to use ORM over RDBMS.

ORM ==> Object Relational Mapping ==> Framework over JDBC to interact with RDBMS

JDBC ==> integration API [ java ] ==> Impedence mismatch
	Java ==> Employee and Address 
*	single table
	employee_id | first_name | street | city | zip ...

* Share PK
	employee table
	employee_id | first_name 

	address table
	employee_id | street | city | zip

* FK
	employee table
	employee_id | first_name 

	address table
	address_id | street | city | zip | employee_id

=====================

@Configuration
public class MyConfiguration {

	@Bean
	public DataSource getDataSource() {
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		cpds.setDriverClass( "org.postgresql.Driver" ); //loads the jdbc driver            
		cpds.setJdbcUrl( "jdbc:postgresql://localhost/testdb" );
		cpds.setUser("swaldman");                                  
		cpds.setPassword("test-password");                                  
	
		cpds.setMinPoolSize(5);                                     
		cpds.setAcquireIncrement(5);
		cpds.setMaxPoolSize(20);
		return cpds;
	}

	@Bean
	public EntityManagerFactory getEmf(DataSource ds) {
		LocalContainerEntityMaangerFactoryBean emf = new LocalContainerEntityMaangerFactoryBean();
			emf.setJPAVendor(new HibernateJPAVendor());
			<!-- emf.setDataSource(getDataSource()); -->
			emf.setDataSource(ds);
			emf.setPackagesToScan("com.adobe.prj.entity");
			...
		return emf;
	}
}


public interface ProductDao {
	void addProduct(Product p);
	Product getProduct(int id);
}

@Repository
public class ProductDaoJPAImpl implements ProductDao {
	@PersitenceContext
	private EntityManager em;

	public void addProduct(Product p) {
		em.save(p);
	}

	public Product getProduct(int id) {
		return em.findById(id);
	}
}

=========================

Spring Data JPA simplifies using JPA

* No need to write @Repository classes
* just create interface extends JPARepository

public interface ProductDao extends JPARepository<Product, Integer> {
}

* you get methods for save(), findById, findAll(), pagination , sorting, limit, delete
* can add additional methods 
* Spring uses Byte Code Instrumentation libraries to generate classes for these interface


* JPARepository ==> PageAndSortingRepository ==> CrudRepository

================

Build New Spring boot application with "MySQL", "jpa" and "web" dependencies

=============

