(ns odinodinstatic.layout
  (:require [hiccup.page :refer [html5]]))

(defn layout-page [page]
  (html5
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport"
             :content "width=device-width, initial-scale=1.0"}]
     [:title "Odin odin blog"]
     [:link {:rel "stylesheet" :href "/css/styles.css"}]]
    [:body
     [:div {:id "main"}
      [:h1 "Odinodin"]
      [:div.body page]]]))