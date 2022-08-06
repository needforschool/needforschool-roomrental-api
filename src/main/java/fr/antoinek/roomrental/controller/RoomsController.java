package fr.antoinek.roomrental.controller;

import fr.antoinek.roomrental.entity.Rooms;
import fr.antoinek.roomrental.repository.RoomsRepository;
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
@RequestMapping("/rooms")
public class RoomsController {
    @Autowired
    private RoomsRepository repository;

    @GetMapping
    public List<Rooms> getAllRooms() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rooms> getRoomById(@PathVariable("id") String id) {
        Optional<Rooms> roomData = repository.findById(id);
        if (roomData.isPresent()) {
            return new ResponseEntity<>(roomData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @PutMapping("/{id}")
    public void modifyRoomById(@PathVariable("id") String id, @Valid
    @RequestBody Rooms rooms) {
        rooms.setId(id);
        repository.save(rooms);
    }

    @PostMapping
    public Rooms createPet(@Valid @RequestBody Rooms rooms) {
        repository.save(rooms);
        return rooms;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable String id) {
        try {
            repository.deleteById(id);
        } catch (Exception e) {
            return new ResponseEntity<>("Fail to delete!", HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>("Pet has been deleted!", HttpStatus.OK);
    }

    @DeleteMapping("/all")
    public ResponseEntity<String> deleteAllRooms() {
        try {
            repository.deleteAll();
        } catch (Exception e) {
            return new ResponseEntity<>("Fail to delete!", HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>("All Rooms has been deleted!", HttpStatus.OK);
    }
}
