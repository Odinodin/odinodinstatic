(ns odinodinstatic.layout
  (:require [hiccup.page :refer [html5]]
            [hiccup.element :refer [link-to]]
            ))

(defn layout-page [page]
  (html5
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name    "viewport"
             :content "width=device-width, initial-scale=1.0"}]
     [:title "Odin odin blog"]
     [:link {:rel "stylesheet" :href "/css/styles.css"}]]
    [:body
     [:div {:id "main"}
      [:h1 "Odinodin"]
      (link-to "/" "Posts")
      [:div.body page]
      [:div {:id "bottom"}
       (link-to "https://twitter.com/odinodin" "Twitter")
       (link-to "https://www.flickr.com/photos/odinodin" "Flickr")
       (link-to "http://500px.com/odinodin" "500px")
       (link-to "http://www.linkedin.com/in/odinholestandal" "Linkedin")
       (link-to "http://www.kodemaker.no/cv/odin/" "CV")
       (link-to "http://stackoverflow.com/users/273594/odinodin" "stackoverflow")
       (link-to "http://www.kodemaker.no" "Kodemaker")]]]))