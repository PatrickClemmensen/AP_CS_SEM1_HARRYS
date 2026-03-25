package model.roles;

/**
 * Abstract base class representing a user of Harry's Salon system.
 * <p>
 *     A user can be an {@link Owner}, {@link Assistant}, {@link Accountant},
 *     each with the own set of permissions and menu access.
 * </p>
 */
public abstract class User {
    private String name;

    /**
     * Creates a new user with the given name.
     *
     * @param name the name of the user
     */
    public User(String name){
        this.name = name;
    }

    /**
     * Returns the name of this user.
     *
     * @return the users name as a string
     */
    public String getName() {
        return name;
    }
}
