(ns odinodinstatic.pages
  (:require [hiccup.element :refer [link-to]]
            [me.raynes.cegdown :as md]
            [odinodinstatic.layout :as layout]
            [odinodinstatic.util :refer [map-vals]]
            [stasis.core :as stasis]))

(def pegdown-options                                        ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough])


(defn render-blog-post [blog-post]
  (layout/layout-page
    (list
      [:h1 (:title blog-post)]
      [:h4 (:published blog-post)]
      (md/to-html (:body blog-post) pegdown-options))))

;; TODO Rename functions ...
(defn markdown-pages [pages]
  (map-vals render-blog-post pages))

;; TODO Make the markup for blog posts ...
(defn blog-post-pages [blog-posts]
  (markdown-pages blog-posts))

(defn blog-list-page [blog-posts]
  (let [sorted-blog-posts (-> (sort-by key blog-posts) reverse)]
    {"/"
      (layout/layout-page
        [:ul {:class "vertical-list"}
         (for [[path post] sorted-blog-posts]
           [:li
            (link-to {:class "box"} path (list [:span.contrast (:published post)] " " (:title post)))])])}))

(defn create-pages [content]
  (stasis/merge-page-sources
    {:blog-post-pages (blog-post-pages (:blog-posts content))
     :blog-list       (blog-list-page (:blog-posts content))}))
