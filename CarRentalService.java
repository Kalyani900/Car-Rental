import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.temporal.ChronoUnit;


interface Vehicle {
    String getMake();
    String getModel();
    String getRegistrationNumber();
    boolean isAvailable();
    void setAvailable(boolean available);
    double getRentalPricePerDay();
    String toString();
}


class Car implements Vehicle {
    private String make;
    private String model;
    private String registrationNumber;
    private boolean isAvailable;
    private double rentalPricePerDay;

    public Car(String make, String model, String registrationNumber, double rentalPricePerDay) {
        this.make = make;
        this.model = model;
        this.registrationNumber = registrationNumber;
        this.isAvailable = true;  
        this.rentalPricePerDay = rentalPricePerDay;
    }

    @Override
    public String getMake() {
        return make;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public String getRegistrationNumber() {
        return registrationNumber;
    }

    @Override
    public boolean isAvailable() {
        return isAvailable;
    }

    @Override
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public double getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    @Override
    public String toString() {
        return make + " " + model + " (Reg: " + registrationNumber + ")";
    }
}


class RentalAgreement {
    private Vehicle vehicle;
    private String customerName;
    private LocalDate rentalStartDate;
    private LocalDate rentalEndDate;

    public RentalAgreement(Vehicle vehicle, String customerName, LocalDate rentalStartDate, LocalDate rentalEndDate) {
        this.vehicle = vehicle;
        this.customerName = customerName;
        this.rentalStartDate = rentalStartDate;
        this.rentalEndDate = rentalEndDate;
        vehicle.setAvailable(false);  
    }

    public double calculateTotalRentalCost() {
        long days = ChronoUnit.DAYS.between(rentalStartDate, rentalEndDate);
        return days * vehicle.getRentalPricePerDay();
    }

    public void returnVehicle() {
        vehicle.setAvailable(true);  
    }

    @Override
    public String toString() {
        return "Rental Agreement:\n" +
                "Customer: " + customerName + "\n" +
                "Vehicle: " + vehicle + "\n" +
                "Rental Start Date: " + rentalStartDate + "\n" +
                "Rental End Date: " + rentalEndDate + "\n" +
                "Total Cost: $" + calculateTotalRentalCost();
    }
}


public class CarRentalService {
    private List<Vehicle> vehicles;
    private List<RentalAgreement> rentalAgreements;

    public CarRentalService() {
        this.vehicles = new ArrayList<>();
        this.rentalAgreements = new ArrayList<>();
    }

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
    }

    public Vehicle findAvailableVehicle(String make, String model) {
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isAvailable() && vehicle.getMake().equalsIgnoreCase(make)
                    && vehicle.getModel().equalsIgnoreCase(model)) {
                return vehicle;
            }
        }
        return null;
    }

    public RentalAgreement rentVehicle(String make, String model, String customerName, LocalDate rentalStartDate, LocalDate rentalEndDate) {
        Vehicle vehicle = findAvailableVehicle(make, model);
        if (vehicle != null) {
            RentalAgreement agreement = new RentalAgreement(vehicle, customerName, rentalStartDate, rentalEndDate);
            rentalAgreements.add(agreement);
            return agreement;
        } else {
            System.out.println("No available vehicles of the specified make and model.");
            return null;
        }
    }

    public void returnVehicle(RentalAgreement agreement) {
        agreement.returnVehicle();
    }

    public void displayAvailableVehicles() {
        System.out.println("Available Vehicles:");
        for (Vehicle vehicle : vehicles) {
            if (vehicle.isAvailable()) {
                System.out.println(vehicle);
            }
        }
    }

    public void displayRentalAgreements() {
        System.out.println("Rental Agreements:");
        for (RentalAgreement agreement : rentalAgreements) {
            System.out.println(agreement);
            System.out.println();
        }
    }

    public static void main(String[] args) {
        CarRentalService carRentalService = new CarRentalService();
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (true) {
            System.out.println("\nCar Rental Service");
            System.out.println("1. Add Vehicle");
            System.out.println("2. Display Available Vehicles");
            System.out.println("3. Rent a Vehicle");
            System.out.println("4. Return a Vehicle");
            System.out.println("5. Display Rental Agreements");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  

            switch (choice) {
                case 1:
                    System.out.print("Enter vehicle make: ");
                    String make = scanner.nextLine();
                    System.out.print("Enter vehicle model: ");
                    String model = scanner.nextLine();
                    System.out.print("Enter registration number: ");
                    String regNumber = scanner.nextLine();
                    System.out.print("Enter rental price per day: ");
                    double pricePerDay = scanner.nextDouble();
                    scanner.nextLine();  

                    carRentalService.addVehicle(new Car(make, model, regNumber, pricePerDay));
                    System.out.println("Vehicle added successfully.");
                    break;

                case 2:
                    carRentalService.displayAvailableVehicles();
                    break;

                case 3:
                    System.out.print("Enter vehicle make: ");
                    make = scanner.nextLine();
                    System.out.print("Enter vehicle model: ");
                    model = scanner.nextLine();
                    System.out.print("Enter customer name: ");
                    String customerName = scanner.nextLine();
                    System.out.print("Enter rental start date (yyyy-MM-dd): ");
                    String startDateStr = scanner.nextLine();
                    System.out.print("Enter rental end date (yyyy-MM-dd): ");
                    String endDateStr = scanner.nextLine();

                    LocalDate startDate = LocalDate.parse(startDateStr, formatter);
                    LocalDate endDate = LocalDate.parse(endDateStr, formatter);

                    RentalAgreement agreement = carRentalService.rentVehicle(make, model, customerName, startDate, endDate);

                    if (agreement != null) {
                        System.out.println("\n" + agreement);
                    }
                    break;

                case 4:
                    System.out.print("Enter customer name for returning vehicle: ");
                    customerName = scanner.nextLine();
                    RentalAgreement returnAgreement = null;
                    for (RentalAgreement ag : carRentalService.rentalAgreements) {
                        if (ag.toString().contains("Customer: " + customerName)) {
                            returnAgreement = ag;
                            break;
                        }
                    }

                    if (returnAgreement != null) {
                        carRentalService.returnVehicle(returnAgreement);
                        System.out.println("Vehicle returned successfully.");
                    } else {
                        System.out.println("No rental agreement found for the customer.");
                    }
                    break;

                case 5:
                    carRentalService.displayRentalAgreements();
                    break;

                case 6:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
