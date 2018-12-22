package rebue.ord.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import rebue.ord.jo.OrdOrderJo;

public interface OrdOrderDao extends JpaRepository<OrdOrderJo, java.lang.Long> {
}
