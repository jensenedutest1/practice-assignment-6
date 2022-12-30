
# Music Library v4

Let's try to write some tests for the Music Library API.

The `solution` branch contains all kinds of tests for every single piece of code in the project. 
A lot of those tests are redundant, but I left them in so that you can see how different Test APIs 
(`JUnit5` (`@BeforeAll`, `@BeforeEach`, `@AfterAll`...), `Mockito`, `@MockBean`, `@SpringBootTest`, 
`@WebMvcTest`, `MockMvc`, `TestRestTemplate`, etc.) are used to test different parts of the application 
using different approaches (isolated unit tests, integration tests with mocked beans, end-to-end tests with HTTP requests).

At the very least, you should try to replicate some of the most basic tests and do the same for the Project Assignment.

Make sure to check out `test/resources/application.yml` because that's where the test server is set up, 
as well as an in-memory H2 database that tries to emulate our real Postgres database.

Also note that some classes extend ContextTests (which is annotated with `@SpringBootTest` and thus starts a test server 
and a Spring context), and some don't (meaning there is no Spring context there).
ControllerUnitTests is annotated with @WebMvcTest, which also starts a Spring context, but only for making mock HTTP requests.