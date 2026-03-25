package model.roles;

/**
 * Represents a customer at Harry's Salon.
 * <P>
 *     A customer is identified by their name and phone number,
 *     and is associated with one or more {@link model.appointments.Appointment}
 *
 * </P>
 */
public class Customer  {

    private String name;
    private String phoneNumber;


    /**
     * Creates a new customer with a name and phone number.
     *
     * @param name          the name of the customer
     * @param phoneNumber   the phone number of the customer
     */
    public Customer(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;

    }

    /**
     * Returns the name of this customer
     *
     * @return the customers name as a string
     */
    public String getName(){
        return name;
    }

    /**
     * Returns the phone number of this customer
     *
     * @return the customers phone number as a string
     */
    public String getPhoneNumber(){
        return phoneNumber;
    }

    /**
     * Returns a string representation of this customer
     *
     * @return the customers name
     */
    @Override
    public String toString() {
        return getName();
    }
}
