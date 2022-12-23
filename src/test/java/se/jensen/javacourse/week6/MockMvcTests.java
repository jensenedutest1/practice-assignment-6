//package se.jensen.javacourse.week6;
//
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import se.jensen.javacourse.controller.week6.LibraryController;
//import se.jensen.javacourse.database.week6.LibraryRepository;
//import se.jensen.javacourse.model.week6.Artist;
//
//@WebMvcTest(LibraryController.class)
//public class MockMvcTests
//{
//    @Autowired MockMvc mockMvc;
//    @MockBean LibraryRepository repo;
//
//    @Test
//    public void mockGetArtistOne() throws Exception
//    {
//		Mockito.when(repo.readArtistById(1)).thenReturn(new Artist(1, "Sia"));
//
//		ResultActions ra = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/artists/1"));
//
//		String content = ra.andReturn().getResponse().getContentAsString();
//
//		System.out.println("content = " + content);
//    }
//}
