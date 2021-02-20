package catapa.servicea;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@RestController
public class ServiceAApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceAApplication.class, args);
	} 

	@GetMapping(value = "/getData")
	public String sendData(){
		final String uri = "http://service-b:8080/getPeople"; // service b
		final String uri2 = "http://service-c:8080/getMovie"; // service c
		RestTemplate restTemplate = new RestTemplate();

		People people = restTemplate.getForObject(uri,People.class);
		Movie movie = restTemplate.getForObject(uri2, Movie.class);
		return people.getName() + " watches " + movie.getMovieName() ;
	}

}

