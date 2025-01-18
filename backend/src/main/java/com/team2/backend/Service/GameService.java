package com.team2.backend.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team2.backend.DTO.Game.NewFavoriteGameDTO;
import com.team2.backend.Exceptions.InvalidFavoriteGameException;
import com.team2.backend.Exceptions.UserNotFoundException;
import com.team2.backend.Models.Game;
import com.team2.backend.Models.User;
import com.team2.backend.Repository.GameRepository;
import com.team2.backend.Repository.UserRepository;

@Service
public class GameService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    public List<Game> getFavoriteGames(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return gameRepository.findByUser(user);
    }

    public Boolean isFavoritedGame(String username, Integer appid) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return gameRepository.existsByUserAndAppId(user, appid);
    }

    @Transactional
    public void addFavoriteGame(String username, NewFavoriteGameDTO newFavoriteGame) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (gameRepository.findByUserAndAppId(user, newFavoriteGame.getAppId()).size() > 0) {
            throw new InvalidFavoriteGameException("Game is already in the user's favorite list.");
        }

        Game newGame = new Game(newFavoriteGame, user);

        gameRepository.save(newGame);
    }

    @Transactional
    public void deleteFavoriteGame(String username, Integer appid) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (gameRepository.findByUserAndAppId(user, appid).size() == 0) {
            throw new InvalidFavoriteGameException("Game is not in the user's favorite list.");
        }

        gameRepository.deleteByUserAndAppId(user, appid);
    }


}
