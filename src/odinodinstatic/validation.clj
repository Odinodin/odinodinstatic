(ns odinodinstatic.validation
  (:require [schema.core :refer [pred optional-key validate either Str Keyword Num Any pred both]]))

(def valid-paths [#"^(/[a-zA-Z0-9_\-.]+)+/?$"
                  #"/$"])

(def Path (pred (fn [s] (some #(re-find % s) valid-paths))
                'simple-slash-prefixed-path))

(def BlogPost
  {:title Str
   :published Str
   (optional-key :dek) Str
   :body Str})

;; Routes is a map of Paths to page content
(def Routes
  {Path (either Str (pred fn?))})

(defn validate-content [content]
  (validate {:blog-posts {Path BlogPost}}
            content))