package com.praveen.sqs.service;

import com.praveen.sqs.dao.UserRepository;
import com.praveen.sqs.mapper.UserMapper;
import com.praveen.sqs.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  @Transactional
  public Long saveUser(User user, String uuid) {

    final var userEntity = UserMapper.toEntity(user, uuid);
    return userRepository.saveAndFlush(userEntity).getId();
  }
}
