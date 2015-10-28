package com.entities;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Addressbook.class)
public abstract class Addressbook_ {

	public static volatile SingularAttribute<Addressbook, String> phonenumber;
	public static volatile SingularAttribute<Addressbook, String> name;
	public static volatile SingularAttribute<Addressbook, Integer> addressbookId;
	public static volatile SingularAttribute<Addressbook, Customer> ownerId;
	public static volatile SingularAttribute<Addressbook, Batch> batchId;

}

