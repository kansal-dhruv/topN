package com.dk.topn.scores.repo;

import com.dk.topn.scores.entity.IdProjection;
import com.dk.topn.scores.entity.TopScores;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TopScoreRepo extends JpaRepository<TopScores, Long> {

    @Query("select top from TopScores top order by top.score desc limit :limit")
    List<IdProjection> getTopKids(int limit);

    void deleteByIdNotIn(List<Long> ids);

    List<TopScores> findAllByOrderByScoreDesc(Pageable pageable);
}
