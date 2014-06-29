:title Maven Version Housekeeping
:published 2013-02-21 21:05
:body

It is inevitable. In any project of a certain size, after a while, you are faced with the challenge of rotting dependencies. To be more precise, the world has moved on and you, for whatever reason, want to keep up.  

TLDR;
The versions-maven-plugin generates lots of noise. Unix can help manage it.

Step 1 - Figure out what has changed
====================================
Fortunately, Maven has a set of tools for figuring out which dependencies have changed. The plugin we'll be using is the [versions-maven-plugin](http://mojo.codehaus.org/versions-maven-plugin/)

The goal that helps us is the *display-dependency-updates*. Just run the following in your project:

```
mvn versions:display-dependency-updates
```

In the following I will use the [maven-surefire-plugin](http://maven.apache.org/surefire/maven-surefire-plugin/) source code as an example.

Step 2 - Unix to the resque
===========================
In a multi-module project, the versions-plugin will generate tons of text. The information you are after is buried in a verbose black hole. Most likely, several of your dependencies are the same across multiple modules. How do you sift through all this information?

One option is to break out your text editor and spend 15 minutes deleting text by hand until you find what you need. This process must be repeated manually the next time you want to check for new versions. 

As fun as that sounds, we are lazy and automate. If you are ever faced with the task to handle large amounts of text, *always* try the Unix toolbox first. Chances are you will find what you need. 

First, we make sure all version information is on one line. The excerpt below illustrates the problem. The dependency name is too long, making the version output break the line.

```
[INFO]   org.apache.maven.plugin-testing:maven-plugin-testing-harness ...
[INFO]                                                               1.2 -> 2.1
```

I chose to solve this with *sed* (GNU sed), but *tr* or *awk* would do the same use.

```
sed -r ':a;N;$!ba;s/\s+(\.\.\.){1}\n(\[INFO\]){1}\s+/ /g'
```

Second, we get rid of the redundancy with *uniq*. Uniq requires its input to be sorted as it only compares adjacent lines for similarity. Thus we pipe the output to *sort* first.

Finally, we only care about what has actually changed. *Grep* will filter out any line that does not contain an arrow, '->'.

The final command is thus:

```
mvn versions:display-dependency-updates 
| gsed -r ':a;N;$!ba;s/\s+(\.\.\.){1}\n(\[INFO\]){1}\s+/ /g'
| sort | uniq 
| grep '.*->'
```

The result is an output that is exactly what we need, no more.

```
[INFO]   commons-io:commons-io ................................... 2.0.1 -> 2.4
[INFO]   jmock:jmock ................................. 1.0.1 -> 20031129.200437
[INFO]   junit:junit ............................................ 3.8.1 -> 4.11
[INFO]   junit:junit ............................................ 3.8.2 -> 4.11
[INFO]   junit:junit ............................................ 4.8.1 -> 4.11
[INFO]   junit:junit ............................................. 4.10 -> 4.11
[INFO]   junit:junit .............................................. 4.0 -> 4.11
[INFO]   net.sourceforge.htmlunit:htmlunit ........................ 2.8 -> 2.11
[INFO]   org.apache.maven.doxia:doxia-core ....................... 1.1.4 -> 1.3
[INFO]   org.apache.maven.doxia:doxia-decoration-model ........... 1.1.4 -> 1.3
[INFO]   org.apache.maven.doxia:doxia-sink-api ................... 1.1.4 -> 1.3
[INFO]   org.apache.maven.doxia:doxia-site-renderer .............. 1.1.4 -> 1.3
[INFO]   org.apache.maven.plugin-testing:maven-plugin-testing-harness 1.2 -> 2.1
[INFO]   org.apache.maven.reporting:maven-reporting-api .......... 2.0.9 -> 3.0
[INFO]   org.apache.maven.reporting:maven-reporting-impl ........... 2.1 -> 2.2
[INFO]   org.apache.maven.shared:maven-common-artifact-filters ..... 1.3 -> 1.4
[INFO]   org.apache.maven.surefire:surefire-api ................ 2.12.4 -> 2.13
[INFO]   org.apache.maven.surefire:surefire-booter ............. 2.12.4 -> 2.13
[INFO]   org.apache.maven.surefire:surefire-junit3 ............. 2.12.4 -> 2.13
[INFO]   org.apache.maven:maven-artifact ....................... 2.0.9 -> 3.0.5
[INFO]   org.apache.maven:maven-artifact ......................... 2.0 -> 3.0.5
[INFO]   org.apache.maven:maven-core ........................... 2.0.9 -> 3.0.5
[INFO]   org.apache.maven:maven-model .......................... 2.0.9 -> 3.0.5
[INFO]   org.apache.maven:maven-plugin-api ..................... 2.0.9 -> 3.0.5
[INFO]   org.apache.maven:maven-plugin-descriptor .............. 2.0.9 -> 2.2.1
[INFO]   org.apache.maven:maven-project .................. 2.0.9 -> 3.0-alpha-2
[INFO]   org.apache.maven:maven-settings ....................... 2.0.6 -> 3.0.5
[INFO]   org.apache.maven:maven-toolchain ................ 2.0.9 -> 3.0-alpha-2
[INFO]   org.codehaus.plexus:plexus-utils ..................... 3.0.8 -> 3.0.10
[INFO]   org.mockito:mockito-all ................................. 1.7 -> 1.9.5
[INFO]   org.testng:testng ......................................... 5.7 -> 6.8
```