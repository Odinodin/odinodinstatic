(ns odinodinstatic.layout
  (:require [hiccup.page :refer [html5]]
            [clojure.java.io :as io]
            [hiccup.element :refer [link-to]]))

(defn layout-page [page]
  (html5
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name    "viewport"
             :content "width=device-width, initial-scale=1.0"}]
     [:title "Odin odin blog"]
     [:link {:rel "stylesheet" :href "/css/responsive.css"}]
     [:link {:rel "shortcut icon" :type "image/png" :href "favicon.ico"}]
     [:link {:rel "alternate" :type "application/atom+xml" :href "/atom.xml" :title "Odinodin blog"}]]

    [:body
     [:script (slurp (io/resource "public/scripts/analytics.js"))]
     [:div {:id "main" :class "vertical-list"}
      [:div {:class "navbar flex-item"}
       [:div {:class "title"} "Odin"]
       [:a {:class "nav-item"
            :href  "/"} "Posts"]
       [:a {:class "nav-item"
            :href  "/about/"} "About"]
       [:a {:class "nav-item ender"
            :href  "http://github.com/odinodin/odinodinstatic"} "Source"]]
      [:div.content {:class "flex-item"} page]
      [:div {:id "bottom" :class "flex-item horizontal-list"}
       (link-to {:class "bottom-box"} "http://www.kodemaker.no/cv/odin/" "CV")
       (link-to {:class "bottom-box"} "https://twitter.com/odinodin" "Twitter")
       (link-to {:class "bottom-box"} "http://www.linkedin.com/in/odinholestandal" "Linkedin")
       (link-to {:class "bottom-box"} "http://stackoverflow.com/users/273594/odinodin" "stackoverflow")
       (link-to {:class "bottom-box"} "http://www.kodemaker.no" "Kodemaker")
       (link-to {:class "bottom-box"} "https://www.flickr.com/photos/odinodin" "Flickr")
       (link-to {:class "bottom-box"} "http://500px.com/odinodin" "500px")
       (link-to {:class "bottom-box"} "/atom.xml" "RSS")]]]))