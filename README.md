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

@Embeddable
public class UserPK implements Serializable {
	private String firstName;
	private String lastName;
	private String middleName;
	//
}


@Table(name="users")
@Entity
public class User {
	@EmbeddedId
	private UserPK userPK;
	..
}

==============

* spring.jpa.hibernate.ddl-auto=update

	==> DDL ==> Data Definition Language ==> CReaTE, ALTER and DROP
	update ==> map to existing table if table exist; else create table; if required alter table to match mapping
	TOP to BottOM approach
	create ==> drop and create tables for every application run ==> good for testing
	verify ==> map to existing tables; no alter on tables; if mapping fails ==> application fails
		Bottom to TOP approach

spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

* em.save() or em.findById(4); ==> check if appropriate SQL is generated


spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

* Generate SQL for MySQL8


==


docker exec -it local-mysql bash

 # mysql -u "root" -p
Enter password:Welcome123

use ADOBE_JPA

 select * from products;

insert into products  (id, name, price, category, quantity) values (0, 'Samsung Z fold', 130000.00 , 'mobile', 100);
insert into products  (id, name, price, category, quantity) values (0, 'Sony Bravia', 95000.00 , 'tv',100);
insert into products  (id, name, price, category, quantity) values (0, 'Onida Thunder', 3500.00 , 'tv', 100);
insert into products  (id, name, price, category, quantity) values (0, 'Logitech Mouse', 600.00 , 'computer', 100);
insert into products  (id, name, price, category, quantity) values (0, 'Lamp', 900.00 , 'electrical', 100);


===============

@Query(value= "select * from products where price >= :low and price <= :high", nativeQuery=true)

=======================================

REST ==> Representational State Transfer
* Resource on server [ database / noSQL/ files / printer]
* state of the resource is served to clients in various representation [ XML / JSON / CSV / ATOM ]

Characterstics of REST
1) client - server logic seperation
2) Stateless [ Server should not have client session details ==> No Cookies / HttpSession concepts]
3) Cacheable [ client side [ Cache-Control, Expires, ETag] or middle tier [Redis]]
4) Uniform Resource

Resource URI should be plural nouns and actions are done using HTTP verbs

Resource: http://server/api/v1/products

* 1)
GET:
http://server/api/v1/products

get all products representation based on "accept" HTTP header

2) 

GET:
http://server/api/v1/products/5

extra path param is to identify based on PK or sub-resources

--> get product represetntation of product whose id is 5

http://server/api/v1/customers/banu@gmail.com/orders

3)

http://server/api/v1/products?page=3&size=10
http://server/api/v1/products?lower=1000&higher=10000

Query param for filtered response

============

4) 
POST
http://server/api/v1/products

"content-type" header is used to specify what type of representation is sent to server

===

5) 
DELETE
http://server/api/v1/products/5

6)
PUT
http://server/api/v1/products/5

"content-type" header is used to specify what type of representation is sent to server which needs to be used
to modify product whose id is "5"

7) PATCH
http://server/api/v1/products/5

for partial update of product

8) JSON-PATCH+PATCH
"content-type" : "application/json-patch+patch"
http://server/api/v1/products/5

payload:

{
	"op" : "replace",
	"path" "/price",
	"value" : 5555.22
}

==

 op can be "add", "replace", "remove"


=========================

GET and DELETE can't have payload ==> IDEMPOTENT

POST, PUT, PATCH can have payload ==> NOT IDEMPOTENT

===============================================


@RestController
@RequestMapping("/api/products")
public class ProductController {

	@GetMapping()
	m1() {

	}

	@PostMapping()
	m2() {

	}
}


==

@RestController
@RequestMapping("/api/customers)
public class ProductController {

	@GetMapping()
	m1() {

	}

	@PostMapping()
	m2() {

	}
}


@PostMapping()
@ResponseStatus(HttpStatus.CREATED)
	public  @ResponseBody Product addProduct(@RequestBody Product p) {
		Product prd = service.saveProduct(p);
		return prd;
	}
		
=========

Browser:
GET
http://localhost:8080/api/products
GET using path param @PathVariable
http://localhost:8080/api/products/4
GET by Query param @RequestParam
http://localhost:8080/api/products?low=1000&high=100000



=========
Download POSTMAN

POSTMAN for POST, PUT and DELETE testing

================================================


Day 3

RESTful Web services
Pathparam and QueryParam
@RestController
@ResponseBody
@RequestBody
ResponseEntity
Mapping HTTP methods

JPA ==> JPARepository which comes with predifened methods for CRUD operations; we can add our own methods using @Query
JPQL and SQL

Tested  GET requests using Browser 
POSTMAN

=======================================================

CustomerService
	DAO methods
AdminService
	DAO methods

=======================

POST request:

POSTMAN

POST http://localhost:8080/api/products

Headers:
Accept: application/json
Content-type: application/json

Body: [raw]

{
    "name" : "LG AC",
    "price" : 45000.00,
    "category": "ELECTRICAL",
    "quantity" : 500
}

====================================
Core Container of Spring ==> DI
Web module ==> Building traditional web applocaiton and RESTful Web services
Spring Data JPA ==> integrate with ORM
AOP ==> Spring Boot module

Aspect Oriented Programming

----------------------------
Goal : eliminate code scattering and code tangling 

What to eliminate? ==> Cross cutting concern ==> not a part of main logic but can be used along with main logic
* logging
* security
* Transaction
* Profile

public void transferFunds(...){
		security logic
		logging
		transaction
		main1
		main2
		logging
		main3
		transaction
}

============

* Aspect ==> Concern which can be used along with main logic [ Logging, Security,]
	these aspects can be weaved / wiring into application

* JoinPoint ==> a place where Aspect can be weaved [ any method or exception]
		All methods and exception qualifies

* PointCut ==> Selected JoinPoints

* Advice ==> Before, After, AfterReturing, Around, Throws

======================================

Resolved [org.springframework.web.bind.MethodArgumentNotValidException: Validation failed for argument [0] in public org.springframework.http.ResponseEntity<com.adobe.prj.entity.Product> com.adobe.prj.api.ProductController.addProduct(com.adobe.prj.entity.Product) with 2 errors: 
	[Field error in object 'product' on field 'name': rejected value []; 
	codes [NotBlank.product.name,NotBlank.name,NotBlank.java.lang.String,NotBlank]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [product.name,name]; arguments []; default message [name]]; default message [Name is required]] 

	[Field error in object 'product' on field 'price': rejected value [-99.0]; codes [Min.product.price,Min.price,Min.double,Min]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [product.price,price]; arguments []; default message [price],10]; 

	default message [Price -99.0 should be more than 10]] ]



ProductController.java
	@PostMapping()
	public ResponseEntity<Product> addProduct(@RequestBody @Valid Product p) {
		Product prd = service.saveProduct(p);
		return new  ResponseEntity<>(prd, HttpStatus.CREATED);  // 201
	}

=====

package com.adobe.prj.api;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.adobe.prj.service.NotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<Object> handleNotFoundException(NotFoundException ex) {
		Map<String, Object> body = new LinkedHashMap<String, Object>();
		body.put("message", ex.getMessage());
		return new ResponseEntity<Object>(body, HttpStatus.BAD_REQUEST);
	}
	
	@Override
	public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<String, Object>();
		
		body.put("timestamp", LocalDate.now());
		body.put("status", status.value());
		
		List<String> errors = ex.getBindingResult()
					.getFieldErrors().stream()
					.map(exception -> exception.getDefaultMessage())
					.collect(Collectors.toList());
		
		body.put("errors", errors);
		return new ResponseEntity<Object>(body, HttpStatus.BAD_REQUEST);
	}
}

===========================================

Unit Testing RestController
Spring boot starter test includes:
	==> JUnit as Unit testing framework / TestNG
	==> Hamcrest ==> Assertion libraries
	==> json-path ==> to validated JSON data
	==> Mockito ==> Mock API

Don't create complete Spring container 
@WebMvcTest(ProductController.class) ==> loads only ProductController on Spring container

* create a mock OrderService
@MockBean
private OrderService service;

* 	use MockMVC to trigger REST calls [ GET/ POSt / ..]
==> uses SpringDispatcherServletTest not DispatcherServlet

@Autowired
private MockMvc mockMvc;


=======
class Order {
@ManyToOne()
	@JoinColumn(name="customer_email") // FK column
	private Customer customer;
	



@Entity
@Table(name="customers")
public class Customer {

	@Id
	private String email;


create table orders ( .., customer_email references customers(email))

======


public interface CustomerDao extends JpaRepository<Customer, String>{

}

public interface OrderDao extends JpaRepository<Order, Integer> {

}

"Will not create ItemDao"

Order has 4 items;
With ItemDao

orderDao.save(order);
itemDao.save(i1);
itemDao.save(i2);
itemDao.save(i3);
itemDao.save(i4);


Delete:

orderDao.delete(order);
itemDao.delete(i1);
itemDao.delete(i2);
itemDao.delete(i3);
itemDao.delete(i4);

Without ItemDao and with Cascade:

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="order_fk") // FK
	private List<Item> items = new ArrayList<>();



Order has 4 items;
orderDao.save(order); // takes care of saving items also

orderDao.delete(order); // takes care of deleting items also

===============================

OneToMAny Default Fetch operation is LAZY, ManyToOne default is EAGER

orderDao.findById(1);

select * from orders where id = 1;

==
fetch = FetchType.EAGER

orderDao.findById(1);

select * from orders o inner join items i where o.oid = i.order_fk

order json:

{
	"customer":  {
		"email": "sam@adobe.com"
	},
	"items" : [
			{"qty": 3, "product": {"id":5}},
		 	{"qty": 1, "product": {"id":1}}
	]	
}

=====

@Transactional is an Around type of Advice [ AOP ]

if method on which this is placed doesn't throw exception it invokes "commit" else "rollback"

=========

Optimistic locking:


@Table(name = "products")
@Entity
public class Product  {

@Version
private int version;

products

id | name | quantity | version
22   x       100          0

user 1:
changes done
update products set quantity = 95, version = version + 1 where id = 22 and version = 0

user 2:
update products set quantity = 98, version = version + 1 where id = 22 and version = 0

 
Let's assume user 2 commits first


id | name | quantity | version
22   x       98          1

User 1 trys to commit

=============================

<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
			<version>2.7.0</version>
		</dependency>
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
			<version>2.7.0</version>
		</dependency>
		
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>

		<dependency>
			<groupId>io.micrometer</groupId>
			<artifactId>micrometer-registry-prometheus</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-cache</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-redis</artifactId>
		</dependency>

docker run -d -p 6379:6379 --name my-redis redis


NodeJS installed

$ npm install -g redis-commander
$ redis-commander

============


Day 4

* Mapping associations
* Cascade  and FETCH
* @Transactional
* Dirty checking
* RESTController

@Query("from Order o inner join Customer c")
	List<Object[]> getReport();

	Object[0] ==> Order
	Object[1] ==> Customer

@Query("select o.orderDate, o.total, c.firstName, c.email from Order o inner join Customer c")
	List<Object[]> getReport();

Object[0] ==> orderDate
object[1] ==> total
...

@Query("select new com.adobe.prj.entity.ReportDTO(o.orderDate, o.total, c.firstName, c.email) from Order o inner join Customer c")
List<ReportDTO> getReport();

=================================
1) Consume RESTapis in Java [ Spring Boot]
2) can also be used for Integration Testing http://localhost:8080/api/products
3) Useful in Microservices
* RestTemplate
* WebClient

==================================

RestTemplate:

package com.adobe.prj;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.adobe.prj.entity.Product;
import com.adobe.prj.service.OrderService;

@SpringBootApplication
public class RestfulexampleApplication implements CommandLineRunner {
	@Autowired
	private OrderService service;
	
	@Autowired
	private RestTemplate template;
	
	@Bean
	public RestTemplate getRestTemplate(RestTemplateBuilder builder) {
			return builder.build();
	}
	
	private void getAllProducts() {
		String result = template.getForObject("http://localhost:8080/api/products", String.class);
		System.out.println(result);
		
		System.out.println("************");
		
		ResponseEntity<List<Product>> response = template.exchange("http://localhost:8080/api/products", 
				HttpMethod.GET, null, new ParameterizedTypeReference<List<Product>>() {
		});
		
		List<Product> products = response.getBody();
		for(Product p : products) {
			System.out.println(p);
		}
	}
		
	private void getProduct() {
		ResponseEntity<Product> response = template.getForEntity("http://localhost:8080/api/products/1", Product.class);
		System.out.println(response.getBody().getName());
	}
	
	private void addUsingRestTemplate() {
		Product p = new Product(0,"Tata Sky", 4500.00, "tv");
		ResponseEntity<Product> resp = template.postForEntity("http://localhost:8080/api/products/", p, Product.class);
		System.out.println(resp.getStatusCodeValue());
		System.out.println(resp.getBody().getId());
	}
	
	public static void main(String[] args) {
		SpringApplication.run(RestfulexampleApplication.class, args);
	}
	
	/*
	 * this method gets called once spring container is created
	 */
	@Override
	public void run(String... args) throws Exception {
//		addProduct();
//		getProducts();
		System.out.println("***** Adding *****");
		addUsingRestTemplate();
		System.out.println("*** GET ALL **********");
		getAllProducts();
		System.out.println("***** Get By ID *******");
		getProduct();
	}

	private void getProducts() {
		List<Product> products = service.getProducts();
		for(Product p : products) {
			System.out.println(p);
		}
	}

	private void addProduct() {
		Product p = new Product(0, "iPhone 12", 120000.00, "mobile");
		p.setQuantity(100);
		service.saveProduct(p);
	}

}

===


// create headers
HttpHeaders headers = new HttpHeaders();
// set `content-type` header
headers.setContentType(MediaType.APPLICATION_JSON);
// set `accept` header
headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

 

// build the request
HttpEntity<Map<Product, Object>> entity = new HttpEntity<>(product, headers);

// send POST request
ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);



===============================

REST Documentation
* RESTful API Modeling Language is a YAML-based language for describing RESTful APIs.

YaML files

spring.datasource.
	url=jdbc:mysql://localhost:3306/ADOBE_JPA?createDatabaseIfNotExist=true
	driverClassName=com.mysql.cj.jdbc.Driver

================================

* OpenAPI Specification
=> Swagger

======================
http://localhost:8080/swagger-ui.html
http://localhost:8080/v2/api-docs

==> @ApiOperation
==> @ApiModel

=================
RESTful web services can be layered

* Caching
==> Client side caching
1) using HTTP headers ==> Cache-Control, Expires, ETag

ETag ==> Entity Tag ==> when ResponseEntity is sent along with it send ETag [ can be hashCode / version]
=> First flow server sends response and ETag
==> subsequent requests Client has to send ETag along with the request
Accept: application/json
If-Not-Match: "etagvalue"

GET http://localhost:8080/api/products/cache/1

Headers we get 
Etag: "-2019683972"

==> server hits the DB pulls the data generates ETag and matched with header from client ==> SC 302 or new data


==> Server side caching
	==> JPA ==> First level [enabled by default ] and Second level caching
	@Transactional
	public Product getProduct(int id) {
		Product p = productDao.findById(1); // hits the DB
		...
		 Product p2 = productDao.findById(1); // points to cached product ==> no hit to DB
	}
	* Second level caching JBOSSSwarm Cache / EHCache
	ehcache.xml
<?xml version="1.0" encoding="UTF-8"?>
<ehcache xmlns:xsi="https://www.w3.org/2001/XMLSchema-instance"
xsi:noNamespaceSchemaLocation="ehcache.xsd" updateCheck="true"
monitoring="autodetect" dynamicConfig="true">

<defaultCache eternal="false" timeToLiveSeconds="30"
memoryStoreEvictionPolicy="LRU" statistics="true" maxElementsInMemory="10000"
overflowToDisk="false" />

<cache name="book" maxEntriesLocalHeap="10000" eternal="false"
timeToIdleSeconds="60" timeToLiveSeconds="60" memoryStoreEvictionPolicy="LRU"
statistics="true">
</cache>

</ehcache>
==> Avoid hiting DB

==> Middle tier caching
* ConcurrentHashMap is configured to be used as CacheManager
* Redis

Steps: 
1) install redis on docker
2) Add dependecnies
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-cache</artifactId>
		</dependency>
		
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

3) Enable Caching
@EnableCaching
by default uses ConcurrentMapCacheManager
this object will get created if no other CacheManagers are available @Conditional(..)

spring.cache.redis.time-to-live=30000

Serialization ==> data to stream
Add Custom Config "RedisCustomConfig.java"

* @Cacheable
@Cacheable(value="productCache")
@Cacheable(value="productCache", key="#id")
* condition is based on parameter values

@Cacheable(value="productCache", key="#p.id", condition="#p.price>50000")
@PostMapping()
	public ResponseEntity<Product> addProduct(@RequestBody @Valid Product p) {
		Product prd = service.saveProduct(p);
		return new  ResponseEntity<>(prd, HttpStatus.CREATED);  // 201
	}

* result is returned object
* unless is condition on return value
@Cacheable(value="productCache", key="#result.id", unless='#result==null')
public @ResponseBody  Product  getProduct(@PathVariable("pid") int id) throws NotFoundException {
		return service.getProduct(id);
}

* @CacheEvict

@CacheEvict(value="productCache", key="#id")
public void removeProduct(int id) {

}


@CacheEvict(value="productCache", allEntries=true)
@RequestMapping("/api/products/clear")
public void clearCache() {

}
* @CachePut
to update the cache entry
@CachePut(value="productCache", key="#id")
public void updateProduct(int id, double price) {
	...
}


npx redis-commander
=======================
productDao.findById(1).get(); // EAGER fetching 

Product p = productDao.getById(1); // Lazy Fetching ==> Return a Proxy not actual "p is proxy object"

p.getName(); // at this point hits the DB and populates the Product

===

JPA Association Mapping
OneToMany == Lazy Fetching
ManyToOne == Eager Fetching

resources folder can have "schema.sql" and "data.sql" ==> gets called as soon as application is started

============================

@NamedEntityGraphs({

        @NamedEntityGraph(name = "companyWithDepartmentsGraph",
                attributeNodes = {@NamedAttributeNode("departments")}),
        
        @NamedEntityGraph(name = "companyWithDepartmentsAndEmployeesGraph",
                attributeNodes = {
                		@NamedAttributeNode(value = "departments", subgraph = "departmentsWithEmployees")},
                subgraphs = @NamedSubgraph(
                        name = "departmentsWithEmployees",
                        attributeNodes = @NamedAttributeNode("employees"))),
        
        @NamedEntityGraph(name = "companyWithDepartmentsAndEmployeesAndOfficesGraph",
                attributeNodes = {@NamedAttributeNode(value = "departments", 
                subgraph = "departmentsWithEmployeesAndOffices")},
                subgraphs = @NamedSubgraph(
                        name = "departmentsWithEmployeesAndOffices",
                        attributeNodes = {@NamedAttributeNode("employees"), @NamedAttributeNode("offices")}))
})
public class Company {
	 @Id
	    @Column(name = "id", updatable = false, nullable = false)
	    private Long id = null;

	    private String name;

	    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
	    private Set<Department> departments = new HashSet<>();

	    @OneToMany(mappedBy = "company", fetch = FetchType.EAGER)
	    private Set<Car> cars = new HashSet<>();


==============

companyDao.findWithGraph(1L, "companyWithDepartmentsGraph");
 select
        company0_.id as id1_2_0_,
        company0_.name as name2_2_0_,
        department1_.company_id as company_3_3_1_,
        department1_.id as id1_3_1_,
        department1_.id as id1_3_2_,
        department1_.company_id as company_3_3_2_,
        department1_.name as name2_3_2_ 
    from
        company company0_ 
    left outer join
        department department1_ 
            on company0_.id=department1_.company_id 
    where
        company0_.id=?

================

companyDao.findWithGraph(1L, "companyWithDepartmentsAndEmployeesAndOfficesGraph");

select
        company0_.id as id1_2_0_,
        company0_.name as name2_2_0_,
        department1_.company_id as company_3_3_1_,
        department1_.id as id1_3_1_,
        department1_.id as id1_3_2_,
        department1_.company_id as company_3_3_2_,
        department1_.name as name2_3_2_,
        employees2_.department_id as departme5_4_3_,
        employees2_.id as id1_4_3_,
        employees2_.id as id1_4_4_,
        employees2_.address_id as address_4_4_4_,
        employees2_.department_id as departme5_4_4_,
        employees2_.name as name2_4_4_,
        employees2_.surname as surname3_4_4_,
        offices3_.department_id as departme4_5_5_,
        offices3_.id as id1_5_5_,
        offices3_.id as id1_5_6_,
        offices3_.address_id as address_3_5_6_,
        offices3_.department_id as departme4_5_6_,
        offices3_.name as name2_5_6_ 
    from
        company company0_ 
    left outer join
        department department1_ 
            on company0_.id=department1_.company_id 
    left outer join
        employee employees2_ 
            on department1_.id=employees2_.department_id 
    left outer join
        office offices3_ 
            on department1_.id=offices3_.department_id 
    where
        company0_.id=?


================

ACUTATOR
HATEOAS
PROTOBUF

===

Security

http_server_requests_seconds_count{uri="/api/products"}
rate(http_server_requests_seconds_count{uri="/api/products"}[5m]) > 0


=================================================================================

Day 5

------

Day 4 Recap:
* JPQL joins
* using Cache CacheManager ==> ConcurrentMapCacheManager, RedisCacheManager
* EntityGraph to solve EAGER and FETCH issues of loading associations

-----------------------------------------

Metrics: Health, CPU usage, Threads Usage, HTTP Requests, ...

actuator library of spring boot provides micrometer to generate metrics 

Time-series database server to store these metrics and generate report [ Prometheus / Graphafa / Netflix Atlas/]

Prometheus scrape data 

actutator + Prometheus

	<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>

		<dependency>
			<groupId>io.micrometer</groupId>
			<artifactId>micrometer-registry-prometheus</artifactId>
		</dependency>


==

application.properties
management.endpoints.web.exposure.include=*

management.metrics.distribution.percentiles-histogram.http.server.requests=true

===

start.txt
prometheus.yml
rules.yml

docker run -d --name=prometheus -p 9090:9090 -v C:\prometheus\prometheus.yml:/etc/prometheus/prometheus.yml prom/prometheus --config.file=/etc/prometheus/prometheus.yml


docker cp C:\prometheus\rules.yml prometheus:/etc/prometheus/rules.yml



ab -c 100 -n 1000 http://localhost:8080/api/products

http_server_requests_seconds_count{uri="/api/products"}
rate(http_server_requests_seconds_count{uri="/api/products"}[5m]) > 0


===============

Custom metrics :check Micrometer @Timed annotation 

===========================================================================

HATEOAS ==> Hypermedia As The Extension Of Application State ==> Level 3  RESTful Web services

Level 2 ==> URI ==> Nouns and HTTP methods as Verbs [ GET / POST / PUT / DELETE]

WebMvcLinkBuilder used to create Links in representation


Entity ==> Model ==> Domain class

HATEOAS ==> RepresentionModel

{
	id:1
	description:"Red Velvet",
	status: ..,
	_links : {
		"payment" : "http://localhost:8080/orders/1/pay",
		"cancel": "http://localhost:8080/orders/1/cancel"

	}
}

===
Payment
http://localhost:8080/orders/1/pay

===

http://localhost:8080/orders

[{"id":1,"orderStatus":"BEING_CREATED","description":"Red Velvet"},{"id":2,"orderStatus":"BEING_CREATED","description":"Mocha"}]


http://localhost:8080/orders/1

{"id":1,"orderStatus":"BEING_CREATED","description":"Red Velvet",
	"_links":
		{
		"payment":[{"href":"http://localhost:8080/orders/1/pay"},{"href":"http://localhost:8080/orders/1/pay"}],
		"cancel":[{"href":"http://localhost:8080/orders/1/cancel"},{"href":"http://localhost:8080/orders/1/cancel"}]
	}
}


POSTMAN:
POST : http://localhost:8080/orders/1/pay

Response:
{
    "id": 1,
    "orderStatus": "PAID_FOR",
    "description": "Red Velvet"
}

===
GET
http://localhost:8080/orders/1

{"id":1,"orderStatus":"PAID_FOR","description":"Red Velvet",
"_links":
	{"fulfill":[{"href":"http://localhost:8080/orders/1/fulfill"},{"href":"http://localhost:8080/orders/1/fulfill"}]}}

======================
 


Spring Data REST is part of the umbrella Spring Data project and makes it easy to build hypermedia-driven REST web services on top of Spring Data repositories [ spring-data-jpa, mongorepository] .

<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-rest</artifactId>
</dependency>

===========================

@EnableAsync ==> setup using Asynchronous operation using thread pool

Executor pool = ExecutorService.newFixedThreadPool(100);

=============================================

Spring boot
* web
==> Tomcat embedded container
==> Threads

* webflux ==> Reactive programming
==> netty Reactor as Web server
==> Event loop

=========

Spring boot webflux provides "Mono" and "Flux" types of data

"Mono" is single data
"FLux" is a collection

=========

reactivespring.zip

<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
https://projectreactor.io/

================
https://rxmarbles.com/

==================







