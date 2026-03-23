package ui;

import model.appointments.Appointment;
import model.appointments.AppointmentStatus;
import model.payments.CashPayment;
import model.payments.CreditPayment;
import model.payments.Payment;
import model.products.Category;
import model.products.Product;
import service.FileStorage;
import util.colors.Colors;
import util.exceptions.InvalidInputException;
import util.inputvalidation.InputValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class PaymentMenu extends Menu{

    public PaymentMenu(){

    }

    protected void show(Scanner scanner) {

        // Step 1 — get all past unpaid appointments
        ArrayList<Appointment> unpaidAppointments = new ArrayList<>();
        for (Appointment a : FileStorage.getAllAppointments()) {
            if (a.getDate().isBefore(LocalDate.now())
                    && (a.getStatus() == AppointmentStatus.BOOKED)) {
                unpaidAppointments.add(a);
            }
        }

        if (unpaidAppointments.isEmpty()) {
            System.out.println("No unpaid past appointments found.");
            return;
        }

        // Step 2 — display unpaid appointments
        System.out.println("\nUnpaid appointments:");
        for (int i = 0; i < unpaidAppointments.size(); i++) {
            System.out.println((i + 1) + ". "
                    + unpaidAppointments.get(i).getCustomer().getName()
                    + " | " + unpaidAppointments.get(i).getDate()
                    + " | " + unpaidAppointments.get(i).getTimeslot().getStartTime());
        }

        // Step 3 — select appointment
        Appointment selectedAppointment = null;
        while (true) {
            System.out.print("Select appointment (1-" + unpaidAppointments.size() + "): ");
            try {
                int choice = InputValidator.validateMenuChoice(
                        scanner.nextLine(), 1, unpaidAppointments.size());
                selectedAppointment = unpaidAppointments.get(choice - 1);
                break;
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }


        // Step 5 — select add-ons
        ArrayList<Product> addons = new ArrayList<>();
        for (Product p : Product.values()) {
            if (p.getCategory() == Category.RETAIL) {
                addons.add(p);
            }
        }

        ArrayList<Product> selectedAddons = new ArrayList<>();


        while (true) {
            System.out.println("\nAvailable add-ons:");
            for (int i = 0; i < addons.size(); i++) {
                System.out.println((i + 1) + ". " + addons.get(i)
                        + " — " + addons.get(i).getPrice() + " kr");
            }
            System.out.println("0. No more add-ons");
            System.out.print("Select add-on (0 to finish): ");
            try {
                int choice = InputValidator.validateMenuChoice(
                        scanner.nextLine(), 0, addons.size());
                if (choice == 0) break;
                selectedAddons.add(addons.get(choice - 1));
                System.out.println(Colors.CONFIRMATION
                        + "Added: " + addons.get(choice - 1)
                        + Colors.RESET);
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // Step 6 — calculate total
        for(Product p : selectedAddons){
            selectedAppointment.getPayment().addProduct(p);
        }
        double total = selectedAppointment.getPayment().getTotalAmount();
        for(Product p : selectedAppointment.getPayment().getProducts()){
            total += p.getPrice();
        }
        System.out.println("Total:    " + total+ " kr");

        // Step 7 — select payment type
        System.out.println("\nPayment type:");
        System.out.println("1. Cash");
        System.out.println("2. Credit");

        while (true) {
            System.out.print("Select payment type (1-2): ");
            try {
                Payment payment = null;
                int choice = InputValidator.validateMenuChoice(
                        scanner.nextLine(), 1, 2);
                if (choice == 1) {
                    payment = new CashPayment(total, LocalDate.now());
                } else {
                    payment = new CreditPayment(total, LocalDate.now());
                }
                FileStorage.registerPayment(selectedAppointment, payment);
                System.out.println(Colors.CONFIRMATION
                        + "Payment registered successfully. Total: " + total + " kr"
                        + Colors.RESET);
                break;
            } catch (InvalidInputException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
