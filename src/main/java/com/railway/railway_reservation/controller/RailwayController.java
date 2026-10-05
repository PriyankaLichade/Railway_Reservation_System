package com.railway.railway_reservation.controller;

import com.railway.railway_reservation.dao.RailwayDAO;
import com.railway.railway_reservation.model.Passenger;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RailwayController {

    private final RailwayDAO railwayDAO;

    public RailwayController(RailwayDAO railwayDAO) {
        this.railwayDAO = railwayDAO;

        // Create database tables
        railwayDAO.createTables();

        // Add sample trains
        railwayDAO.addSampleTrains();
    }

    // ==============================
    // ADD PASSENGER
    // ==============================

    @PostMapping("/passengers")
    public String addPassenger(@RequestBody Passenger passenger) {

        railwayDAO.addPassenger(passenger);

        return "Passenger added successfully!";
    }

    // ==============================
    // GET ALL TRAINS
    // ==============================

    @GetMapping("/trains")
    public List<Map<String, Object>> getTrains() {

        return railwayDAO.getAllTrains();
    }

    // ==============================
    // SEARCH TRAIN
    // ==============================

    @GetMapping("/trains/search")
    public List<Map<String, Object>> searchTrains(
            @RequestParam String source,
            @RequestParam String destination) {

        return railwayDAO.searchTrains(
                source,
                destination
        );
    }

    // ==============================
    // BOOK TICKET
    // ==============================

    @PostMapping("/book")
    public String bookTicket(
            @RequestParam int passengerId,
            @RequestParam int trainId,
            @RequestParam String journeyDate,
            @RequestParam String classType) {

        boolean result = railwayDAO.bookTicket(
                passengerId,
                trainId,
                journeyDate,
                classType
        );

        if (result) {
            return "Ticket booked successfully!";
        }

        return "No seats available!";
    }

    // ==============================
    // VIEW RESERVATIONS
    // ==============================

    @GetMapping("/reservations")
    public List<Map<String, Object>> getReservations() {

        return railwayDAO.getReservations();
    }

    // ==============================
    // CANCEL TICKET
    // ==============================

    @PutMapping("/reservations/{id}/cancel")
    public String cancelTicket(@PathVariable int id) {

        boolean result = railwayDAO.cancelReservation(id);

        if (result) {
            return "Reservation cancelled successfully!";
        }

        return "Reservation not found or already cancelled.";
    }
}