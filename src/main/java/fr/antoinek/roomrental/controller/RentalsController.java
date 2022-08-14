package fr.antoinek.roomrental.controller;

import fr.antoinek.roomrental.entity.Rentals;
import fr.antoinek.roomrental.repository.RentalsRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void modifyRentalById(@PathVariable("id") String id, @Valid
    @RequestBody Rentals rentals) {
        rentals.setId(id);
        repository.save(rentals);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRental(@PathVariable String id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            return new ResponseEntity<>("Fail to delete!", HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>("Pet has been deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllRentals() {
        try {
            repository.deleteAll();
        } catch (Exception e) {
            return new ResponseEntity<>("Fail to delete!", HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>("All Rentals has been deleted!", HttpStatus.OK);
    }
}
