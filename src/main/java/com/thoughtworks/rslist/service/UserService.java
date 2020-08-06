package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.exception.UserIndexNotValidException;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void registerUser(User user){
        UserDto userDto = UserDto.builder().name(user.getName()).gender(user.getGender()).age(user.getAge())
                .email(user.getEmail()).phone(user.getPhone()).voteNum(user.getVoteNum()).build();
        userRepository.save(userDto);
    }


    public User getUser(int id) throws UserIndexNotValidException {
        if (!userRepository.findById(id).isPresent())
            throw new UserIndexNotValidException("Invalid user index!");
        UserDto userDto = userRepository.findById(id).get();
        User user = new User(userDto.getName(), userDto.getGender(), userDto.getAge(), userDto.getEmail(), userDto.getPhone());
        return user;
    }

    @Transactional
    public void deleteUser(int id) throws UserIndexNotValidException{
        if (!userRepository.findById(id).isPresent())
            throw new UserIndexNotValidException();
        userRepository.deleteById(id);
    }
}
