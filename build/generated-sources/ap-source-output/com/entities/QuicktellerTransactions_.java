package com.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(QuicktellerTransactions.class)
public abstract class QuicktellerTransactions_ {

	public static volatile SingularAttribute<QuicktellerTransactions, String> respDesc;
	public static volatile SingularAttribute<QuicktellerTransactions, Double> amount;
	public static volatile SingularAttribute<QuicktellerTransactions, String> txRef;
	public static volatile SingularAttribute<QuicktellerTransactions, String> customerId;
	public static volatile SingularAttribute<QuicktellerTransactions, Integer> qtID;
	public static volatile SingularAttribute<QuicktellerTransactions, String> respCode;
	public static volatile SingularAttribute<QuicktellerTransactions, Integer> requestStatus;
	public static volatile SingularAttribute<QuicktellerTransactions, Date> tranxDate;
	public static volatile SingularAttribute<QuicktellerTransactions, String> status;

}

