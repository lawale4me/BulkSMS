package com.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Customer.class)
public abstract class Customer_ {

	public static volatile SingularAttribute<Customer, Double> reward;
	public static volatile SingularAttribute<Customer, String> lastName;
	public static volatile SingularAttribute<Customer, String> address;
	public static volatile SingularAttribute<Customer, Integer> accountType;
	public static volatile SingularAttribute<Customer, Integer> reseller;
	public static volatile SingularAttribute<Customer, Date> regDate;
	public static volatile CollectionAttribute<Customer, Outmessages> outmessagesCollection;
	public static volatile CollectionAttribute<Customer, Draft> draftCollection;
	public static volatile SingularAttribute<Customer, String> firstName;
	public static volatile CollectionAttribute<Customer, Addressbook> addressbookCollection;
	public static volatile CollectionAttribute<Customer, Batch> batchCollection;
	public static volatile SingularAttribute<Customer, String> password;
	public static volatile SingularAttribute<Customer, Double> balance;
	public static volatile SingularAttribute<Customer, Double> rate;
	public static volatile CollectionAttribute<Customer, Customer> customerCollection;
	public static volatile SingularAttribute<Customer, String> phone;
	public static volatile SingularAttribute<Customer, Integer> customerId;
	public static volatile SingularAttribute<Customer, String> activationCode;
	public static volatile SingularAttribute<Customer, String> email;
	public static volatile SingularAttribute<Customer, String> username;
	public static volatile SingularAttribute<Customer, Integer> status;

}

