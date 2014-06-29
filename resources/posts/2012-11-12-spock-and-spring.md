:title Spock and Spring
:published 2012-11-12 20:48
:body

[Spock](http://www.spockframework.org) is a really great testing framework that can be used to make better, more focused Java and Groovy tests of any kind. It builds on top of JUnit, and runs everywhere it does. This means Spock tests will run in your IDE, your build environment and on you continuous integration server. The key selling point of Spock is that it increases readability by enforcing structure and removing unecessary boilerplate.

Spock tests can also use the Spring framework for dependency injection. This post will document how it can be setup and how to avoid some of the pitfalls when using Spring.

Initial setup
==============
Spock supports Spring Context configuration through the [Spock-Spring extension](http://code.google.com/p/spock/wiki/SpringExtension). You have to have the following dependencies on your classpath:

* groovy-all
* spock-core
* spock-spring
* spring-test
* spring-beans

For an actual example, Marcin Gryszko has a [blog post](http://grysz.com/2011/02/15/testing-a-legacy-java-application-with-groovy-spock-spring-test-and-unitils/) about it along with a Github project.

Where is my Spring context?
===========================
In order to use Spring dependency injected beans in your tests, you use the @ContextConfiguration annotation just as you would in JUnit. However, there is one thing to be aware of. Spock has a setupSpec() method that works much in the same way as JUnit's @BeforeClass annotation. However, it can only access @Shared or static fields, meaning that any Spring injected beans must be shared. But this is actually not possible. The result is that the setupSpec() method can' access the Spring context.

```groovy
@ContextConfiguration(locations = ["classpath:/spring-context.xml"])
class MyFancySpringSpec extends Specification {
		
	@Autowired
	SomeService aService

	def setupSpec() {
		// NB! No access to Spring context!
	}

	def setup() {
		// Has access to Spring context
	}

	def "some feature"() {
	// Has access to Spring context
	}		
}
```


Doing something only once?
==========================
The reason for wanting to access the Spring Context in the setupSpec() is typically that you want to initialize your system before any tests are run. In order to achieve this, you can use the [following trick](https://groups.google.com/forum/?fromgroups=#!searchin/spockframework/junit$20code$20junit/spockframework/Ai4VFhUpus4/wPWhdNo0TR8J):

```groovy
@ContextConfiguration(locations = ["classpath:/spring-context.xml"])
class MyFancySpringSpec extends Specification {

 	@Shared boolean setupHasRun = false

  	@Autowired
  	SomeService aService

  	def setup() {
   		if (!setupHasRun) {
     		setupFirstTime()
      		setupHasRun = true
    	}
  	}

  	// Just run once
  	private def setupFirstTime() {
    	aService.doStuff()
	  }
}
```

Using Spring wired beans in the where-clause
============================================
Calling Spring wired beans in a Spock where clause does not work out of the box. The reason for this is that the where clause is actually run before the setup()-method, meaning it [does not have access to the Spring context](https://groups.google.com/forum/?fromgroups=#!topic/spockframework/MLmTPLFSTF8).

One way to get around it is this:

```groovy
@ContextConfiguration(locations = ["classpath:/spring-context.xml"])
class MyFancySpringSpec extends Specification {
	
  	@Shared boolean setupHasRun = false

  	@Autowired
  	MonkeyService monkeyService

  	@Unroll
  	def "not all monkeys like bananas - #comment"() {

  		expect: "all users "
  		findMonkeyUsingSpringBean(monkeyType) == likesBananas

  		where: 
  		// No access to the Spring context

  		monkeyType | likesBananas | comment
  		"big"	   | true	      | "Big monkeys like bananas"
  		"small"    | false        | "small monkeys hate bananas"
  	}

  	// Helper method; called from the expect-clause, thus has access to the Spring Context
  	def findMonkeyUsingSpringBean(String type) {
  		if (type == "big") {
  			return monkeyService.bigMonkeysLikeBananas()
  		} else if (type == "small") {
			return monkeyService.smallMonkeysLikeBananas()
  		}
  	}
}
```

Except for the cases described above, the Spock framework really shines when testing Java enterprise applications that rely on Spring. Groovy may not be the perfect language for all kinds of tasks, but it fits well for writing test code.

Other resources
================
In order to get you started with Spock, I highly recommend [Peter Niederwieser's presentation](http://vimeo.com/33947244). If you are looking for a tool to do functional testing, [Luke Daley has a great presentation](http://skillsmatter.com/podcast/groovy-grails/spock) of what Spock has to offer in that respect.

There is also the [official Spock-Spring documentation](http://code.google.com/p/spock/wiki/SpringExtension).