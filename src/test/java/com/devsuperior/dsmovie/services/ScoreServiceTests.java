package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;

	@Mock
	private UserService userService;

	@Mock
	private MovieRepository movieRepository;

	@Mock
	private ScoreRepository scoreRepository;

	private Long nonExistingId;
	private UserEntity userEntity;
	private MovieEntity movieEntity;
	private ScoreDTO scoreDTO;
	private ScoreEntity scoreEntity;

	@BeforeEach
	void setUp() throws Exception {
		nonExistingId = 2L;
		userEntity = UserFactory.createUserEntity();
		movieEntity = MovieFactory.createMovieEntity();
		scoreDTO = ScoreFactory.createScoreDTO();
		scoreEntity = ScoreFactory.createScoreEntity();

		Mockito.when(userService.authenticated()).thenReturn(userEntity);
		Mockito.when(movieRepository.findById(scoreDTO.getMovieId())).thenReturn(Optional.of(movieEntity));
		Mockito.when(movieRepository.findById(nonExistingId)).thenReturn(Optional.empty());
		Mockito.when(scoreRepository.saveAndFlush(ArgumentMatchers.any())).thenReturn(scoreEntity);
		Mockito.when(movieRepository.save(ArgumentMatchers.any())).thenReturn(movieEntity);
	}
	
	@Test
	public void saveScoreShouldReturnMovieDTO() {
		MovieDTO result = service.saveScore(scoreDTO);

		Assertions.assertNotNull(result);
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
		movieEntity.setId(nonExistingId);
		scoreEntity.setMovie(movieEntity);
		scoreDTO = new ScoreDTO(scoreEntity);

		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.saveScore(scoreDTO);
		});
	}
}
