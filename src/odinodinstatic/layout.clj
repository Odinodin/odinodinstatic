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
     [:link {:rel "stylesheet" :href "/css/responsive.css"}]]
    [:body
     [:div {:id "main" :class "vertical-list"}
      [:div {:class "navbar flex-item"}
       [:div {:class "title"} "Odin"]
       [:a {:class "nav-item"
            :href "/"} "Poster"]
       [:a {:class "nav-item ender"
            :href "http://github.com/odinodin/odinodinstatic"} "Kildekode"]]
      [:div.content {:class "flex-item"} page]
      [:div {:id "bottom" :class "flex-item horizontal-list"}
       (link-to {:class "bottom-box"} "http://www.kodemaker.no/cv/odin/" "CV")
       (link-to {:class "bottom-box"} "https://twitter.com/odinodin" "Twitter")
       (link-to {:class "bottom-box"} "http://www.linkedin.com/in/odinholestandal" "Linkedin")
       (link-to {:class "bottom-box"} "http://stackoverflow.com/users/273594/odinodin" "stackoverflow")
       (link-to {:class "bottom-box"} "http://www.kodemaker.no" "Kodemaker")
       (link-to {:class "bottom-box"} "https://www.flickr.com/photos/odinodin" "Flickr")
       (link-to {:class "bottom-box"} "http://500px.com/odinodin" "500px")]]]))
