package xyz.relentlesscrew.persistence.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Member.class)
public abstract class Member_ {

	public static volatile SingularAttribute<Member, Long> discordId;
	public static volatile SingularAttribute<Member, String> dauntlessUsername;
	public static volatile SingularAttribute<Member, Rank> rank;
	public static volatile SingularAttribute<Member, Long> id;

}

