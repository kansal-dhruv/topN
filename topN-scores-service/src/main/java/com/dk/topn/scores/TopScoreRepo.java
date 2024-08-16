package com.dk.topn.scores;

import com.dk.topn.scores.entity.TopScores;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TopScoreRepo extends JpaRepository<TopScores, Long> {

    @Query("delete from TopScores where id > (select id from TopScores order by score limit :limit offset :offset)")
    void deleteUnderKValues(int limit, int offset){

    }
}
