package com.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Batch.class)
public abstract class Batch_ {

	public static volatile SingularAttribute<Batch, String> batchname;
	public static volatile SingularAttribute<Batch, Customer> batchowner;
	public static volatile CollectionAttribute<Batch, Addressbook> addressbookCollection;
	public static volatile SingularAttribute<Batch, Date> batchdate;
	public static volatile SingularAttribute<Batch, Integer> batchId;

}

