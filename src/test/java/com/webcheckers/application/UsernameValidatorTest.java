package com.webcheckers.application;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The unit test suite for the {@link UsernameValidator} component.
 *
 * @author Ben
 */
@Tag("Application-Tier")
class UsernameValidatorTest {
    /**
     * A valid username. It begins with a capital letter, is >=12 characters long, does not end with a space, and
     * contains only alphanumeric characters and spaces.
     */
    private static final String VALID_NAME_1 = "This 1st name is Valid";
    /**
     * Another valid username. It begins with a capital letter, is >=12 characters long, does not end with a space, and
     * contains only alphanumeric characters and spaces.
     */
    private static final String VALID_NAME_2 = "This 2nd name is also Valid2";
    /**
     * Another valid username. It begins with a capital letter, is >=12 characters long, does not end with a space, and
     * contains only alphanumeric characters and spaces.
     */
    private static final String VALID_NAME_3 = "This 3rd name is valid as WelL";

    /**
     * A username that is invalid because it begins with a lowercase letter, not a capital letter
     */
    private static final String BEGINS_WITH_LOWERCASE = "this 3rd name is NOT valid";
    /**
     * A username that is invalid because it begins with a digit, not a capital letter
     */
    private static final String BEGINS_WITH_DIGIT = "4th name is not valid Either";
    /**
     * A username that is invalid because it begins with a space, not a capital letter
     */
    private static final String BEGINS_WITH_SPACE = " This 5th name is not Valid";
    /**
     * A username that is invalid because it begins with a special character, not a capital letter
     */
    private static final String BEGINS_WITH_SPECIAL_CHARACTER = "*This 6th name is not Valid";

    /**
     * A username that is invalid because it is too short (<12 characters).
     */
    private static final String TOO_SHORT = "T00 Short";

    /**
     * A username that is invalid because it ends with a space.
     */
    private static final String ENDS_WITH_SPACE = "This 2nd name is not valid ";

    /**
     * A username that is invalid because it is completely blank (all spaces) and thus does not begin with a capital
     * letter. It still meets the minimum length requirement, however.
     */
    private static final String BLANK = "               ";

    /**
     * A username that is invalid because it contains a special character.
     */
    private static final String CONTAINS_SPECIAL_CHARACTER = "Invalid_name_with_underscores";

    /**
     * Another username that is invalid because it contains a special character. This character is not found on a
     * keyboard, so it is likely it wouldn't be included in a block list of special characters (if one existed).
     */
    private static final String CONTAINS_SPECIAL_CHARACTER_NOT_ON_KEYBOARD = "Thi§ name i§ invalid too";

    /**
     * A username that is the empty string. It is invalid because it does not meet the minimum length requirement and
     * because it does not begin with a capital letter.
     */
    private static final String EMPTY_STRING = "";

    /**
     * A username that is the null string.
     */
    private static final String NULL_STRING = null;

    /**
     * A username that is eleven characters long (one character below the length limit).
     */
    private static final String ELEVEN_CHARACTERS = "A2345678901";

    /**
     * A username that is twelve characters long (exactly long enough to meet the minimum length).
     */
    private static final String TWELVE_CHARACTERS = "A23456789012";

    /**
     * A username that is thirteen characters long (one character longer than the minimum length).
     */
    private static final String THIRTEEN_CHARACTERS = "A234567890123";


    /**
     * Test that several valid usernames containing spaces and digits (but beginning with capital letters) are
     * considered valid.
     */
    @Test
    void testValidUsernames() {
        // Test that some valid usernames are considered valid
        assertTrue(UsernameValidator.usernameValid(VALID_NAME_1),
                "Valid username containing digits and spaces was rejected");
        assertTrue(UsernameValidator.usernameValid(VALID_NAME_2),
                "Valid username ending in digit was rejected");
        assertTrue(UsernameValidator.usernameValid(VALID_NAME_3),
                "Valid username ending in capital letter was rejected");
    }

    /**
     * Test the requirement that usernames must begin with a capital letter.
     */
    @Test
    void testBeginsWithCapitalLetter() {
        // Test that some invalid usernames that do not start with a capital letter are rejected
        assertFalse(UsernameValidator.usernameValid(BEGINS_WITH_LOWERCASE),
                "Invalid username beginning with lowercase letter was accepted");
        assertFalse(UsernameValidator.usernameValid(BEGINS_WITH_DIGIT),
                "Invalid username beginning with digit was accepted");
        assertFalse(UsernameValidator.usernameValid(BEGINS_WITH_SPACE),
                "Invalid username beginning with space was accepted");
        assertFalse(UsernameValidator.usernameValid(BEGINS_WITH_SPECIAL_CHARACTER),
                "Invalid username beginning with special character was accepted");
    }

    /**
     * Test the minimum length requirement (usernames must be at least 12 characters in length).
     */
    @Test
    void testMinimumLengthRequirement() {
        // Test the minimum length requirement: any string less than twelve characters in length should be rejected
        assertFalse(UsernameValidator.usernameValid(ELEVEN_CHARACTERS),
                "Eleven-character-long username (too short) was accepted");
        assertTrue(UsernameValidator.usernameValid(TWELVE_CHARACTERS),
                "Twelve-character-long username (valid) was rejected");
        assertTrue(UsernameValidator.usernameValid(THIRTEEN_CHARACTERS),
                "Thirteen-character-long username (valid) was rejected");

        // Test that a username that is much too short is not accepted
        assertFalse(UsernameValidator.usernameValid(TOO_SHORT),
                "Invalid username that is too short was accepted");

        // Test that the empty string is not considered a valid username (it fails to meet the length requirement and
        // does not begin with a capital letter)
        assertFalse(UsernameValidator.usernameValid(EMPTY_STRING),
                "Empty string was accepted as valid username");
    }

    /**
     * Test the requirement that usernames must contain only alphanumeric characters and spaces (no special characters)
     */
    @Test
    void testNoSpecialCharactersRequirement() {
        // Test that some invalid usernames that contain special characters are rejected
        assertFalse(UsernameValidator.usernameValid(CONTAINS_SPECIAL_CHARACTER),
                "Invalid username containing special character was accepted");
        assertFalse(UsernameValidator.usernameValid(CONTAINS_SPECIAL_CHARACTER_NOT_ON_KEYBOARD),
                "Invalid username containing special character was accepted");
    }

    /**
     * Ensure that the usernameValid function rejects a null userName parameter with a NullPointerException
     */
    @Test
    void testParameterNullityCheck() {
        // Test that the usernameValid method rejects a null userName parameter with a NullPointerException
        assertThrows(NullPointerException.class, () -> {
            UsernameValidator.usernameValid(NULL_STRING);
        }, "usernameValid method did not throw NullPointerException on null userName parameter");
    }

    /**
     * Test the requirements concerning spaces (usernames must not begin with spaces, and usernames must not consist
     * entirely of spaces).
     */
    @Test
    void testSpaceRequirements() {
        // Test that a username that is invalid because it ends in a space is rejected
        assertFalse(UsernameValidator.usernameValid(ENDS_WITH_SPACE),
                "Invalid username ending with a space was accepted");

        // Test that a username that contains only spaces but meets the minimum length requirement is still rejected
        assertFalse(UsernameValidator.usernameValid(BLANK),
                "Invalid username containing only spaces was accepted");
    }
}
