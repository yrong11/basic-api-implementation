package com.thoughtworks.rslist.domain;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Objects;

@Data
@Valid
public class User {
    @NotNull @Size(max = 8)
    private String name;
    @NotNull
    private String gender;
    @NotNull @Min(18) @Max(100)
    private int age;
    @Email
    private String email;
    @Pattern(regexp = "1\\d{10}")
    private String phone;
    private int voteNum = 10;

    public User(){

    }
    public User(String name, String gender, int age, String email, String phone) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return age == user.age &&
                name.equals(user.name) &&
                gender.equals(user.gender) &&
                Objects.equals(email, user.email) &&
                Objects.equals(phone, user.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gender, age, email, phone);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setVoteNum(int voteNum) {
        this.voteNum = voteNum;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public int getVoteNum() {
        return voteNum;
    }
}
