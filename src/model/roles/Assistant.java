package model.roles;

/**
 * Represents the assistant role at Harry's Salon
 * <P>
 *     The assistant can create and delete appointments
 *     and view upcoming bookings.
 * </P>
 */
public class Assistant extends User {

    /**
     * Creates a new assistant with the given name.
     *
     * @param name the name of the assistant
     */
    public Assistant(String name) {
        super(name);
    }
}
