(ns odinodinstatic.pages
  (:require [hiccup.element :refer [link-to]]
            [me.raynes.cegdown :as md]
            [odinodinstatic.layout :as layout]
            [odinodinstatic.rss :as rss]
            [odinodinstatic.util :refer [map-vals]]
            [stasis.core :as stasis]
            [odinodinstatic.highlight :as highlight]))

(def pegdown-options  ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough])

(defn highlight-code [pages]
  "Highlights code blocks"
  ;; by sending a function and not a string containing HTML, stasis will only generate
  ;; pages on-demand, thus improving the development process
  (map-vals
    #(fn [_] (highlight/highlight-code-blocks %))
    pages))

(defn render-blog-post [blog-post]
  (layout/layout-page
    (list
      [:h1 {:class "large-text-box"} (:title blog-post)]
      [:div {:class "horizontal-left-list"}
       [:div {:class "date-label"} (:published blog-post)]
       (when-let [dek (:dek blog-post)] [:h3 {:class "subtitle"} dek])]
      (md/to-html (:body blog-post) pegdown-options))))

(defn- blog-post-pages [blog-posts]
  "Render each blog post"
  (map-vals render-blog-post blog-posts))

(defn- blog-list-page [blog-posts]
  "A list of blog posts with links to each post"
  (let [sorted-blog-posts (-> (sort-by key blog-posts) reverse)]
    {"/"
      (layout/layout-page
        [:ul {:class "vertical-list"}
         (for [[path post] sorted-blog-posts]
           [:li
            (link-to {:class "box horizontal-list"} path (list
                                                           [:span.contrast {:class "post-date"} (:published post)]
                                                           [:span {:class "post-title"} " " (:title post)]))])])}))

(defn- about-page []
  {"/about/" (layout/layout-page
               (list
                 [:h1 {:class "large-text-box"} "About"]
                 [:p "I'm a curious developer who likes to learn. I work at Kodemaker, a Norwegian consultancy
                  where I earn a living by editing text files."]))})

(defn create-pages [content]
  "Converts raw content into HTML pages"
  (stasis/merge-page-sources
    {:blog-post-pages (-> content :blog-posts  blog-post-pages highlight-code)
     :blog-list       (blog-list-page (:blog-posts content))
     :about-page      (about-page)
     :rss             {"/atom.xml" (rss/atom-xml (:blog-posts content))}}))
