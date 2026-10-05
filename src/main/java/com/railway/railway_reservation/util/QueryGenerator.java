package com.railway.railway_reservation.util;

public class QueryGenerator {

    // ==============================
    // CREATE TABLE QUERIES
    // ==============================

    public static String createPassengerTable() {

        return "CREATE TABLE IF NOT EXISTS passenger (" +
                "passenger_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "age INT NOT NULL, " +
                "gender VARCHAR(20), " +
                "phone VARCHAR(15)" +
                ")";
    }

    public static String createTrainTable() {

        return "CREATE TABLE IF NOT EXISTS train (" +
                "train_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "train_number VARCHAR(20) UNIQUE NOT NULL, " +
                "train_name VARCHAR(100) NOT NULL, " +
                "source VARCHAR(100) NOT NULL, " +
                "destination VARCHAR(100) NOT NULL, " +
                "total_seats INT NOT NULL, " +
                "available_seats INT NOT NULL" +
                ")";
    }

    public static String createReservationTable() {

        return "CREATE TABLE IF NOT EXISTS reservation (" +
                "reservation_id INT AUTO_INCREMENT PRIMARY KEY, " +
                "pnr VARCHAR(20) UNIQUE NOT NULL, " +
                "passenger_id INT NOT NULL, " +
                "train_id INT NOT NULL, " +
                "journey_date DATE NOT NULL, " +
                "seat_number INT NOT NULL, " +
                "class_type VARCHAR(30), " +
                "status VARCHAR(20) DEFAULT 'CONFIRMED', " +

                "FOREIGN KEY (passenger_id) " +
                "REFERENCES passenger(passenger_id), " +

                "FOREIGN KEY (train_id) " +
                "REFERENCES train(train_id)" +

                ")";
    }


    // ==============================
    // INSERT QUERIES
    // ==============================

    public static String insertPassenger() {

        return "INSERT INTO passenger " +
                "(name, age, gender, phone) " +
                "VALUES (?, ?, ?, ?)";
    }

    public static String insertTrain() {

        return "INSERT IGNORE INTO train " +
                "(train_number, train_name, source, destination, " +
                "total_seats, available_seats) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
    }

    public static String insertReservation() {

        return "INSERT INTO reservation " +
                "(pnr, passenger_id, train_id, journey_date, " +
                "seat_number, class_type, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, 'CONFIRMED')";
    }


    // ==============================
    // SELECT QUERIES
    // ==============================

    public static String selectAllTrains() {

        return "SELECT * FROM train";
    }

    public static String searchTrain() {

        return "SELECT * FROM train " +
                "WHERE source = ? AND destination = ?";
    }

    public static String selectReservations() {

        return "SELECT r.*, " +
                "p.name AS passenger_name, " +
                "t.train_name, " +
                "t.train_number " +

                "FROM reservation r " +

                "JOIN passenger p " +
                "ON r.passenger_id = p.passenger_id " +

                "JOIN train t " +
                "ON r.train_id = t.train_id " +

                "ORDER BY r.reservation_id DESC";
    }

    public static String selectSeatNumber() {

        return "SELECT total_seats - available_seats " +
                "FROM train " +
                "WHERE train_id = ?";
    }

    public static String selectTrainIdFromReservation() {

        return "SELECT train_id " +
                "FROM reservation " +
                "WHERE reservation_id = ? " +
                "AND status = 'CONFIRMED'";
    }


    // ==============================
    // UPDATE QUERIES
    // ==============================

    public static String decreaseSeat() {

        return "UPDATE train " +
                "SET available_seats = available_seats - 1 " +
                "WHERE train_id = ? " +
                "AND available_seats > 0";
    }

    public static String cancelReservation() {

        return "UPDATE reservation " +
                "SET status = 'CANCELLED' " +
                "WHERE reservation_id = ?";
    }

    public static String increaseSeat() {

        return "UPDATE train " +
                "SET available_seats = available_seats + 1 " +
                "WHERE train_id = ?";
    }
}
