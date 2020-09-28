package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.McGymmy;
import seedu.address.model.ReadOnlyMcGymmy;

/**
 * Represents a storage for {@link McGymmy}.
 */
public interface AddressBookStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getAddressBookFilePath();

    /**
     * Returns McGymmy data as a {@link ReadOnlyMcGymmy}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyMcGymmy> readAddressBook() throws DataConversionException, IOException;

    /**
     * @see #getAddressBookFilePath()
     */
    Optional<ReadOnlyMcGymmy> readAddressBook(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyMcGymmy} to the storage.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveAddressBook(ReadOnlyMcGymmy addressBook) throws IOException;

    /**
     * @see #saveAddressBook(ReadOnlyMcGymmy)
     */
    void saveAddressBook(ReadOnlyMcGymmy addressBook, Path filePath) throws IOException;

}
