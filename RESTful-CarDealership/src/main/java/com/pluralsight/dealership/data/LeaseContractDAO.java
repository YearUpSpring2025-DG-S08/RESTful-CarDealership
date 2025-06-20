package com.pluralsight.dealership.data;

import com.pluralsight.dealership.models.LeaseContract;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class LeaseContractDAO {
    private static final Logger logger = LogManager.getLogger(LeaseContractDAO.class);
    private final BasicDataSource bds;
    private final DealershipDAO dealershipDAO;

    @Autowired
    public LeaseContractDAO(BasicDataSource bds, DealershipDAO dealershipDAO) {
        this.bds = bds;
        this.dealershipDAO = dealershipDAO;
    }

    // insert query methods
    public void addLeaseContract(LeaseContract leaseContract) {
        // take the given sales contract and insert the details into the database
        String addNewContract = """
                insert into car_dealership.lease_contracts
                (dateOfSale, customerName, vehicleVIN, monthlyPayment, endingValue, leaseFee)
                values (?, ?, ?, ?, ?, ?)
                """;

        try (Connection c = bds.getConnection();
             PreparedStatement s = c.prepareStatement(addNewContract)) {

            s.setString(1, leaseContract.getDate());
            s.setString(2, leaseContract.getCustomerName());
            s.setInt(3, leaseContract.getVehicleSold().getVin());
            s.setDouble(4, leaseContract.getMonthlyPayment(leaseContract.getVehicleSold()));
            s.setDouble(5, leaseContract.getEndingValue());
            s.setDouble(6, leaseContract.getLeaseFee());

            int rowsAffected = s.executeUpdate();

            if (leaseContract.getVehicleSold().isHasContract()) {
                logger.error("❌ The selected vehicle already has a contract ❌\n {}", leaseContract.getVehicleSold());
            }

            if (rowsAffected > 0) {
                System.out.println(rowsAffected + " row(s) inserted");
                dealershipDAO.updateVehicle(leaseContract.getVehicleSold());
            } else {
                logger.warn("❌ No rows were inserted into the table ❌");
            }


        } catch (SQLException e) {
            logger.error("❌ Could not add a lease contract: {} ❌", e.getMessage());
            e.printStackTrace();
        }

    }

}
