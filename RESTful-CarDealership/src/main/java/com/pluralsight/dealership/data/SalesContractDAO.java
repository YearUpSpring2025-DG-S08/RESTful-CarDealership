package com.pluralsight.dealership.data;

import com.pluralsight.dealership.models.SalesContract;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class SalesContractDAO {
    private static final Logger logger = LogManager.getLogger(SalesContractDAO.class);
    private final BasicDataSource bds;
    private final DealershipDAO dealershipDAO;

    @Autowired
    public SalesContractDAO(BasicDataSource bds, DealershipDAO dealershipDAO) {
        this.bds = bds;
        this.dealershipDAO = dealershipDAO;
    }

    // insert query methods
    public void addSalesContract(SalesContract salesContract) {
        // take the given sales contract and insert the details into the database
        String addNewContract = """
                INSERT into sales_contracts
                (dateOfSale, customerName, vehicleVIN, monthlyPayment, salesTax, recordingFee, processingFee, finance)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection c = bds.getConnection();
             PreparedStatement s = c.prepareStatement(addNewContract)
        ) {

            s.setString(1, salesContract.getDate());
            s.setString(2, salesContract.getCustomerName());
            s.setInt(3, salesContract.getVehicleSold().getVin());
            s.setDouble(4, salesContract.getMonthlyPayment(salesContract.getVehicleSold()));
            s.setDouble(5, salesContract.getSalesTax());
            s.setDouble(6, salesContract.getRecordingFee());
            s.setDouble(7, salesContract.getProcessingFee());
            s.setBoolean(8, salesContract.isFinanced());

            int rowsAffected = s.executeUpdate();

            if (salesContract.getVehicleSold().isHasContract()) {
                logger.error("❌ The selected vehicle already has a contract ❌\n {}", salesContract.getVehicleSold());
            }

            if (rowsAffected > 0) {
                System.out.println(rowsAffected + " row(s) inserted");
                dealershipDAO.updateVehicle(salesContract.getVehicleSold());

            } else {
                logger.warn("❌ No rows were inserted into the table ❌");
            }

        } catch (SQLException e) {
            logger.error("❌ Could not add a sales contract: {} ❌", e.getMessage());
            e.printStackTrace();
        }
    }
}
