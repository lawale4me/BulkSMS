package com.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Adminusers.class)
public abstract class Adminusers_ {

	public static volatile SingularAttribute<Adminusers, String> firstname;
	public static volatile SingularAttribute<Adminusers, String> password;
	public static volatile SingularAttribute<Adminusers, Integer> trycount;
	public static volatile SingularAttribute<Adminusers, String> phonenumber;
	public static volatile SingularAttribute<Adminusers, String> secret;
	public static volatile SingularAttribute<Adminusers, Date> datecreated;
	public static volatile SingularAttribute<Adminusers, Integer> userId;
	public static volatile SingularAttribute<Adminusers, String> email;
	public static volatile SingularAttribute<Adminusers, String> lastname;
	public static volatile SingularAttribute<Adminusers, String> username;
	public static volatile SingularAttribute<Adminusers, Integer> status;

}

