
# Music Library v3



Time to stop using a text file for the Music Library instead of a real database.


Now everything's implemented, apart from the database connection, and the repository class.


Your job is to:
- define the datasource in `application.properties` or `application.yml` 
(whichever you prefer),
- properly annotate the `LibraryRepository` class as a Spring 
Repository,
- autowire the `jdbcTemplate` object in the Repository class,
- and fill in the missing pieces in some of its methods.


*You should not modify any of the other files.* You’re free to do so if you want to experiment, but this task is possible to complete by only working on `LibraryRepository`.


<br>  


---


<br>



### Schema
The database contains two tables with the following schema"



<br>



Table `artists`:

| id `(PK)` | name |
|----------|------|
| SERIAL   | TEXT |



<br>



Table `tracks`:

| id `(PK)` | name | year | artist_id `(FK REFERENCES (artists) id)` |
|---------|------|------|--------------------------------------|
| SERIAL  | TEXT | INT  | INT                                  |



---



This time again I've included my (slightly updated) Postman collection of requests so that you can test your application.
