package controllers;

import com.pluralsight.dealership.data.DealershipDAO;
import com.pluralsight.dealership.models.Vehicle;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// settings a rest controller so this application can interact to the user
@RestController
public class DealershipController {
    
    private final DealershipDAO dealershipDAO;

    public DealershipController(DealershipDAO dealershipDAO) {
        this.dealershipDAO = dealershipDAO;
    }
    
    // this annotation describes the type of requests that a user can ask the server
    // the path tag describes the URI - identifies the 'route' of where the data is located
    // the method tag describes the process that is happening in the request (GET = select (MySQL) = getting data from the server
    // the path variable tag describes the variable being taken from the path to use as a search criteria
    @RequestMapping(path="/inventory/ByVin/{vin}", method= RequestMethod.GET)
    public Vehicle getVehicleByVin(@PathVariable int vin){
        return dealershipDAO.getByVin(vin);
    }
    
    @RequestMapping(path="/inventory/ByPrice/{minPrice, maxPrice}", method= RequestMethod.GET)
    public List<Vehicle> getVehiclesByPrice(@PathVariable double minPrice, double maxPrice){
        return dealershipDAO.getByPrice(minPrice, maxPrice);
    }
    
    @RequestMapping(path="/inventory/ByMake/{make}", method= RequestMethod.GET)
    public List<Vehicle> getVehiclesByMake(@PathVariable String make){
        return dealershipDAO.getByMake(make);
    }
    
    @RequestMapping(path="/inventory/ByModel{model}", method= RequestMethod.GET)
    public List<Vehicle> getVehiclesByModel(@PathVariable String model){
        return dealershipDAO.getByModel(model);
    }

    @RequestMapping(path="/inventory/ByMakeANDModel{make, model}", method= RequestMethod.GET)
    public List<Vehicle> getVehiclesByMakeModel(@PathVariable String make, String model){
        return dealershipDAO.getByMakeModel(make, model);
    }

    @RequestMapping(path="/inventory/ByYear/{minYear, maxYear}", method= RequestMethod.GET)
    public List<Vehicle> getVehiclesByYear(@PathVariable double minYear, double maxYear){
        return dealershipDAO.getByYear(minYear, maxYear);
    }

    @RequestMapping(path="/inventory/ByColor/{color}", method= RequestMethod.GET)
    public List<Vehicle> getVehiclesByColor(@PathVariable String color){
        return dealershipDAO.getByColor(color);
    }

    @RequestMapping(path="/inventory/ByMileage/{minMileage, maxMileage}", method= RequestMethod.GET)
    public List<Vehicle> getVehiclesByMileage(@PathVariable double minMileage, double maxMileage){
        return dealershipDAO.getByMileage(minMileage, maxMileage);
    }

    @RequestMapping(path="/inventory/ByVehicleType/{vehicleType}", method= RequestMethod.GET)
    public List<Vehicle> getVehiclesByVehicleType(@PathVariable String vehicleType){
        return dealershipDAO.getByVehicleType(vehicleType);
    }

    // the request method function POST = insert (MySQL) = creating a new data point
    // the response status annotation gives the user an HTTP status code to communicate what happened with the request
    // the value holds the status code: 200s = success | 400s = client errors | 500s = server error
    // the request body takes user input from the body of the request when it sends to the server
    @RequestMapping(path="/inventory/AddVehicle/{newVehicle}", method= RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Vehicle addNewVehicle(@RequestBody Vehicle newVehicle){
        return dealershipDAO.addVehicle(newVehicle);
    }

    @RequestMapping(path="/inventory/RemoveVehicle/{vin}", method= RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Vehicle removeVehicle(@RequestBody int vin){
        return dealershipDAO.removeVehicle(vin);
    }

    @RequestMapping(path="/inventory/UpdateVehicle/{updateVehicle}", method= RequestMethod.PUT)
    public Vehicle updateVehicle(@RequestBody Vehicle updateVehicle){
        return dealershipDAO.updateVehicle(updateVehicle);
    }
    
}
