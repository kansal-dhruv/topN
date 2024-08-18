package com.dk.topn.scores.repo;

import com.dk.topn.scores.entity.TopScores;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopScoreRepo extends JpaRepository<TopScores, Long> {

    @Query("delete from TopScores where id not in (select id from TopScores order by score desc limit :limit)")
    @Modifying
    void deleteUnderKValues(int limit);

    List<TopScores> findAllByOrderByScoreDesc(Pageable pageable);
}
