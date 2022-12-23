//package se.jensen.javacourse.week6;
//
//import org.checkerframework.checker.units.qual.A;
//import org.hamcrest.core.IsNull;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.mockito.internal.matchers.NotNull;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.MvcResult;
//import org.springframework.test.web.servlet.RequestBuilder;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import se.jensen.javacourse.controller.week6.LibraryController;
//import se.jensen.javacourse.database.week6.LibraryRepository;
//import se.jensen.javacourse.model.week6.Artist;
//import se.jensen.javacourse.service.week6.LibraryService;
//
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.core.IsEqual.equalTo;
//import static org.hamcrest.core.IsNull.notNullValue;
//import static org.hamcrest.core.IsNull.nullValue;
//import static org.mockito.ArgumentMatchers.isNull;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
//
////@WebMvcTest(LibraryController.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//class Week6ApplicationTests {
//
//	@MockBean LibraryRepository repo;
////	static LibraryRepository repo;
////	static LibraryService service;
//
////	@Autowired 	LibraryService service;
//	@Autowired TestRestTemplate trt;
//
//	@LocalServerPort int port;
//
//
//
//
//
//
//
//	// todo: maybe test if Jackson can convert our Java objects to JSON and vice versa
//
//
//
//
//
//
//
//
//
//
//
////	@BeforeAll
////	public static void mockTheRepo(@Autowired LibraryService _service)
////	{
////		service = _service;
////		repo = Mockito.mock(LibraryRepository.class);
//////		Mockito.when(repo.readArtistById(1)).thenReturn(new Artist(1, "Sia"));
////	}
//
//	@Test
//	public void httpGetArtistOneReturnsArtist()
//	{
//		repo = Mockito.mock(LibraryRepository.class);
//		Mockito.when(repo.readArtistById(1)).thenReturn(new Artist(1, "Sia"));
//
//		ResponseEntity<Artist> resp = trt.getForEntity(
//				"http://localhost:" + port + "/api/v1/artists/1",
//				Artist.class, "");
//
//		Artist artist = resp.getBody();
//		int code = resp.getStatusCode().value();
//
//		assertThat(code, equalTo(200));
//		assertThat(artist, is(notNullValue()));
//		System.out.println("artist.name = " + artist.getName());
//		assertThat(artist.getName(), equalTo("Sia"));
//	}
//
////	@Test
////	public void givenRepoReadArtistZeroIsMocked_whenServiceGetsArtist_thenNullIsReturned() {
////		// arrange
////		Mockito.when(repo.readArtistById(1)).thenReturn(new Artist(1, "Sia"));
////
////		// act
////		Artist artist = service.getArtistById(0);
////
////		// assert
////		assertThat(artist, is(nullValue()));
////	}
//
////	@MockBean LibraryService service;
//
////	@Autowired MockMvc mockMvc;
//
//	@Test
//	public void mockGetArtistOne() throws Exception
//	{
////		Mockito.when(service.getArtistById(1)).thenReturn(new Artist(1, "Sia"));
////
////		ResultActions ra = mockMvc.perform(
////				MockMvcRequestBuilders.get("/api/v1/artists/1"));
////
////		MvcResult res = ra.andReturn();
////		String content = res.getResponse().getContentAsString();
////
////		System.out.println("content = " + content);
//	}
//
//
////
////
////
////
////
////
////	@Test
////	public void givenRepoReadArtistIsMocked_whenServiceGetsArtist_thenArtistIsReturned() {
////		// arrange
////		// ...it's already arranged
////
////		// act
////		Artist artist = service.getArtistById(1);
////
////		// assert
////		assertThat(artist.getName(), equalTo("Sia"));
////	}
////	@Test
////	public void givenRepoReadArtistZeroIsMocked_whenServiceGetsArtist_thenNullIsReturned() {
////		// arrange
////		// ...it's already arranged
////
////		// act
////		Artist artist = service.getArtistById(0);
////
////		// assert
////		assertThat(artist, is(nullValue()));
////	}
////
////
////
////
////
////
////
////
////
////
////	@Autowired 	LibraryService service;
//
//
////	@Autowired
////	MockMvc mockMvc;
//
//
//
//}
