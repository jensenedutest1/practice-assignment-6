
# Music Library v4

Let's try to write some tests for the Music Library API.

The `solution` branch contains all kinds of tests for every single piece of code in the project. 
A lot of those tests are redundant, but I left them in so that you can see how different Test APIs 
(`JUnit5` (`@BeforeAll`, `@BeforeEach`, `@AfterAll`...), `Mockito`, `@MockBean`, `@SpringBootTest`, 
`@WebMvcTest`, `MockMvc`, `TestRestTemplate`, etc.) are used to test different parts of the application 
using different approaches (isolated unit tests, integration tests with mocked beans, end-to-end tests with HTTP requests).

At the very least, you should try to replicate some of the most basic tests and do the same for the Project Assignment.