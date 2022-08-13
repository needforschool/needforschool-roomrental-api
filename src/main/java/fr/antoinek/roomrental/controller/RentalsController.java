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
    public Rentals createRental(@Valid @RequestBody Rentals rentals) {
        repository.save(rentals);
        return rentals;
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
