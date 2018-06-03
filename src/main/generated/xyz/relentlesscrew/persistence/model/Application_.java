package xyz.relentlesscrew.persistence.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Application.class)
public abstract class Application_ {

	public static volatile SingularAttribute<Application, Date> appliedOn;
	public static volatile SingularAttribute<Application, String> dauntlessUsername;
	public static volatile SingularAttribute<Application, Long> id;
	public static volatile SingularAttribute<Application, String> discordUsername;

}

