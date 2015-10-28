package com.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Transactions.class)
public abstract class Transactions_ {

	public static volatile SingularAttribute<Transactions, Double> amount;
	public static volatile SingularAttribute<Transactions, Double> balanceBefore;
	public static volatile SingularAttribute<Transactions, Date> transDate;
	public static volatile SingularAttribute<Transactions, String> description;
	public static volatile SingularAttribute<Transactions, Double> balanceAfter;
	public static volatile SingularAttribute<Transactions, String> source;
	public static volatile SingularAttribute<Transactions, Integer> transactionId;
	public static volatile SingularAttribute<Transactions, String> creditType;
	public static volatile SingularAttribute<Transactions, String> customer;

}

