package minchae.meme.repository;

import minchae.meme.entity.MailAuthentication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.Optional;

public interface MailRepository extends JpaRepository<MailAuthentication, Long> {
    void deleteByCreatedTimeBefore(Date date);

    Optional<MailAuthentication> findMailAuthenticationByEmailAndVerificationCode(String email, String vCode);
}
