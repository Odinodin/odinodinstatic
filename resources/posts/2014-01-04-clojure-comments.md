:title Clojure comments
:published 2014-01-04
:dek Getting it right
:body

There are multiple mechanisms for commenting Clojure code. I thought I'd write up my current knowledge of the subject, perhaps it will be useful for others as well. If you see any improvements, please feel free to comment (\*snicker\*).

A semi-colon designates the start of a comment to the end of the line. The convention is to use single semi-colons for commenting at ends of lines and to use double semi-colons to comment a block of code. 

```clj
;; Beware, side-effects!
(do 
  (init-canvas canvas)                      ; Clear the canvas
  (draw-blue-circle canvas (:ball model)))) ; Draw from the model  
```

The [Clojure docs style guide](http://clojuredocs.org/examples_style_guide) has some more examples.

Function definitions can be commented in a string following the argument vector.

```clj
(defn log [& items]
  "Logs to the JavaScript console"
  (.log js/console (apply str items)))
```

Blocks of code can be commented out by the [comment macro](http://clojuredocs.org/clojure_core/clojure.core/comment).

```clj
(comment
  ;; Will not be executed ...
  (do-stuff))
```

There is a tiny little detail that you need to know about though; the comment macro yields nil!

```clj
;; Ooops, will explode in your face at runtime!
(+ 1 2 (comment (+ 1 1)))
user=> NullPointerException   clojure.lang.Numbers.ops (Numbers.java:942)
```

The comment macro is beautifully implemented, just look at the source code:

```clj
(defmacro comment
  "Ignores body, yields nil"
  {:added "1.0"}
  [& body])
```

A similar construct is the highly useful *#\_* reader macro. When the Clojure reader comes across a *#\_*, it ignores the following form. It does not return nil. 

```clj
(+ 1 2 #_(+ 1 1))
user=> 3
```

Note that both the comment macro and #_ requires the following code to be valid. 

```clj
(+ 1 2 #_( [ )) ; Unmatched bracket!
user=> RuntimeException Unmatched delimiter: )  
       clojure.lang.Util.runtimeException (Util.java:219)
```

In general, only write comments where necessary; code should preferrably be self-explanatory.

For further (opinionated) thoughts on Clojure comments and other code style related topics, the [Prismatic engineering practices](https://github.com/Prismatic/eng-practices/blob/master/clojure/20130927-ns-organization.md) is an interesting read. In a similar vein, see the [Scheme style guide](http://mumble.net/~campbell/scheme/style.txt).