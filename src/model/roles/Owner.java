package model.roles;

/**
 * Represents the owner role at Harry's Salon.
 * <P>
 *     The owner has full access to the systes, including
 *     managing appointments, viewing financial information,
 *     and registering closed days.
 * </P>
 */
public class Owner extends User{

    /**
     * Creates a new owner with the given name.
     *
     * @param name the name of the owner
     */
    public Owner(String name) {
        super(name);
    }
}
