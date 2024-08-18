package com.dk.topn.scores.tests.repo;

import com.dk.topn.processor.listener.RmqListeners;
import com.dk.topn.scores.entity.TopScores;
import com.dk.topn.scores.repo.TopScoreRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Pageable;

import java.util.*;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class TopScoreRepoTest {

    @Autowired
    private TopScoreRepo topScoreRepo;

    @MockBean
    RmqListeners rmqListeners;

    @MockBean
    RabbitTemplate rabbitTemplate;

    @MockBean
    ConnectionFactory connectionFactory;

    final Random random = new Random();

    @Test
    public void testInsert(){
        topScoreRepo.saveAll(sampleData(100));
        List<TopScores> fromDB = topScoreRepo.findAll();
        Assertions.assertEquals(fromDB.size(), 100);
        for (TopScores topScores : fromDB) {
            Assertions.assertTrue(topScores.getId() > 0);
            Assertions.assertTrue(Objects.nonNull(topScores.getScore()));
            Assertions.assertTrue(Objects.nonNull(topScores.getPlayerId()));
        }
    }

    @Test
    public void testInsertTop1000(){
        List<TopScores> scores = sampleData(10000);
        List<TopScores> sorted = scores.stream().sorted(Comparator.comparing(TopScores::getScore).reversed()).limit(1000).toList();
        topScoreRepo.saveAll(scores);
        topScoreRepo.deleteUnderKValues(1000);
        List<TopScores> fromDB = topScoreRepo.findAllByOrderByScoreDesc(Pageable.ofSize(1000));
        Assertions.assertEquals(fromDB.size(), 1000);
        for (int i = 0; i < 1000; i++) {
            Assertions.assertEquals(sorted.get(i).getScore(), fromDB.get(i).getScore());
            Assertions.assertEquals(sorted.get(i).getPlayerId(), fromDB.get(i).getPlayerId());
        }
    }


    private List<TopScores> sampleData(int size){
        List<TopScores> topScores = new ArrayList<>();
        for(int i=0; i<size; i++){
            topScores.add(sampleData("player"+String.valueOf(random.nextInt()), random.nextDouble(10.00)));
        }
        return topScores;
    }

    private TopScores sampleData(String playerId, double score) {
        TopScores topScores = new TopScores();
        topScores.setPlayerId(playerId);
        topScores.setScore(score);
        return topScores;
    }
}
