package es.uex.mdai2425;

import es.uex.mdai2425.services.PersonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Mdai2425Application implements CommandLineRunner {

    @Autowired
    private final PersonaService personaService;

    public Mdai2425Application(PersonaService personaService) {
        this.personaService = personaService;
    }

    public static void main(String[] args) {
        SpringApplication.run(Mdai2425Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception{
            String file = "src/main/resources/personas.json";
            personaService.insertarPersonasDesdeJson(file);
    }

}
