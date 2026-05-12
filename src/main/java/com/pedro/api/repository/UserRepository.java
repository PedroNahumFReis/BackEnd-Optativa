package com.pedro.api.repository;

import com.pedro.api.model.User;
import com.pedro.api.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByEmail(String email);

    @Query(nativeQuery = true, value = """
            SELECT u.email AS username,
                   u.password AS password,
                   p.id AS roleId,
                   p.nome AS authority
            FROM tb_user u
            INNER JOIN tb_user_perfil up ON u.id = up.user_id
            INNER JOIN tb_perfil p ON p.id = up.perfil_id
            WHERE u.email = :username
            """)
    List<UserDetailsProjection> searchUserAndRolesByEmail(String username);

}