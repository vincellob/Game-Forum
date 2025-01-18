package com.team2.backend.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team2.backend.Models.Game;
import com.team2.backend.Models.User;

public interface GameRepository extends JpaRepository<Game, Long> {
  List<Game> findByUser(User user);

  List<Game> findByAppId(Integer appid);

  List<Game> findByUserAndAppId(User user, Integer appid);

  void deleteByUserAndAppId(User user, Integer appid);

  Optional<Game> findByAppId(Long appid);

  Boolean existsByUserAndAppId(User user, Integer appid);
  
}
