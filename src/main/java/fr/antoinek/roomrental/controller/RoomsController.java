package fr.antoinek.roomrental.controller;

import fr.antoinek.roomrental.entity.Rentals;
import fr.antoinek.roomrental.entity.Rooms;
import fr.antoinek.roomrental.repository.RentalsRepository;
import fr.antoinek.roomrental.repository.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
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

    @Autowired
    private RentalsRepository rentalsRepository;

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

    // search room by attendees, videoProjector, whiteboard, handicapAccess, date, morning or afternoon availability
    @GetMapping("/search")
    public List<Rooms> searchRooms(@RequestParam(value = "attendees", required = true) Integer attendees,
                                   @RequestParam(value = "videoProjector", required = true) Boolean videoProjector,
                                   @RequestParam(value = "whiteboard", required = true) Boolean whiteboard,
                                   @RequestParam(value = "handicapAccess", required = true) Boolean handicapAccess,
                                   @RequestParam(value = "date", required = true) String date,
                                   @RequestParam(value = "morning", required = true) Boolean morning,
                                   @RequestParam(value = "afternoon", required = true) Boolean afternoon) {
        // first of all check if morning or afternoon is true else return empty list
        if (!morning && !afternoon) {
            return new ArrayList<>();
        }

        List<Rooms> rooms = repository.findAll();
        // filter rooms
        // filter if attendees is equal or greater than the attendees of the room
        rooms.removeIf(room -> room.getAttendees() < attendees);

        // filter if videoProjector is equal to the videoProjector of the room
        rooms.removeIf(room -> room.getVideoProjector() != videoProjector);

        // filter if whiteboard is equal to the whiteboard of the room
        rooms.removeIf(room -> room.getWhiteboard() != whiteboard);

        // filter if handicapAccess is equal to the handicapAccess of the room
        rooms.removeIf(room -> room.getHandicapAccess() != handicapAccess);

        // verify if the room is available for the given date and time (morning or afternoon) and not valid in rentals
        rooms.removeIf(room -> {
            List<Rentals> rentals = rentalsRepository.findAll();
            for (Rentals rental : rentals) {
                if (rental.getRoom().getId().equals(room.getId())
                        && rental.getValid()
                        && rental.getDate().equals(date)
                        && rental.getMorning() == morning
                        && rental.getAfternoon() == afternoon
                ) {
                    return true;
                }
            }
            return false;
        });

        // return the list of rooms
        return rooms;
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

        return new ResponseEntity<>("Room has been deleted!", HttpStatus.OK);
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
