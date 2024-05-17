package beside2.ten039.repository;

import beside2.ten039.domain.RollingPaper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RollingPaperRepository extends JpaRepository<RollingPaper, Long> {
    Page<RollingPaper> findAllByIdLessThanOrderByIdDesc(Long id, PageRequest request);
    List<RollingPaper> findByIdLessThanOrderByIdDesc(Long id, Pageable page);
    List<RollingPaper> findAllByOrderByIdDesc(Pageable page);
    Boolean existsByIdLessThan(Long id);

    @Query(value = "select * from rolling_paper order by id desc limit 1", nativeQuery = true)
    RollingPaper lastId();

    Long countAllById(Long id);
}
