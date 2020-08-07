package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Objects;

@Data
@Valid
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @JsonProperty("user_name")
    @JsonAlias("name")
    @NotNull @Size(max = 8)
    private String name;

    @JsonProperty("user_gender")
    @JsonAlias("gender")
    @NotNull
    private String gender;

    @JsonProperty("user_age")
    @JsonAlias("age")
    @NotNull @Min(18) @Max(100)
    private int age;

    @JsonProperty("user_email")
    @JsonAlias("email")
    @Email
    private String email;
    @JsonProperty("user_phone")
    @JsonAlias("phone")
    @Pattern(regexp = "1\\d{10}")
    private String phone;
    @Builder.Default
    private int voteNum = 10;

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

    @JsonProperty
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

    @JsonIgnore
    public int getVoteNum() {
        return voteNum;
    }
}
