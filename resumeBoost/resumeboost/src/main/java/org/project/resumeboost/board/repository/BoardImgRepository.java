package org.project.resumeboost.board.repository;

import java.util.Optional;

import org.project.resumeboost.board.entity.BoardEntity;
import org.project.resumeboost.board.entity.BoardImgEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardImgRepository extends JpaRepository<BoardImgEntity, Long> {

  Optional<BoardImgEntity> findByBoardEntityId(Long id);

  Optional<BoardImgEntity> findByBoardEntity(BoardEntity boardEntity);

}
