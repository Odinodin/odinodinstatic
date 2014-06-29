(ns odinodinstatic.validation
  (:require [schema.core :refer [optional-key validate either Str Keyword Num Any pred both]]))

(def Path (pred (fn [^String s] (re-find #"^(/[a-zA-Z0-9_\-.]+)+/?$" s)) 'simple-slash-prefixed-path))

(def BlogPost
  {:title Str
   :published Str
   :body Str})

(defn validate-content [content]
  (validate {:blog-posts {Path BlogPost}}
            content))