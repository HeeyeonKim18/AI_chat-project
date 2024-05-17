package beside2.ten039.repository;

import beside2.ten039.domain.Chat;
import beside2.ten039.domain.Member;
import beside2.ten039.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query("select c from Chat c where c.member =:member and c.role =:role order by c.createdDate desc limit 1")
    Chat lastMessage(@Param("member") Member Member, @Param("role") Role role);

    Page<Chat> findAllByMemberAndIdLessThanOrderByIdDesc(Member member, Long id, PageRequest request);

    int countAllByMember(Member member);

    List<Chat> findAllByMemberOrderByIdDesc(Member member, Pageable page);

    List<Chat> findByMemberAndIdLessThanOrderByIdDesc(Member member, Long id, Pageable page);

    Boolean existsByIdLessThan(Long id);

    @Query(value = "select * from chat order by id desc limit 1", nativeQuery = true)
    Chat lastId();

}
