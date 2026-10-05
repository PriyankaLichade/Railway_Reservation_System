package com.railway.railway_reservation.dao;

import com.railway.railway_reservation.model.Passenger;
import com.railway.railway_reservation.util.QueryGenerator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class RailwayDAO {

    private final JdbcTemplate jdbcTemplate;

    public RailwayDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ==============================
    // CREATE TABLES
    // ==============================

    public void createTables() {

        jdbcTemplate.execute(
                QueryGenerator.createPassengerTable()
        );

        jdbcTemplate.execute(
                QueryGenerator.createTrainTable()
        );

        jdbcTemplate.execute(
                QueryGenerator.createReservationTable()
        );
    }

    // ==============================
    // ADD PASSENGER
    // ==============================

    public int addPassenger(Passenger passenger) {

        return jdbcTemplate.update(
                QueryGenerator.insertPassenger(),
                passenger.getName(),
                passenger.getAge(),
                passenger.getGender(),
                passenger.getPhone()
        );
    }

    // ==============================
    // GET ALL TRAINS
    // ==============================

    public List<Map<String, Object>> getAllTrains() {

        return jdbcTemplate.queryForList(
                QueryGenerator.selectAllTrains()
        );
    }

    // ==============================
    // SEARCH TRAINS
    // ==============================

    public List<Map<String, Object>> searchTrains(
            String source,
            String destination) {

        return jdbcTemplate.queryForList(
                QueryGenerator.searchTrain(),
                source,
                destination
        );
    }

    // ==============================
    // BOOK TICKET
    // ==============================

    public boolean bookTicket(
            int passengerId,
            int trainId,
            String journeyDate,
            String classType) {

        int updated = jdbcTemplate.update(
                QueryGenerator.decreaseSeat(),
                trainId
        );

        if (updated == 0) {
            return false;
        }

        String pnr = "PNR" +
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();

        Integer seatNumber = jdbcTemplate.queryForObject(
                QueryGenerator.selectSeatNumber(),
                Integer.class,
                trainId
        );

        if (seatNumber == null) {
            seatNumber = 1;
        }

        jdbcTemplate.update(
                QueryGenerator.insertReservation(),
                pnr,
                passengerId,
                trainId,
                journeyDate,
                seatNumber,
                classType
        );

        return true;
    }

    // ==============================
    // VIEW RESERVATIONS
    // ==============================

    public List<Map<String, Object>> getReservations() {

        return jdbcTemplate.queryForList(
                QueryGenerator.selectReservations()
        );
    }

    // ==============================
    // CANCEL RESERVATION
    // ==============================

    public boolean cancelReservation(int reservationId) {

        Integer trainId = jdbcTemplate.queryForObject(
                QueryGenerator.selectTrainIdFromReservation(),
                Integer.class,
                reservationId
        );

        if (trainId == null) {
            return false;
        }

        int updated = jdbcTemplate.update(
                QueryGenerator.cancelReservation(),
                reservationId
        );

        if (updated > 0) {

            jdbcTemplate.update(
                    QueryGenerator.increaseSeat(),
                    trainId
            );

            return true;
        }

        return false;
    }

    // ==============================
    // ADD SAMPLE TRAINS
    // ==============================

    public void addSampleTrains() {

        jdbcTemplate.update(
                QueryGenerator.insertTrain(),
                "12101",
                "Jnaneswari Express",
                "Nagpur",
                "Mumbai",
                100,
                100
        );

        jdbcTemplate.update(
                QueryGenerator.insertTrain(),
                "12105",
                "Vidarbha Express",
                "Nagpur",
                "Mumbai",
                120,
                120
        );

        jdbcTemplate.update(
                QueryGenerator.insertTrain(),
                "12810",
                "Howrah Express",
                "Nagpur",
                "Kolkata",
                100,
                100
        );
    }
}