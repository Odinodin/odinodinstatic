(defproject odinodinstatic "0.1.0-SNAPSHOT"
  :description "Odinodin static blog generator"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url  "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [stasis "2.2.2"]
                 [ring "1.2.1"]
                 [hiccup "1.0.5"]
                 [enlive "1.1.5"]
                 [clygments "0.1.1"]
                 [optimus "0.14.2"]
                 [org.clojure/data.xml "0.0.7"] ;; For Atom feed
                 [mapdown "0.2.1"]
                 [prismatic/schema "0.2.0"]
                 [me.raynes/cegdown "0.1.1"]]
  :ring {:handler odinodinstatic.web/app}
  :jvm-opts ["-Xmx1024m"]
  :aliases {"build-site" ["run" "-m" "odinodinstatic.web/export"]}

  :profiles {:dev {:plugins [[lein-ring "0.8.10"]]}})