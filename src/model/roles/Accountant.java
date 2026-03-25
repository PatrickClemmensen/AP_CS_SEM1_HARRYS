package model.roles;

/**
 * Represents the accountant role at Harry's Salon.
 * <P>
 *     The accountant can look up and sort appointments by date,
 *     and has access to financial information after entering the salon password
 * </P>
 */
public class Accountant extends User {

    /**
     * Creates a new accountant with the given name.
     *
     * @param name the name of the accountant
     */
    public Accountant(String name) {
        super(name);
    }
}
