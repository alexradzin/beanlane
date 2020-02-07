# BeanLane 
[![CircleCI](https://circleci.com/gh/alexradzin/beanlane/tree/master.svg?style=svg)](https://circleci.com/gh/alexradzin/beanlane/tree/master)
[![Build Status](https://travis-ci.org/alexradzin/beanlane.svg?branch=master)](https://travis-ci.org/alexradzin/beanlane)
[![codecov](https://codecov.io/gh/alexradzin/beanlane/branch/master/graph/badge.svg)](https://codecov.io/gh/alexradzin/beanlane)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/6a26c7c5106c4831a5ca6d74e9eda49d)](https://www.codacy.com/app/alexradzin/beanlane?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=alexradzin/beanlane&amp;utm_campaign=Badge_Grade)

A simple utility that allows getting the name of bean properties as string without reflection.

## Motivation

There are a lot of libraries that operate with string representation of bean properties usually when building the
query criteria. For example Hibernate, JPA, MongoDB client etc. The following example shows query of Mongo DB:

```java
// connection
Datastore datastore = new Morphia().createDatastore(new MongoClient(), "people");
// query
Query<Person> query = datastore.createQuery(Person.class);
query.and(
        query.criteria("firstName").equalIgnoreCase(firstName),
        query.criteria("lastName").equalIgnoreCase(lastName),
        query.criteria("age").greaterThanOrEq(age));
query.order(Sort.descending("lastName")).asList(new FindOptions().skip(limit * page).limit(limit));
```

Existence of strings "firstName", "lastName" and "age" here is bad. If the appropriate fields in model are renamed to
"givenName" and "familyName" one have to go through all queries and fix them. In case of Hibernate the wrong queries will
at least throw exception at runtime, that help to locate problems earlier. But schema-less Mongo queries will just return
wrong results whithout any failures.

Libraries like jOOQ and QueryDSL solve this problems using code generation. BeanLane suggests solution without any code generation,
so you can continue using your favorite criteria API in slightly safer manner.

```java
// query
Query<Person> query = datastore.createQuery(Person.class);
Person p = $(Person.class);
query.and(
        query.criteria($(p::getFirstName)).equalIgnoreCase(firstName),
        query.criteria($(p::getLastName)).equalIgnoreCase(lastName),
        query.criteria($(p::getAge)).greaterThanOrEq(age));
query.order(Sort.descending("lastName")).asList(new FindOptions().skip(limit * page).limit(limit));
```

As we can see in this example "magic" function call `$(p::getFirstName)` returns string `firstName`, `$(p::getAge)` returns `lastName` etc. 

## Quick start

The library is still not published in maven repository but this will be done soon. Onece this is done just include its artifact into your dependency management script, e.g.

```java
compile 'com.github:org.beanlane:1.0.0'
```

The simplest way to use the library is to make your DAO layer class to implement `BeanLaneShortSpec`, i.e.

```java
public class MyDao implements BeanLaneShortSpec {
}
```

Once this is done 3 magic functions become available:
*   `$()` that generates bean property names (e.g. `lastName`, `age`, `home.street`)
*   `__()` that generates snake lower case names (e.g. `last_name`)
*   `___()` that generates snake upper case names (e.g. `LAST_NAME`)

## How does it work
The interface `BeanLaneShortSpec` provides several sort named default functions that delegate implementation into to
class `BeanLane`. `BeanLane` uses CGLIB to generate proxy over provided class (`Person` in our example), so the names of called 
methods become avaliable and can be returned to application level code. 

Class `BeanLane` has several configuration parameters they can be used if you instantiate it directly without interface
`BeanLaneShortSpec`. This also allows you to change names of magic function according to your taste.

## Why names of magic functions do not follow naming conventions

Well, each function with short name has synonym with conventional name. However, IMHO short names just improve readability.
The statement `$(p::getFirstName)` just a little bit longer than `"firstName"`, however `getName(p::getFirstName)`
is significantly longer and not clearer. People that prefer to use longer, self-explainable traditional java-style names can use `BeanLaneLongSpec`.

## Available specs
First, about the name "spec". This convenstion is taken from various scala libraries, e.g. from scala test that provides several traits named `FlatSpec`, `FunSpec` etc. 
Second, need to mentione one note about the design. Java 8 allowed writing `default` implementation into interfaces that makes it possible to use the "mix-in" paradigm of design. The implementation is written in interface. Class can implement this interface and use implementation provided by the interface. In opposite to base class that limits our design because each class has only one base class interfaces do not have such limitation. 

### BeanLaneShortSpec
We have already saw example of usage of this spec above. This spec provides functions with short, non-conventional names that however have one advantage: the code is shorter, less verbose and even more readable once your are regular to this short notation.

### BeanLaneLongSpec
This sepc is developed for people that prefer longer, self-explainable identifiers:
* `bean()` - returns the java-bean conventional name.
* `lsnake()` - returns lower case snake name. For example `firstName` is transfomed to `first_name`
* `usnake()` - returns upper case snake name. For example `firstName` is transfomed to `FIRST_NAME`

### BeanLaneUpperSnakeSpec and BeanLaneLowerSnakeSpec
Expose short named functions (exactly like BeanLaneShortSpec) that howeever return snake case names in uppper or lower case respectively. 

### BeanLaneAnnotationSpec 
Sometimes we want to get name of field from annotation exactly as verious ORM/OM frameworks do. BeanLane has generic container
annotation that can be used to configure the library to use other annotation. Just make your DAO to implmenent `BeanLaneAnnotationSpec`
and mark it with annotation `@BeanPropertyExtractor`:

```java
@BeanPropertyExtractor(value = XmlElement.class, field = "name")
public class MyXmlDao implements BeanLaneAnnotationSpec {
}

@BeanPropertyExtractor(value = JsonProperty.class)
public class MyJsonDao implements BeanLaneAnnotationSpec {
}
```

`BeanLaneAnnotationSpec` uses strings extracted from annotations instead of from class fields. For example if classPerson` is defined as following:

```java
public class Person {
    @XmlElement(name = "FirstName") private String firstName;
    @XmlElement(name = "LastName") private String lastName;
    @XmlElement(name = "HomeAddress") private Address home;
    //..........
}
```

We can use it as following:

```java
@BeanPropertyExtractor(value = XmlElement.class, field = "name")
class PersonDao implements BeanLaneAnnotationSpec {
    public void foo() {
        Person p = $(Person.class);
        $(p::getFirstName);                     // returns FirstName
        $(() -> p.getHome().getStreetNumber()); // returns HomeAddress.StreetNumber
    }
}
```

### Multiple extractors
Annotation `@BeanPropertyExtractor` can be applied to the same class several times:

```java
@BeanPropertyExtractor(value = JsonProperty.class)
@BeanPropertyExtractor(value = XmlElement.class, field = "name")
class PersonDao implements BeanLaneAnnotationSpec {}
```
In the example above the library will try to locate the property name from `@JsonProperty` and then, if specific property is not marked with this annotation, from `@XmlElement`

### Bean property formatters
Annotation `@BeanPropertyExtractor` can be used to configure formatter. Formatter is a class that implements `Function<String, String>`. It accepts string (for example property or getter name) and returns formatted property name. For example it can accept string `getFirstName` and return `FIRST_NAME`. Various specs described above just configure the library to use specific formattter or a chain of formatters. 

The library provides several pre-implelmented formatters: 
* `GetterFormatter` that removes prefix "`get`" or "`is`" from string. For example it transforms `getFirstName` to `FirstName`
* `CapitalizationFormatter` that capitalizes given string to either `PascalCase` or `camelCase`
* `ToSnakeCaseFormatter` that transforms capitalization-based identifier to snake case, i.e. `getFirstName` to `FIRST_NAME`.
* `RegexFormatter` applies regex on input string in order to create the output. 

As far as formatter implements only standard interface `Function` anyone can implement his/her own custom formatter and use it with BeanLane. 

Formatters can be configured using annotation `@BeanPropertyFormatter` that can be used only with `@BeanPropertyExtractor`. For example the following definition means: extract name of property from field "name" of annotation `@XmlElement` and transform it to snake case using dash as a delimiter.

```java
@BeanPropertyExtractor(value = XmlElement.class, field = "name", formatter = {@BeanPropertyFormatter(value ToSnakeCaseFormatter.class, args="-")})
```
This means that `$(p::getFirstName))` will return `first-name`.

Formatters can be chained, so each formatter uses result of the previous formatter as its input:

```java
@BeanPropertyExtractor(value = XmlElement.class, field = "name", formatter = {@BeanPropertyFormatter(ToSnakeCaseFormatter.class), @BeanPropertyFormatter(value = CaseFormatter.class, args = "UPPER")})
```
The example above formats property extracted from the `@XmlElement` first to snake case, then capitalizes it, e.g. `firstName` will become `FIRST_NAME`.

### BeanPropertyExtractor that does not refer to other annotation
`@BeanPropertyExtractor` can be used without referencing to other annotation. In this case it is used to configure regular `PropertyNameExtractor` with formatters:

```java
@BeanPropertyExtractor(formatter = {@BeanPropertyFormatter(value = GetterFormatter.class), @BeanPropertyFormatter(value = CapitalizationFormatter.class, args = "false")})
```
In example above the name of method `getFirstName()` will be first transformed to `FirstName` by `@GetterFormatter` and then formatted using camelCase by `CapitalizationFormatter` with argument `false` to `firstName`.

### Meta annotations
BeanLane requires separate configuration for each DAO. However typically people use the same style and naming conventions within one application, so it seems very useful to share configuration among different DAOs. BeanLane supports meta annotations that help us to reuse the configuration. It is very easy. All annotations that you would like to write on DAO you can write on special "meta" annotation and then use it.

```java
// This is meta annotation
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@BeanPropertyExtractor(value = JsonProperty.class)
@BeanPropertyExtractor(value = XmlElement.class, field = "name")
@BeanPropertyExtractor(formatter = {@BeanPropertyFormatter(value = GetterFormatter.class), @BeanPropertyFormatter(value = CapitalizationFormatter.class, args = "false")})
@interface JsonXmlPojo {
}
```
Applying aannotation `@JsonXmlPojo` to any DAO has the same effect as copuing all 3 `@BeanPropertyExtractor` to each DAO. 

