package fr.antoinek.roomrental.controller;

import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.ClientOptions;
import com.mailjet.client.errors.MailjetException;
import com.mailjet.client.resource.Emailv31;
import fr.antoinek.roomrental.entity.Rentals;
import fr.antoinek.roomrental.repository.RentalsRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * Project : spring-boot-data-jpa-mongodb
 * User: hendisantika
 * Email: hendisantika@gmail.com
 * Telegram : @hendisantika34
 * Date: 2019-01-17
 * Time: 05:52
 * To change this template use File | Settings | File Templates.
 */
@RestController
@RequestMapping("/rentals")
public class RentalsController {
    @Autowired
    private RentalsRepository repository;

    @Value("${app.mailjet.apiKey}")
    private String apiKey;

    @Value("${app.mailjet.apiSecret}")
    private String apiSecret;

    @Value("${app.mailjet.apiSender}")
    private String apiSender;

    @GetMapping
    public List<Rentals> getAllRentals() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rentals> getRentalById(@PathVariable("id") String id) {
        Optional<Rentals> rentalData = repository.findById(id);
        if (rentalData.isPresent()) {
            return new ResponseEntity<>(rentalData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/{id}")
    public List<Rentals> modifyRentalById(@PathVariable("id") String id, @Valid
    @RequestBody Rentals rental) throws MailjetException {

        // send email to the user
        if (rental.getValid()) {
            MailjetRequest request;
            MailjetResponse response;
            // send email
            ClientOptions options = ClientOptions.builder()
                    .apiKey(apiKey)
                    .apiSecretKey(apiSecret)
                    .build();
            MailjetClient client = new MailjetClient(options);

            request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.FROM, new JSONObject()
                                            .put("Email", apiSender)
                                            .put("Name", "Room Rental"))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", rental.getEmail())
                                                    .put("Name", rental.getFirstName() + " " + rental.getLastName())))
                                    .put(Emailv31.Message.SUBJECT, "Votre réservation a été validée")
                                    .put(Emailv31.Message.TEXTPART,
                                            "Votre réservation de la salle " + rental.getRoom().getName()
                                                    + " a été validée pour le "
                                                    + rental.getDate() + " " + (rental.getMorning() && rental.getAfternoon() ? "toute la journée"
                                                    : rental.getMorning() ? "matin" :
                                                    rental.getAfternoon() ? "après-midi" : "") + ".")));
            response = client.post(request);
            System.out.println(response.getStatus());
            System.out.println(response.getData());
        }

        // check if another rental with the same date, morning or afternoon is already in the database, if yes, remove it
        List<Rentals> rentals = repository.findAll();
        for (Rentals r : rentals) {
            if (r.getDate().equals(rental.getDate()) && (r.getMorning().equals(rental.getMorning()) && r.getAfternoon().equals(rental.getAfternoon()))) {
                repository.delete(r);
            }
        }
        rental.setId(id);
        repository.save(rental);

        // return all rentals
        return repository.findAll();
    }

    @PostMapping
    public Rentals createRental(@Valid @RequestBody Rentals rental) {
        rental.setValid(false);

        List<Rentals> rentals = repository.findAll();
        for (Rentals r : rentals) {
           // check if a rental from rentals is valid at the same time as the new rental then return null
            if (r.getValid() && r.getDate().equals(rental.getDate()) && (r.getMorning().equals(rental.getMorning()) || r.getAfternoon().equals(rental.getAfternoon()))) {
                return null;
            }

            // avoid duplicate rentals by checking if the new rental is the same email, firstname, lastname, date, morning or afternoon
            if (r.getEmail().equals(rental.getEmail()) && r.getFirstName().equals(rental.getFirstName()) && r.getLastName().equals(rental.getLastName()) && r.getDate().equals(rental.getDate()) && r.getMorning().equals(rental.getMorning()) && r.getAfternoon().equals(rental.getAfternoon())) {
                return null;
            }
        }

        repository.save(rental);
        return rental;
    }
}
