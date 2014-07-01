:title Mapping maps
:published 2014-07-01
:body

Når jeg jobber med Clojure kommer jeg av og til bort i tilfeller hvor jeg har en map og ønsker transformere 
nøklene eller verdiene i mappet. Overraskende nok så har jeg ikke funnet noen funksjoner som tilbyr dette ut av boksen
 i de innebygde Clojure-bibliotekene. 
 
Så hvordan transformerer man alle verdiene, mens man samtidig beholder nøklene i map'et? 
Zipmap er en fin funksjon å bruke til dette.

```clj
(defn map-vals [f m]
  (zipmap (keys m)
          (map f (vals m))))
```

Eksempel:

```clj
(map-vals inc {:a 1 :b 2}) 
=> {:a 2 :b 3}
```

Tilsvarende, hvordan transformerer man nøklene mens man beholder verdiene i map'et? 
Igjen passer zipmap som hånd i hanske.

```clj
(defn map-keys [f m]
  (zipmap (map f (keys m))
          (vals m)))
```

Eksempel:

```clj
(map keys inc {1 "a" 2 "b"}) 
=> {2 "a" 3 "b"}
```

Avslutningsvis så er det kjekt å ha en funksjon som mapper over både nøkler og verdier samtidig, så slipper vi gjøre
det i to operasjoner.

```clj
(defn map-keys-vals [kfn vfn m]
  (zipmap (map kfn (keys m))
          (map vfn (vals m))))
```

Eksempel:

```clj
(map-keys-vals inc inc {1 1, 2 2})
=> {2 2, 3 3}
```