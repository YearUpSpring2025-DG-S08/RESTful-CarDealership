package com.pluralsight.dealership.data;

import com.pluralsight.dealership.models.Vehicle;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// the repository tag tells Spring that this class handles the database work
@Repository
public class DealershipDAO {
    private static final Logger logger = LogManager.getLogger(DealershipDAO.class);
    private final BasicDataSource dataSource;

    @Autowired
    public DealershipDAO(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    
    // search query methods
    @Bean
    public Vehicle getByVin(int userChosenVin) {
        Vehicle queryVehicle = null;

        // we need to ask for the data
        String query = """
                select v.VIN, v.`year`, v.make, v.model, v.`type`, v.color, v.mileage, v.price
                from car_dealership.vehicles v
                inner join car_dealership.inventory i on i.VIN = v.VIN
                where i.VIN = ? and i.is_Sold = false;
                """;

        // we need to make a connection to the database, send a prepared statement of the query, and execute it
        try (Connection c = dataSource.getConnection();
             PreparedStatement s = c.prepareStatement(query)) {

            // protect from sql injection
            s.setInt(1, userChosenVin);

            ResultSet queryResults = s.executeQuery();


            // boolean to check whether query results has any rows
            boolean hasRows = false;
            // loop through results and parse the information to be saved
            while (queryResults.next()) {
                hasRows = true;

                int vin = queryResults.getInt(1);
                int year = queryResults.getInt(2);
                String make = queryResults.getString(3);
                String model = queryResults.getString(4);
                String type = queryResults.getString(5);
                String color = queryResults.getString(6);
                double mileage = queryResults.getDouble(7);
                double price = queryResults.getDouble(8);

                // create a new instance of the Vehicle model and add to the array to be returned
                queryVehicle = new Vehicle(vin, year, make, model, type, color, mileage, price, false);
            }

            // add a logging message to communicate with user
            if (hasRows) {
                logger.info("✅ Successfully retrieved vehicles with the vin: {} ✅", userChosenVin);
            } else {
                logger.warn("❌ No vehicles found with the vin: {} ❌", userChosenVin);
            }

        } catch (SQLException e) {
            logger.error("Could not query vehicles by vin");
        }


        return queryVehicle;
    }

    @Bean
    public List<Vehicle> getByPrice(double minPrice, double maxPrice) {
        // we need a place to hold the results
        ArrayList<Vehicle> results = new ArrayList<>();

        // we need to ask for the data
        String query = """
                select v.VIN, v.`year`, v.make, v.model, v.`type`, v.color, v.mileage, v.price
                from car_dealership.vehicles v
                inner join car_dealership.inventory i on i.VIN = v.VIN
                WHERE v.price >= ? AND v.price <= ? and i.is_Sold = false;
                """;

        // we need to make a connection to the database, send a prepared statement of the query, and execute it
        try (Connection c = dataSource.getConnection();
             PreparedStatement s = c.prepareStatement(query)) {

            // protect from sql injection
            s.setDouble(1, minPrice);
            s.setDouble(2, maxPrice);

            ResultSet queryResults = s.executeQuery();


            // boolean to check whether query results has any rows
            boolean hasRows = false;
            // loop through results and parse the information to be saved
            while (queryResults.next()) {
                hasRows = true;

                int vin = queryResults.getInt(1);
                int year = queryResults.getInt(2);
                String make = queryResults.getString(3);
                String model = queryResults.getString(4);
                String type = queryResults.getString(5);
                String color = queryResults.getString(6);
                double mileage = queryResults.getDouble(7);
                double price = queryResults.getDouble(8);

                // create a new instance of the Vehicle model and add to the array to be returned
                Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, mileage, price, false);
                results.add(vehicle);
            }

            // add a logging message to communicate with user
            if (hasRows) {
                logger.info("✅ Successfully retrieved vehicles within the price range: ${} and ${} ✅", minPrice, maxPrice);
            } else {
                logger.warn("❌ No vehicles found within the price range: ${} and ${} ❌", minPrice, maxPrice);
            }

        } catch (SQLException e) {
            logger.error("Could not query vehicles by price");
        }


        return results;
    }

    @Bean
    public List<Vehicle> getByMake(String userChosenMake) {
        // we need a place to hold the results
        ArrayList<Vehicle> results = new ArrayList<>();

        // we need to ask for the data
        String query = """
                select v.VIN, v.`year`, v.make, v.model, v.`type`, v.color, v.mileage, v.price
                from car_dealership.vehicles v
                inner join car_dealership.inventory i on i.VIN = v.VIN
                where v.make = ? and i.is_Sold = false;
                """;

        // we need to make a connection to the database, send a prepared statement of the query, and execute it
        try (Connection c = dataSource.getConnection();
             PreparedStatement s = c.prepareStatement(query)) {

            // protect from sql injection
            s.setString(1, userChosenMake);

            ResultSet queryResults = s.executeQuery();

            // boolean to check whether query results has any rows
            boolean hasRows = false;
            // loop through results and parse the information to be saved
            while (queryResults.next()) {
                hasRows = true;
                int vin = queryResults.getInt(1);
                int year = queryResults.getInt(2);
                String make = queryResults.getString(3);
                String model = queryResults.getString(4);
                String type = queryResults.getString(5);
                String color = queryResults.getString(6);
                double mileage = queryResults.getDouble(7);
                double price = queryResults.getDouble(8);

                // create a new instance of the Vehicle model and add to the array to be returned
                Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, mileage, price, false);
                results.add(vehicle);
            }

            // add a logging message to communicate with user
            if (hasRows) {
                logger.info("✅ Successfully retrieved vehicles with the make: {} ✅", userChosenMake);
            } else {
                logger.warn("❌ No vehicles found with make: {} ❌", userChosenMake);
            }

        } catch (SQLException e) {
            logger.error("Could not query vehicles by make");
        }


        return results;
    }

    @Bean
    public List<Vehicle> getByModel(String userChosenModel) {
        // we need a place to hold the results
        ArrayList<Vehicle> results = new ArrayList<>();

        // we need to ask for the data
        String query = """
                select v.VIN, v.`year`, v.make, v.model, v.`type`, v.color, v.mileage, v.price
                from car_dealership.vehicles v
                inner join car_dealership.inventory i on i.VIN = v.VIN
                where v.model = ? and i.is_Sold = false;
                """;

        // we need to make a connection to the database, send a prepared statement of the query, and execute it
        try (Connection c = dataSource.getConnection();
             PreparedStatement s = c.prepareStatement(query)) {

            // protect from sql injection
            s.setString(1, userChosenModel);

            ResultSet queryResults = s.executeQuery();

            // boolean to check whether query results has any rows
            boolean hasRows = false;
            // loop through results and parse the information to be saved
            while (queryResults.next()) {
                hasRows = true;
                int vin = queryResults.getInt(1);
                int year = queryResults.getInt(2);
                String make = queryResults.getString(3);
                String model = queryResults.getString(4);
                String type = queryResults.getString(5);
                String color = queryResults.getString(6);
                double mileage = queryResults.getDouble(7);
                double price = queryResults.getDouble(8);

                // create a new instance of the Vehicle model and add to the array to be returned
                Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, mileage, price, false);
                results.add(vehicle);
            }

            // add a logging message to communicate with user
            if (hasRows) {
                logger.info("✅ Successfully retrieved vehicles with the model: {} ✅", userChosenModel);
            } else {
                logger.warn("❌ No vehicles found with the model: {} ❌", userChosenModel);
            }

        } catch (SQLException e) {
            logger.error("Could not query vehicles by model");
        }


        return results;
    }

    @Bean
    public List<Vehicle> getByMakeModel(String userChosenMake, String userChoseModel) {
        // we need a place to hold the results
        ArrayList<Vehicle> results = new ArrayList<>();

        // we need to ask for the data
        String query = """
                select v.VIN, v.`year`, v.make, v.model, v.`type`, v.color, v.mileage, v.price
                from car_dealership.vehicles v
                inner join car_dealership.inventory i on i.VIN = v.VIN
                where v.make = ? and v.model = ? and i.is_Sold = false
                """;

        // we need to make a connection to the database, send a prepared statement of the query, and execute it
        try (Connection c = dataSource.getConnection();
             PreparedStatement s = c.prepareStatement(query)) {

            // protect from sql injection
            s.setString(1, userChosenMake);
            s.setString(2, userChoseModel);

            ResultSet queryResults = s.executeQuery();


            // boolean to check whether query results has any rows
            boolean hasRows = false;
            // loop through results and parse the information to be saved
            while (queryResults.next()) {
                hasRows = true;
                int vin = queryResults.getInt(1);
                int year = queryResults.getInt(2);
                String make = queryResults.getString(3);
                String model = queryResults.getString(4);
                String type = queryResults.getString(5);
                String color = queryResults.getString(6);
                double mileage = queryResults.getDouble(7);
                double price = queryResults.getDouble(8);

                // create a new instance of the Vehicle model and add to the array to be returned
                Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, mileage, price, false);
                results.add(vehicle);
            }

            // add a logging message to communicate with user
            if (hasRows) {
                logger.info("✅ Successfully retrieved vehicles with the make and model: {} {}✅", userChosenMake, userChoseModel);
            } else {
                logger.warn("❌ No vehicles found with make and model: {} {}❌", userChosenMake, userChoseModel);
            }

        } catch (SQLException e) {
            logger.error("Could not query vehicles by make and model");
        }


        return results;
    }

    @Bean
    public List<Vehicle> getByYear(double minYear, double maxYear) {
        // we need a place to hold the results
        ArrayList<Vehicle> results = new ArrayList<>();

        // we need to ask for the data
        String query = """
                select v.VIN, v.`year`, v.make, v.model, v.`type`, v.color, v.mileage, v.price
                from car_dealership.vehicles v
                inner join car_dealership.inventory i on i.VIN = v.VIN
                where v.`year`
                between 2019 and 2024 and i.is_Sold = false
                """;

        // we need to make a connection to the database, send a prepared statement of the query, and execute it
        try (Connection c = dataSource.getConnection();
             PreparedStatement s = c.prepareStatement(query)) {

            // protect from sql injection
            s.setDouble(1, minYear);
            s.setDouble(2, maxYear);

            ResultSet queryResults = s.executeQuery();

            // boolean to check whether query results has any rows
            boolean hasRows = false;
            // loop through results and parse the information to be saved
            while (queryResults.next()) {
                hasRows = true;
                int vin = queryResults.getInt(1);
                int year = queryResults.getInt(2);
                String make = queryResults.getString(3);
                String model = queryResults.getString(4);
                String type = queryResults.getString(5);
                String color = queryResults.getString(6);
                double mileage = queryResults.getDouble(7);
                double price = queryResults.getDouble(8);

                // create a new instance of the Vehicle model and add to the array to be returned
                Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, mileage, price, false);
                results.add(vehicle);
            }

            // add a logging message to communicate with user
            if (hasRows) {
                logger.info("✅ Successfully retrieved vehicles within the year range: {} and {} ✅", (int) minYear, (int) maxYear);
            } else {
                logger.warn("❌ No vehicles found within the year range: {} and {} ❌", (int) minYear, (int) maxYear);
            }


        } catch (SQLException e) {
            logger.error("Could not query vehicles by year");
        }


        return results;
    }

    @Bean
    public List<Vehicle> getByColor(String userChosenColor) {
        // we need a place to hold the results
        ArrayList<Vehicle> results = new ArrayList<>();

        // we need to ask for the data
        String query = """
                select v.VIN, v.`year`, v.make, v.model, v.`type`, v.color, v.mileage, v.price
                from car_dealership.vehicles v
                inner join car_dealership.inventory i on i.VIN = v.VIN
                where v.color = ? and i.is_Sold = false
                """;

        // we need to make a connection to the database, send a prepared statement of the query, and execute it
        try (Connection c = dataSource.getConnection();
             PreparedStatement s = c.prepareStatement(query)) {

            // protect from sql injection
            s.setString(1, userChosenColor);

            ResultSet queryResults = s.executeQuery();

            // boolean to check whether query results has any rows
            boolean hasRows = false;
            // loop through results and parse the information to be saved
            while (queryResults.next()) {
                hasRows = true;
                int vin = queryResults.getInt(1);
                int year = queryResults.getInt(2);
                String make = queryResults.getString(3);
                String model = queryResults.getString(4);
                String type = queryResults.getString(5);
                String color = queryResults.getString(6);
                double mileage = queryResults.getDouble(7);
                double price = queryResults.getDouble(8);

                // create a new instance of the Vehicle model and add to the array to be returned
                Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, mileage, price, false);
                results.add(vehicle);
            }

            // add a logging message to communicate with user
            if (hasRows) {
                logger.info("✅ Successfully retrieved vehicles with the color: {} ✅", userChosenColor);
            } else {
                logger.warn("❌ No vehicles found with the color: {} ❌", userChosenColor);
            }


        } catch (SQLException e) {
            logger.error("Could not query vehicles by color");
        }


        return results;
    }

    @Bean
    public List<Vehicle> getByMileage(double minMileage, double maxMileage) {
        // we need a place to hold the results
        ArrayList<Vehicle> results = new ArrayList<>();

        // we need to ask for the data
        String query = """
                select v.VIN, v.`year`, v.make, v.model, v.`type`, v.color, v.mileage, v.price
                from car_dealership.vehicles v
                inner join car_dealership.inventory i on i.VIN = v.VIN
                where v.mileage
                between ? and ?
                and i.is_Sold = false;
                """;

        // we need to make a connection to the database, send a prepared statement of the query, and execute it
        try (Connection c = dataSource.getConnection();
             PreparedStatement s = c.prepareStatement(query)) {

            // protect from sql injection
            s.setDouble(1, minMileage);
            s.setDouble(2, maxMileage);

            ResultSet queryResults = s.executeQuery();


            // boolean to check whether query results has any rows
            boolean hasRows = false;
            // loop through results and parse the information to be saved
            while (queryResults.next()) {
                hasRows = true;
                int vin = queryResults.getInt(1);
                int year = queryResults.getInt(2);
                String make = queryResults.getString(3);
                String model = queryResults.getString(4);
                String type = queryResults.getString(5);
                String color = queryResults.getString(6);
                double mileage = queryResults.getDouble(7);
                double price = queryResults.getDouble(8);

                // create a new instance of the Vehicle model and add to the array to be returned
                Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, mileage, price, false);
                results.add(vehicle);
            }

            // add a logging message to communicate with user
            if (hasRows) {
                logger.info("✅ Successfully retrieved vehicles within the mileage range: {} and {} ✅", (int) minMileage, (int) maxMileage);
            } else {
                logger.warn("❌ No vehicles found within the mileage range: {} and {} ❌", (int) minMileage, (int) maxMileage);
            }


        } catch (SQLException e) {
            logger.error("Could not query vehicles by mileage");
        }


        return results;
    }

    @Bean
    public List<Vehicle> getByVehicleType(String userChosenVehicleType) {
        // we need a place to hold the results
        ArrayList<Vehicle> results = new ArrayList<>();

        // we need to ask for the data
        String query = """
                select v.VIN, v.`year`, v.make, v.model, v.`type`, v.color, v.mileage, v.price
                from car_dealership.vehicles v
                inner join car_dealership.inventory i on i.VIN = v.VIN
                where v.`type`  = ?
                and i.is_Sold = false
                """;

        // we need to make a connection to the database, send a prepared statement of the query, and execute it
        try (Connection c = dataSource.getConnection();
             PreparedStatement s = c.prepareStatement(query)) {

            // protect from sql injection
            s.setString(1, userChosenVehicleType);

            ResultSet queryResults = s.executeQuery();

            // boolean to check whether query results has any rows
            boolean hasRows = false;
            // loop through results and parse the information to be saved
            while (queryResults.next()) {
                hasRows = true;
                int vin = queryResults.getInt(1);
                int year = queryResults.getInt(2);
                String make = queryResults.getString(3);
                String model = queryResults.getString(4);
                String type = queryResults.getString(5);
                String color = queryResults.getString(6);
                double mileage = queryResults.getDouble(7);
                double price = queryResults.getDouble(8);

                // create a new instance of the Vehicle model and add to the array to be returned
                Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, mileage, price, false);
                results.add(vehicle);
            }


            // add a logging message to communicate with user
            if (hasRows) {
                logger.info("✅ Successfully retrieved vehicles with the type: {} ✅", userChosenVehicleType);
            } else {
                logger.warn("❌ No vehicles found with the type: {} ❌", userChosenVehicleType);
            }

        } catch (SQLException e) {
            logger.error("Could not query vehicles by type");
        }


        return results;
    }

    @Bean
    public List<Vehicle> getAllVehicles() {
        // we need a place to hold the results
        ArrayList<Vehicle> results = new ArrayList<>();

        // we need to ask for the data
        String query = """
                select v.VIN, v.`year`, v.make, v.model, v.`type`, v.color, v.mileage, v.price
                from car_dealership.vehicles v
                inner join car_dealership.inventory i on i.VIN = v.VIN
                where i.is_Sold = false
                """;

        // we need to make a connection to the database, send a prepared statement of the query, and execute it
        try (Connection c = dataSource.getConnection();
             PreparedStatement s = c.prepareStatement(query);
             ResultSet queryResults = s.executeQuery()
        ) {

            // boolean to check whether query results has any rows
            boolean hasRows = false;
            // loop through results and parse the information to be saved
            while (queryResults.next()) {
                hasRows = true;
                int vin = queryResults.getInt(1);
                int year = queryResults.getInt(2);
                String make = queryResults.getString(3);
                String model = queryResults.getString(4);
                String type = queryResults.getString(5);
                String color = queryResults.getString(6);
                double mileage = queryResults.getDouble(7);
                double price = queryResults.getDouble(8);

                // create a new instance of the Vehicle model and add to the array to be returned
                Vehicle vehicle = new Vehicle(vin, year, make, model, type, color, mileage, price, false);
                results.add(vehicle);
            }

            // add a logging message to communicate with user
            if (hasRows) {
                logger.info("✅ Successfully retrieved vehicles ✅");
            } else {
                logger.warn("❌ No vehicles found ❌");
            }

        } catch (SQLException e) {
            logger.error("Could not query vehicles");
        }


        return results;
    }

    // insert data method
    @Bean
    public Vehicle addVehicle(Vehicle vehicle) {
        // we need to add the given vehicle to the database using an INSERT statement
        String insertIntoVehiclesQuery = """
                insert into car_dealership.vehicles (VIN, `year`, make, model, `type`, color, mileage, price, has_Contract)
                values (?, ?, ?, ?, ?, ?, ?, ?, false)
                """;

        try (Connection c = dataSource.getConnection()) {
            // this try will handle the vehicle insert query
            try (PreparedStatement s = c.prepareStatement(insertIntoVehiclesQuery)) {

                s.setInt(1, vehicle.getVin());
                s.setInt(2, vehicle.getYear());
                s.setString(3, vehicle.getMake());
                s.setString(4, vehicle.getModel());
                s.setString(5, vehicle.getType());
                s.setString(6, vehicle.getColor());
                s.setInt(7, (int) vehicle.getMileage());
                s.setBigDecimal(8, BigDecimal.valueOf(vehicle.getPrice()));

                int rowsAffectedByFirstQuery = s.executeUpdate();

                // add a logging message to communicate with user
                if (rowsAffectedByFirstQuery > 0) {
                    logger.info("✅ Successfully added a new vehicle: {} ✅", vehicle.toFormattedRow());
                }

            } catch (SQLException e) {
                logger.error("❌ Could not add new vehicle to vehicle inventory ❌");
            }

            String insertIntoInventoryQuery = """
                    insert into car_dealership.inventory (VIN, dealershipID, is_Sold)
                    values (?, ?, ?)
                    """;

            try (PreparedStatement ps = c.prepareStatement(insertIntoInventoryQuery)) {

                ps.setInt(1, vehicle.getVin());
                ps.setInt(2, 1);
                ps.setBoolean(3, false);


                int rowsAffectedBySecondQuery = ps.executeUpdate();

                if (rowsAffectedBySecondQuery > 0) {
                    logger.info("✅ Successfully added a new vehicle to the inventory ✅");
                }


            } catch (SQLException ex) {
                logger.error("❌ Could not add new vehicle to inventory ❌");
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return vehicle;
    }

    // remove data method
    @Bean
    public Vehicle removeVehicle(int vin) {
        // we need to take the given vin and match to a vehicle within the database to remove
        // refactor code to set a helper method to delete from inventory, and vehicles table
        String deleteFromInventoryQuery = """
                    delete from car_dealership.inventory i
                    where i.vin = ?
                """;

        try (Connection c = dataSource.getConnection();
             PreparedStatement s = c.prepareStatement(deleteFromInventoryQuery)) {
            s.setInt(1, vin);

            int rowsAffected = s.executeUpdate();


            // add a logging message to communicate with user
            if (rowsAffected > 0) {
                logger.info("✅ Successfully removed a vehicle from dealership vehicles ✅");
            }

        } catch (SQLException e) {
            logger.error("❌ Could not remove vehicle from dealership vehicles ❌");
            System.out.println(e.getMessage());
        }
        return null;
    }

    // update data method
    @Bean
    public Vehicle updateVehicle(Vehicle vehicle) {

        String updateVehicleInVehiclesQuery = """
                update car_dealership.vehicles v
                set v.has_Contract = true
                where v.VIN = ?;
                """;

        String updateVehicleInInventoryQuery = """
                update car_dealership.inventory i
                set i.is_Sold = true
                where i.VIN = ?
                """;

        try (Connection c = dataSource.getConnection()) {
            // prepared statement for first query
            PreparedStatement s = c.prepareStatement(updateVehicleInVehiclesQuery);

            s.setInt(1, vehicle.getVin());
            int rowsAffectedByFirstQuery = s.executeUpdate();

            if (rowsAffectedByFirstQuery > 0) {
                logger.info("✅ Successfully updated vehicle: {} ✅", vehicle.toFormattedRow());
            }

            // prepared statement for second query
            PreparedStatement ps = c.prepareStatement(updateVehicleInInventoryQuery);

            ps.setInt(1, vehicle.getVin());

            int rowsAffectedBySecondQuery = ps.executeUpdate();

            if (rowsAffectedBySecondQuery > 0) {
                logger.info("✅ Successfully updated vehicle in inventory ✅");
            }

        } catch (SQLException e) {
            logger.error("❌ Could not update vehicle: {} ❌", vehicle.toFormattedRow());
        }

        return vehicle;
    }

}

