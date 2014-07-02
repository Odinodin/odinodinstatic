(ns odinodinstatic.layout
  (:require [hiccup.page :refer [html5]]
            [hiccup.element :refer [link-to]]))

(defn layout-page [page]
  (html5
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name    "viewport"
             :content "width=device-width, initial-scale=1.0"}]
     [:title "Odin odin blog"]
     [:link {:rel "stylesheet" :href "/css/styles.css"}]]
    [:body
     [:div {:id "main" :class "vertical-list"}
      [:div {:class "navbar flex-item"}
       [:div {:class "title"} "Odin"]
       [:a {:class "nav-item clickable"
            :href "/"} "Posts"]
       [:a {:class "nav-item ender clickable"
            :href "http://github.com/odinodin/odinodinstatic"} "Source"]]
      [:div.content {:class "flex-item"} page]
      [:div {:id "bottom" :class "flex-item horizontal-list"}
       (link-to {:class "bottom-box clickable"} "http://www.kodemaker.no/cv/odin/" "CV")
       (link-to {:class "bottom-box clickable"} "https://twitter.com/odinodin" "Twitter")
       (link-to {:class "bottom-box clickable"} "http://www.linkedin.com/in/odinholestandal" "Linkedin")
       (link-to {:class "bottom-box clickable"} "http://stackoverflow.com/users/273594/odinodin" "stackoverflow")
       (link-to {:class "bottom-box clickable"} "http://www.kodemaker.no" "Kodemaker")
       (link-to {:class "bottom-box clickable"} "https://www.flickr.com/photos/odinodin" "Flickr")
       (link-to {:class "bottom-box clickable"} "http://500px.com/odinodin" "500px")]]]))
