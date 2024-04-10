package ru.practicum.shareit.user.repository;
import ru.practicum.shareit.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

   User findByEmailAndIdNot(String email, Integer userId);

}
