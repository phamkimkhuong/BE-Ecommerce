package iuh.fit.se.dtos;

import iuh.fit.se.entities.User;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link User}
 */
public class UserDTO implements Serializable {
    private int userId;
    @NotEmpty(message = "Full name is required")
    private String fullName;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;
    private boolean gender;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public @NotEmpty(message = "Full name is required") String getFullName() {
        return fullName;
    }

    public void setFullName(@NotEmpty(message = "Full name is required") String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }
}