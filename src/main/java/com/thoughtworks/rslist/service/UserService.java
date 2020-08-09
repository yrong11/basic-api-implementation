package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.exception.UserIndexNotValidException;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {


    final UserRepository userRepository;
    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

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

    public List<User> getUsers(Integer page, Integer size) {
        List<UserDto> userDtos;
        if (page != null && size != null && page > 0 && size > 0){
            Pageable pageable = PageRequest.of(page - 1, size);
            userDtos = userRepository.findAll(pageable).getContent();
        }else
            userDtos = userRepository.findAll();

        return userDtos.stream().map(
                item -> User.builder().age(item.getAge()).email(item.getEmail()).gender(item.getGender())
                        .name(item.getName()).phone(item.getPhone()).voteNum(item.getVoteNum()).build())
                .collect(Collectors.toList());

    }
}
