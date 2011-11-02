This module gives to [Mule](http://www.mulesoft.org/what-mule-esb) the ability to use the [Spring Expression Language](http://static.springsource.org/spring/docs/3.0.5.RELEASE/reference/expressions.html) as an expression evaluator. 

Mule module SpEL covers the gap between the very weak functionality of comparators and optional values using the basic Mule mechanisms and the über powerful scripting languages like groovy.

Wherever others expression evaluators like groovy, xpath, header or ogln are used, SpEL can be used too.

# The Spring Expression Language
> The Spring Expression Language (SpEL for short) is a powerful expression language that supports querying and manipulating an object graph at runtime. The language syntax is similar to Unified EL but offers additional features, most notably method invocation and basic string templating functionality.

## SpEL Resources
* [Official reference](http://static.springsource.org/spring/docs/current/spring-framework-reference/html/expressions.html).
* Very good [tutorial](http://dhruba.name/2009/12/30/spring-expression-language-spel-primer/).

# Installation
The expresion evaluator can either be installed for all applications running within the Mule instance or can be setup to be used for a single application.

## All Applications
Download the connector from the link above and place the resulting jar file in /lib/user directory of the Mule installation folder.

## Single Application
To make the SpEL expression evaluator available only to single application then place it in the	lib directory of the application otherwise if using Maven to compile and deploy your application the following can be done:

Add the expresion evaluator's maven repo to your pom.xml:

	<repositories>
	    <repository>
	        <id>muleforge-releases</id>
	        <name>MuleForge Snapshot Repository</name>
	        <url>http://repository.mulesoft.org/releases/</url>
	        <layout>default</layout>
	    </repository>
	</repositories>

Add the connector as a dependency to your project. This can be done by adding the following under the dependencies element in the pom.xml file of the application:

	<dependency>
	    <groupId>org.mule.modules</groupId>
	    <artifactId>mule-module-spel</artifactId>
	    <version>1.0</version>
	</dependency>

# Usage
## Special Variables
To the regular set of possibilities of SpEL, mule-module-spel will add a few more in the form of variables :

* `#context` the mule context
* `#registry`	the mule registry
* `#message`	the treated message
* `#originalPayload`	the original payload of the message
* `#payload`	the payload of the message
* `#id`	the mule message id
* `#systemProperties` the system properties also available as @systemProperties
* `#systemEnvironment` the environment properties also available as @systemEnvironment

## Beans
In SpEL, beans can be accessed using `@beanname`. Mule registry entries, and thus the mule spring context beans can be reached using this method (see the last example below).

# Examples
    <expression-transformer>
        <return-argument evaluator="custom" custom-evaluator="spel" expression="'hello' "/>
        <return-argument evaluator="custom" custom-evaluator="spel" expression="#payload.name.toUpperCase() "/>
    </expression-transformer>

    <message-properties-transformer>
        <add-message-property key="1" value="#[spel:new String('hello world').toUpperCase()]"/>
        <add-message-property key="2" value="#[spel:'good'.concat('bye')]"/>
    </message-properties-transformer>

    <expression-filter evaluator="custom" customEvaluator="spel" expression="#payload.active"/>

    <spring:beans>
        <spring:bean name="myBittenApple" class="org.mule.tck.testmodels.fruit.Apple">
            <spring:property name="bitten" value="true" />
        </spring:bean>
    </spring:beans>

    <expression-transformer>
        <return-argument evaluator="custom" custom-evaluator="spel" expression="@myBittenApple.bitten"/>
    </expression-transformer>


