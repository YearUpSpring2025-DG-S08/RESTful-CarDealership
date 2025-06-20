package com.pluralsight.dealership.ui;

import com.pluralsight.dealership.data.DealershipDAO;
import com.pluralsight.dealership.data.LeaseContractDAO;
import com.pluralsight.dealership.data.SalesContractDAO;
import com.pluralsight.dealership.models.LeaseContract;
import com.pluralsight.dealership.models.SalesContract;
import com.pluralsight.dealership.models.Vehicle;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;

import static com.pluralsight.dealership.ui.StyledUI.styledHeader;

// the service tag tells Spring that this class handles the business logic
// the methods of this class are the user facing interfaces that they will interact with
// which sends requests to our database server
@Service
public class UserInterface {
    private final Console console = new Console();
    private final DealershipDAO dealershipDAO;
    private final SalesContractDAO salesContractDAO;
    private final LeaseContractDAO leaseContractDAO;

    // constructor method
    public UserInterface(DealershipDAO dealershipDAO, SalesContractDAO salesContractDAO, LeaseContractDAO leaseContractDAO) {
        this.dealershipDAO = dealershipDAO;
        this.salesContractDAO = salesContractDAO;
        this.leaseContractDAO = leaseContractDAO;
        displayMenu();
    }


    // user interface display menu method
    private void displayMenu() {
        // this helper method will display the menu for the User to make a selection
        // for which process they would like to choose
        // will include accepting user input within this method
        while (true) {
//            printDealershipInfo(this.dealership); // change this to DealershipDAO
            styledHeader("Welcome to the Dealership!");

            String[] menuOptions = new String[]{
                    "Search by Price",
                    "Search by Make/Model",
                    "Search by Year",
                    "Search by Color",
                    "Search by Mileage",
                    "Search by Vehicle Type",
                    "Search All Vehicles",
                    "Add Vehicle to Lot",
                    "Remove Vehicle from Lot",
                    "Sell a Vehicle",
                    "Lease a Vehicle",
                    "Exit Program"};

            int userChoice = console.promptForOption(menuOptions);
            switch (userChoice) {
                case 1:
                    processGetByPriceRequest();
                    break;
                case 2:
                    processGetByMakeModelRequest();
                    break;
                case 3:
                    processGetByYearRequest();
                    break;
                case 4:
                    processGetByColorRequest();
                    break;
                case 5:
                    processGetByMileageRequest();
                    break;
                case 6:
                    processGetByVehicleTypeRequest();
                    break;
                case 7:
                    processGetAllVehiclesRequest();
                    break;
                case 8:
                    processAddVehicleRequest();
                    break;
                case 9:
                    processRemoveVehicleRequest();
                    break;
                case 10:
                    processSalesContract();
                    break;
                case 11:
                    processLeaseContract();
                    break;
                case 12:
                    System.out.println("Thank you for coming to the dealership!");
                    return;
                default:
                    System.out.println("Please make a selection from the menu");
            }
        }

    }

    // processing methods for user requests
    private void processGetByPriceRequest() {
        // get min/max from the user
        // add formatted header so that users are aware what they are doing
        // in this instance: Searching for vehicle by price
        System.out.println(StyledUI.styledBoxTitle("Search Vehicles By Price"));
        double minPrice;
        double maxPrice;

        while (true) {
            minPrice = console.promptForDouble("Please enter a minimum price: ");
            maxPrice = console.promptForDouble("Please enter a maximum price: ");

            if (minPrice > maxPrice) {
                System.out.println("Your minimum price cannot be greater than maximum price");
                continue;
            }


            List<Vehicle> results = dealershipDAO.getByPrice(minPrice, maxPrice);
            printVehicleInventory(results);
            break;
        }
    }

    private void processGetByMakeModelRequest() {
        // get make/model from the user
        // add formatted header so that users are aware what they are doing
        // in this instance: Searching for vehicle by Make/Model
        System.out.println(StyledUI.styledBoxTitle("Search Vehicles By Make / Model"));
        String[] options = new String[]{"Search by Make", "Search by Model", "Search Make and Model"};

        int userChoice = console.promptForOption(options);

        switch (userChoice) {
            case 1 -> {
                String searchMake = console.promptForString("Please enter a make: ");

                List<Vehicle> carMakeResults = dealershipDAO.getByMake(searchMake);
                printVehicleInventory(carMakeResults);

            }
            case 2 -> {
                String searchModel = console.promptForString("Please enter a model: ");

                List<Vehicle> carModelResults = dealershipDAO.getByModel(searchModel);
                printVehicleInventory(carModelResults);

            }
            case 3 -> {
                String searchMake = console.promptForString("Please enter the Make of the vehicle to Search ");
                String searchModel = console.promptForString("Please enter the Model of the vehicle to Search ");

                List<Vehicle> searchResults = dealershipDAO.getByMakeModel(searchMake, searchModel);
                printVehicleInventory(searchResults);


            }
            default -> System.out.println("Invalid entry. Please Try Again.");
        }
    }

    private void processGetByYearRequest() {
        System.out.println(StyledUI.styledBoxTitle("Search Vehicles By Year"));
        double minYear = 0;
        double maxYear = 0;
        while (true) {
            try {
                minYear = console.promptForDouble("Please enter a minimum year: ");
                maxYear = console.promptForDouble("Please enter a maximum year: ");

                if (minYear > maxYear) {
                    System.out.println("Minimum year cannot be greater than maximum year. Please try again.");
                    continue;
                }

            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numerical value for year.");
            }

            List<Vehicle> results = dealershipDAO.getByYear(minYear, maxYear);
            printVehicleInventory(results);
            break;

        }
    }

    private void processGetByColorRequest() {
        System.out.println(StyledUI.styledBoxTitle("Search Vehicles By Color"));
        String userChosenColor = console.promptForString("Please enter a color: ", false);

        List<Vehicle> results = dealershipDAO.getByColor(userChosenColor);
        printVehicleInventory(results);
    }

    private void processGetByMileageRequest() {
        System.out.println(StyledUI.styledBoxTitle("Search Vehicles By Mileage"));
        double minMileage = 0;
        double maxMileage = 0;

        while (true) {
            try {
                minMileage = console.promptForDouble("Please enter a minimum mileage: ");
                maxMileage = console.promptForDouble("Please enter a maximum mileage: ");

                if (minMileage > maxMileage) {
                    System.out.println("Minimum mileage cannot be greater than maximum mileage. Please try again.");
                    continue;
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a numerical value for mileage.");
            }

            List<Vehicle> results = dealershipDAO.getByMileage(minMileage, maxMileage);
            printVehicleInventory(results);
            break;
        }
    }

    private void processGetByVehicleTypeRequest() {
        System.out.println(StyledUI.styledBoxTitle("Search Vehicles By Vehicle Type"));
        String userChosenVehicleType = console.promptForString("Please enter a vehicle type: ", false);

        List<Vehicle> results = dealershipDAO.getByVehicleType(userChosenVehicleType);
        printVehicleInventory(results);
    }

    private void processGetAllVehiclesRequest() {
        System.out.println(StyledUI.styledBoxTitle("Search All Vehicles"));

        List<Vehicle> results = dealershipDAO.getAllVehicles();
        printVehicleInventory(results);
    }

    private void processAddVehicleRequest() {
        System.out.println(StyledUI.styledBoxTitle("Add a Vehicle to Dealership lot"));
        System.out.println("Please enter the following details to add a vehicle to the lot: ");
        int vin = generateRandomVin();
        int year = console.promptForInt("Vehicle Year: ");
        String make = console.promptForString("Vehicle Make: ");
        String model = console.promptForString("Vehicle Model: ");
        String type = console.promptForString("Vehicle Type: ");
        String color = console.promptForString("Vehicle Color: ");
        double mileage = console.promptForDouble("Vehicle Mileage: ");
        double price = console.promptForDouble("Vehicle Price: ");

        try {
            Vehicle newVehicle = new Vehicle(vin, year, make, model, type, color, mileage, price, false);
            dealershipDAO.addVehicle(newVehicle);

        } catch (Exception e) {
            System.out.println("❌ There was an error trying to add this vehicle to the lot ❌");
        }
    }

    private void processRemoveVehicleRequest() {
        System.out.println(StyledUI.styledBoxTitle("Remove a Vehicle From the Dealership lot"));
        List<Vehicle> results = dealershipDAO.getAllVehicles();
        printVehicleInventory(results);

        int vin = console.promptForInt("Please enter the vin number of the vehicle you want to remove: ");

        System.out.println("You have chosen: \n"
                + StyledUI.FormattedTextHeader() + "\n"
                + dealershipDAO.getByVin(vin).toFormattedRow() + "\n"
                + "\nAre you sure you want to remove this vehicle? \n");

        String[] options = new String[]{"Yes", "No"};
        int confirmDelete = console.promptForOption(options);

        if (confirmDelete == 1) {
            dealershipDAO.removeVehicle(vin);
        } else {
            System.out.println("Returning to menu...");
        }

    }

    private void processSalesContract() {
        System.out.println(StyledUI.styledBoxTitle("Purchase a Sales Contract"));
        List<Vehicle> allVehicles = dealershipDAO.getAllVehicles();
        printVehicleInventory(allVehicles);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // create loop for potential InputErrorMismatch
        int vin;
        while (true) {
            vin = console.promptForInt("Please enter the vin number of the vehicle you want to buy: ");

            if (vin >= 111111111 && vin <= 999999999) {
                break;
            } else {
                System.out.println("Invalid VIN. Please try again.");
            }


            System.out.println("You have chosen: \n"
                    + StyledUI.FormattedTextHeader() + "\n"
                    + dealershipDAO.getByVin(vin).toFormattedRow() + "\n"
                    + "\nAre you sure you want to buy this vehicle? \n");

            String[] options = new String[]{"Yes", "No"};
            int confirmPurchase = console.promptForOption(options);

            if (confirmPurchase == 1) {
                String dateOfPurchase = LocalDateTime.now().format(formatter);
                String customerName = console.promptForString("Please enter your full name: ");
                String customerEmail = console.promptForString("Please enter your email address: ");
                Vehicle purchasedVehicle = dealershipDAO.getByVin(vin);
                boolean userFinanced;
                String userChooseFinance = console.promptForString("Would you like to finance the vehicle? (Y/N) ");
                userFinanced = userChooseFinance.equals("Y");


                SalesContract newContract = new SalesContract(dateOfPurchase, customerName, customerEmail, purchasedVehicle, 0, 0, 0, userFinanced);

                if (newContract != null) {
                    salesContractDAO.addSalesContract(newContract);
                    dealershipDAO.removeVehicle(newContract.getVehicleSold().getVin());
                    break;
                } else {
                    System.out.println("Could not complete a new sales contract");
                }


            } else {
                return;
            }

        }


    }

    private void processLeaseContract() {
        System.out.println(StyledUI.styledBoxTitle("Purchase a Lease Contract"));
        List<Vehicle> allVehicles = dealershipDAO.getAllVehicles();
        printVehicleInventory(allVehicles);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // create loop for potential InputErrorMismatch
        int vin;
        while (true) {
            vin = console.promptForInt("Please enter the vin number of the vehicle you want to buy: ");

            if (vin >= 111111111 && vin <= 999999999) {
                break;
            } else {
                System.out.println("Invalid VIN. Please try again.");
            }


            System.out.println("You have chosen: \n"
                    + StyledUI.FormattedTextHeader() + "\n"
                    + dealershipDAO.getByVin(vin).toFormattedRow() + "\n"
                    + "\nAre you sure you want to buy this vehicle? \n");

            String[] options = new String[]{"Yes", "No"};
            int confirmPurchase = console.promptForOption(options);

            if (confirmPurchase == 1) {
                String dateOfPurchase = LocalDateTime.now().format(formatter);
                String customerName = console.promptForString("Please enter your full name: ");
                String customerEmail = console.promptForString("Please enter your email address: ");
                Vehicle purchasedVehicle = dealershipDAO.getByVin(vin);

                LeaseContract newContract = new LeaseContract(dateOfPurchase, customerName, customerEmail, purchasedVehicle, 0, 0);

                if (newContract != null) {
                    leaseContractDAO.addLeaseContract(newContract);
                    dealershipDAO.removeVehicle(newContract.getVehicleSold().getVin());
                    break;
                } else {
                    System.out.println("Could not complete a new sales contract");
                }


            } else {
                return;
            }

        }
    }

    // helper method
    public void printVehicleInventory(List<Vehicle> vehicles) {
        if (vehicles == null || vehicles.isEmpty()) {
            System.out.println("No vehicles in inventory \n");
            return;
        }

        // Print styled header
        System.out.println(StyledUI.FormattedTextHeader());

        // Print each vehicle using the Vehicle's toFormattedRow() method
        for (Vehicle v : vehicles) {
            System.out.println(v.toFormattedRow());
        }

        System.out.println();
    }

    public int generateRandomVin() {
        Random random = new Random();
        return 100000000 + random.nextInt(900000000);
    }

}