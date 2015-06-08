:title Validering av Gradle properties
:published 2014-09-14
:dek Spar tid på bedre feilmeldinger
:body

Etter mange år med Maven, så er [Gradle](http://www.gradle.org) en befrielse å jobbe med. Du får 
mye ut av boksen, og når du har behov for det så kan du tilpasse bygget ditt på en enkel måte. Med enkel mener jeg
at du programmerer med et programmeringsspråk rett i byggescriptet ditt og ikke med XML og plugin-artefakter. 

Fattig validering av properties
===============================
Gradle har et konsept om properties som lar deg ta argument fra kommandolinjen. Hva skjer hvis du kjører en task 
og glemmer å spesifisere en påkrevd property?  

La oss lage en task printProperty som bruker to properties 'x' og 'y'.

```groovy
task printProps(description: "Print two properties") {
    doFirst {
        println "x: " + x
        println "y: " + y
    }
}
```

Og så kjører vi tasken uten properties:

```bash
$> gradle printProps

printProps FAILED

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':printProps'.
> Could not find property 'x' on task ':printProps'.   
```

Ikke spesielt imponerende. Det hadde vært fint hvis vi kunne fått beskjed om alle properties som manglet, ikke bare x.
Det finnes et [forslag](https://issues.gradle.org/browse/GRADLE-478) fra 2009 om å forbedre dette i Gradle, men  
mens vi venter så kan vi jo bare fikse det selv.


Validering av properties
========================
Vi lager en hjelpefunksjon som tar inn et prosjekt og en liste med navn på påkrevde properties. Hvis prosjektet
mangler en eller flere properties, så feiler vi bygget. Grunnen til at vi tar inn prosjektet som en referanse
er fordi vi ikke liker å ha referanser til globale variabler i hjelpekoden vår.

```groovy
void require(Project project, List props) {
    def missing = props.findAll {
        !project.hasProperty(it)
    }

    assert missing.isEmpty(), "The task requires these properties: " + props
}
```

I selve tasken kaller vi valideringsmetoden i doFirst. Det må gjøres i doFirst fordi
hvis ikke så skjer valideringen i konfigurasjonsfasen. Vi ønsker å kun validere properties når tasken faktisk skal 
kjøre.

```groovy
task printProps(description: "Print two properties") {
    doFirst {
        require(project, ["x", "y"])

        println "x: " + x
        println "y: " + y
    }
}
```

Hva skjer når vi kjører tasken nå?

```bash
$> gradle printProps

printProps FAILED

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':printProps'.
> The task requires these properties: [x, y]. Expression: missing.isEmpty()
```


Sukker på toppen
================
Vi kan ta dette et steg lenger og dynamisk legge til validering av properties på alle tasks som har en property
med navn "requiredProps". Det gjør vi på følgende måte:


```groovy
tasks.each { Task t ->
    if (!t.hasProperty("requiredProps")) return

    // Append required properties to task description
    String pDesc = "Required properties: " + t.requiredProps
    if (t.description) {
        t.description += " | " + pDesc
    } else {
        t.description = pDesc
    }

    // When task is run, verify properties
    t.doFirst {
        require(project, t.requiredProps)
    }
}
```

Deretter oppdaterer vi tasken ved å legge til en ext.requiredProps hvor vi deklarerer en liste med påkrevde
properties:

```groovy
task printProps(description: "Print two properties") {
    ext.requiredProps = ["x", "y"]

    doFirst {
        println "x: " + x
        println "y: " + y
    }
}
```

Dette funker på samme måte som tidligere. En bonus er at vi i tillegg skriver ut hvilke properties som tasken
krever som en del av description. Effekten ser vi når vi kjører 'gradle tasks'


```bash
$> gradle tasks
tasks

------------------------------------------------------------
All tasks runnable from root project
------------------------------------------------------------

Other tasks
-----------
printProps - Print two properties | Required properties: [x, y]
```

Det jeg ikke liker med denne løsningen er at vi i requiredProps må oppgi navnet på alle properties i en tekststreng,
noe som potensielt kan glippe når vi refaktorerer.

Vet du om en bedre måte å validere properties på i Gradle? Tips meg gjerne på [twitter](http://twitter.com/odinodin)