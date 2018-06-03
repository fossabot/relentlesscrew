package xyz.relentlesscrew.persistence.DAO;

import org.hibernate.Session;
import xyz.relentlesscrew.persistence.model.Rank;
import xyz.relentlesscrew.persistence.model.Rank_;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class RankDAO extends GenericDAOImpl<Rank, Long> {

    public Rank findByRankName(String rankName) {
        Session session = getSession();

        session.beginTransaction();
        CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
        CriteriaQuery<Rank> query = criteriaBuilder.createQuery(Rank.class);
        Root<Rank> root = query.from(Rank.class);
        query.where(criteriaBuilder.equal(root.get(Rank_.name), rankName));

        Rank rank = session.createQuery(query).uniqueResult();

        session.getTransaction().commit();

        return rank;
    }
}
