package rebue.ord.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rebue.ord.jo.OrdTaskJo;

public interface OrdTaskDao extends JpaRepository<OrdTaskJo, java.lang.Long> {
}
