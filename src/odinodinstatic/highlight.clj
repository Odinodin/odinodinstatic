(ns odinodinstatic.highlight
  (:require [clojure.java.io :as io]
            [clygments.core :as pygments]
            [net.cgrand.enlive-html :as enlive]))


(defn- extract-code
  [highlighted]
  (-> highlighted
      java.io.StringReader.
      enlive/html-resource
      (enlive/select [:pre])
      first
      :content))

(defn- highlight [node]
  (let [code (->> node :content (apply str))
        lang (or (->> node :attrs :class keyword)
                 "sh")]
    (assoc node :content (-> code
                             (pygments/highlight lang :html)
                             extract-code))))

(defn highlight-code-blocks [page]
  (enlive/sniptest page
                   [:pre :code] highlight
                   [:pre :code] #(assoc-in % [:attrs :class] "codehilite")))
