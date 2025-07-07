package org.project.resumeboost.board.repository;

import org.project.resumeboost.board.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

        Page<BoardEntity> findByTitleContaining(Pageable pageable, String search);

        Page<BoardEntity> findByContentContaining(Pageable pageable, String search);

        Page<BoardEntity> findByCategoryContaining(Pageable pageable, String search);

        Page<BoardEntity> findByWriterContaining(Pageable pageable, String search);

        // 전체검색
        @Query("SELECT b FROM BoardEntity b WHERE " +
                        "b.title LIKE %:search% OR " +
                        "b.content LIKE %:search% OR " +
                        "b.memberEntity.nickName LIKE %:search%")
        Page<BoardEntity> findByTitleOrContentOrWriterContaining(Pageable pageable, @Param("search") String search);

        // 조회수
        @Modifying // JPQL
        @Query("update BoardEntity b set b.viewCount = b.viewCount + 1 where b.id = :id")
        void BoardViewCount(@Param("id") Long id);

        // 자기소개서 검색
        @Query("SELECT b FROM BoardEntity b WHERE " +
                        "b.title LIKE %:search% AND " +
                        "b.category LIKE %:letter%")
        Page<BoardEntity> findByTitleAndCategoryContaining(Pageable pageable, @Param("search") String search,
                        @Param("letter") String letter);

        @Query("SELECT b FROM BoardEntity b WHERE " +
                        "b.content LIKE %:search% AND " +
                        "b.category LIKE %:letter%")
        Page<BoardEntity> findByContentAndCategoryContaining(Pageable pageable, @Param("search") String search,
                        @Param("letter") String letter);

        @Query("SELECT b FROM BoardEntity b WHERE " +
                        "b.memberEntity.nickName LIKE %:search% AND " +
                        "b.category LIKE %:letter%")
        Page<BoardEntity> findByWriterAndCategoryContaining(Pageable pageable, @Param("search") String search,
                        @Param("letter") String letter);

        @Query("SELECT b FROM BoardEntity b WHERE " +
                        "b.category LIKE %:letter% AND " +
                        "(b.title LIKE %:search% OR b.content LIKE %:search% OR b.memberEntity.nickName LIKE %:search%)")
        Page<BoardEntity> findByTitleOrContentOrWriterAndLetterContaining(Pageable pageable,
                        @Param("search") String search,
                        @Param("letter") String letter);

        @Modifying // JPQL
        @Query("update BoardEntity m set m.replyCount = m.replyCount + 1 where m.id = :id")
        void replyCountFn(@Param("id") Long boardId);

        @Modifying // JPQL
        @Query("update BoardEntity m set m.replyCount = m.replyCount - 1 where m.id = :id")
        void replyCountDeleteFn(@Param("id") Long boardId);

        Page<BoardEntity> findAllByMemberEntityId(Long memberId, Pageable pageable);

}
