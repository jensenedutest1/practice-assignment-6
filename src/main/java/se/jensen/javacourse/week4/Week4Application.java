package se.jensen.javacourse.week4;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import se.jensen.javacourse.week4.database.LibraryRepository;

@SpringBootApplication
public class Week4Application
{
    public static void main(String[] args)
    {
        ApplicationContext ctx = SpringApplication.run(Week4Application.class, args);
        
        LibraryRepository libraryRepository = ctx.getBean(LibraryRepository.class);
        libraryRepository.readFromDisk();
    }
}
