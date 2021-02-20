package catapa.servicea;

import java.io.IOException;
import java.util.Collections;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.IdTokenCredentials;
import com.google.auth.oauth2.IdTokenProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@RestController
public class ServiceAApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceAApplication.class, args);
	}

	public String makeAuthenticatedRequest(String url1, String url2) {
		String people="tony", movie="avatar";
		System.out.println("test");

		try {

			RestTemplate restTemplate = new RestTemplate();
			GoogleCredentials credentials;
			credentials = GoogleCredentials.getApplicationDefault();

			IdTokenCredentials tokenCredentials = IdTokenCredentials.newBuilder()
					.setIdTokenProvider((IdTokenProvider) credentials).setTargetAudience(url1).build();

			// Create an ID token
			String token;

			token = tokenCredentials.refreshAccessToken().getTokenValue();

			HttpHeaders headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setBearerAuth(token);
			HttpEntity httpEntity = new HttpEntity(headers);

			HttpEntity<People> response = restTemplate.exchange(url1, HttpMethod.GET, httpEntity, People.class);
			people = response.getBody().getName();

			restTemplate = new RestTemplate();
			credentials = GoogleCredentials.getApplicationDefault();
			tokenCredentials = IdTokenCredentials.newBuilder().setIdTokenProvider((IdTokenProvider) credentials)
					.setTargetAudience(url2).build();

			// Create an ID token

			token = tokenCredentials.refreshAccessToken().getTokenValue();
			restTemplate = new RestTemplate();

			headers = new HttpHeaders();
			headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
			headers.setBearerAuth(token);
			httpEntity = new HttpEntity(headers);

			HttpEntity<Movie> response1 = restTemplate.exchange(url2, HttpMethod.GET, httpEntity, Movie.class);
			movie = response1.getBody().getMovieName();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.print(e.getMessage());
		}
		return people + " watches " + movie;
	}

	@GetMapping(value = "/getData")
	public String sendData() {
		String uri="test",uri2="test";
		try {
			uri = System.getenv("service-b"); // service b
			uri2 = System.getenv("service-c");
		} catch (Exception e) {
			//TODO: handle exception
			System.out.print(e.getMessage());
		}
		String data = "tony";
		try {
			
			data = makeAuthenticatedRequest(uri.concat("/getPeople"), uri2.concat("/getMovie"));
		} catch (Exception e) {
			//TODO: handle exception
		}
		return data;
	}

}
