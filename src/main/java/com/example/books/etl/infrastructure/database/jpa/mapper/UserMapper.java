package com.example.books.etl.infrastructure.database.jpa.mapper;

import com.example.books.etl.domain.user.AdminUser;
import com.example.books.etl.domain.user.User;
import com.example.books.etl.infrastructure.database.jpa.entity.Authority;
import java.util.*;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

/**
 * Mapper for the entity {@link com.example.books.etl.infrastructure.database.jpa.entity.User} and its DTO called {@link User}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class UserMapper {

    public List<User> usersToUserDTOs(List<com.example.books.etl.infrastructure.database.jpa.entity.User> users) {
        return users.stream().filter(Objects::nonNull).map(this::userToUserDTO).toList();
    }

    public User userToUserDTO(com.example.books.etl.infrastructure.database.jpa.entity.User user) {
        return new User(user);
    }

    public List<AdminUser> usersToAdminUserDTOs(List<com.example.books.etl.infrastructure.database.jpa.entity.User> users) {
        return users.stream().filter(Objects::nonNull).map(this::userToAdminUserDTO).toList();
    }

    public AdminUser userToAdminUserDTO(com.example.books.etl.infrastructure.database.jpa.entity.User user) {
        return new AdminUser(user);
    }

    public List<com.example.books.etl.infrastructure.database.jpa.entity.User> userDTOsToUsers(List<AdminUser> userDTOs) {
        return userDTOs.stream().filter(Objects::nonNull).map(this::userDTOToUser).toList();
    }

    public com.example.books.etl.infrastructure.database.jpa.entity.User userDTOToUser(AdminUser userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            com.example.books.etl.infrastructure.database.jpa.entity.User user =
                new com.example.books.etl.infrastructure.database.jpa.entity.User();
            user.setId(userDTO.getId());
            user.setLogin(userDTO.getLogin());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setImageUrl(userDTO.getImageUrl());
            user.setCreatedBy(userDTO.getCreatedBy());
            user.setCreatedDate(userDTO.getCreatedDate());
            user.setLastModifiedBy(userDTO.getLastModifiedBy());
            user.setLastModifiedDate(userDTO.getLastModifiedDate());
            user.setActivated(userDTO.isActivated());
            user.setLangKey(userDTO.getLangKey());
            Set<Authority> authorities = this.authoritiesFromStrings(userDTO.getAuthorities());
            user.setAuthorities(authorities);
            return user;
        }
    }

    private Set<Authority> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();

        if (authoritiesAsString != null) {
            authorities = authoritiesAsString
                .stream()
                .map(string -> {
                    Authority auth = new Authority();
                    auth.setName(string);
                    return auth;
                })
                .collect(Collectors.toSet());
        }

        return authorities;
    }

    public com.example.books.etl.infrastructure.database.jpa.entity.User userFromId(Long id) {
        if (id == null) {
            return null;
        }
        com.example.books.etl.infrastructure.database.jpa.entity.User user =
            new com.example.books.etl.infrastructure.database.jpa.entity.User();
        user.setId(id);
        return user;
    }

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public User toDtoId(com.example.books.etl.infrastructure.database.jpa.entity.User user) {
        if (user == null) {
            return null;
        }
        User userDto = new User();
        userDto.setId(user.getId());
        return userDto;
    }

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public Set<User> toDtoIdSet(Set<com.example.books.etl.infrastructure.database.jpa.entity.User> users) {
        if (users == null) {
            return Collections.emptySet();
        }

        Set<User> userSet = new HashSet<>();
        for (com.example.books.etl.infrastructure.database.jpa.entity.User userEntity : users) {
            userSet.add(this.toDtoId(userEntity));
        }

        return userSet;
    }

    @Named("login")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    public User toDtoLogin(com.example.books.etl.infrastructure.database.jpa.entity.User user) {
        if (user == null) {
            return null;
        }
        User userDto = new User();
        userDto.setId(user.getId());
        userDto.setLogin(user.getLogin());
        return userDto;
    }

    @Named("loginSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    public Set<User> toDtoLoginSet(Set<com.example.books.etl.infrastructure.database.jpa.entity.User> users) {
        if (users == null) {
            return Collections.emptySet();
        }

        Set<User> userSet = new HashSet<>();
        for (com.example.books.etl.infrastructure.database.jpa.entity.User userEntity : users) {
            userSet.add(this.toDtoLogin(userEntity));
        }

        return userSet;
    }
}
