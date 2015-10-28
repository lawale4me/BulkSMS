package com.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Outmessages.class)
public abstract class Outmessages_ {

	public static volatile SingularAttribute<Outmessages, Customer> senderId;
	public static volatile SingularAttribute<Outmessages, Integer> messageType;
	public static volatile SingularAttribute<Outmessages, Date> sendDate;
	public static volatile SingularAttribute<Outmessages, String> destAddress;
	public static volatile SingularAttribute<Outmessages, Integer> msgId;
	public static volatile SingularAttribute<Outmessages, String> header;
	public static volatile SingularAttribute<Outmessages, String> message;
	public static volatile SingularAttribute<Outmessages, Integer> status;
	public static volatile SingularAttribute<Outmessages, Date> msgDate;

}

