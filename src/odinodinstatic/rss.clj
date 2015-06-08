(ns odinodinstatic.rss
  (:require [clojure.data.xml :as xml]))

(defn- entry [[path post]]
  [:entry
   [:title (:title post)]
   [:updated (:published post)]
   [:author [:name "Odin Standal"]]
   [:link {:href (str "http://odinodin.no" path)}]
   [:id (str "urn:odinodin-no:feed:post:" (:name post))]
   [:summary {:type "html"} (:dek post)]])

;; List of {"path-to-post" {:published .. :title .. :name ... :dek}}
(defn atom-xml [posts]
  (xml/emit-str
    (xml/sexp-as-element
      [:feed {:xmlns "http://www.w3.org/2005/Atom"}
       [:id "urn:odinodin-no:feed"]
       [:updated (-> posts first second :published)]
       [:title {:type "text"} "OdinOdin blog"]
       [:link {:rel "self" :href "http://odinodin.no/atom.xml"}]
       (map entry posts)])))