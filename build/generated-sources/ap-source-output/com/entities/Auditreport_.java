package com.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Auditreport.class)
public abstract class Auditreport_ {

	public static volatile SingularAttribute<Auditreport, String> ipAddress;
	public static volatile SingularAttribute<Auditreport, String> action;
	public static volatile SingularAttribute<Auditreport, Integer> id;
	public static volatile SingularAttribute<Auditreport, String> userName;
	public static volatile SingularAttribute<Auditreport, Date> actionDate;

}

