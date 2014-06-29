(ns odinodinstatic.pages
  (:require [stasis.core :as stasis]
            [clojure.string :as str]
            [me.raynes.cegdown :as md]
            [hiccup.element :refer [link-to]]
            [odinodinstatic.layout :as layout]
            [odinodinstatic.util :refer [map-vals]]))


(def pegdown-options                                        ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough])

(defn render-markdown-page [page]
  )

(defn render-blog-post [blog-post]
  (layout/layout-page
    (list
      [:h1 (:title blog-post)]
      (md/to-html (:body blog-post) pegdown-options)))
  )

;; TODO Filter out md before this stage ..
(defn markdown-pages [pages]
  (map-vals render-blog-post pages))

;; TODO Make the markup for blog posts ...
(defn blog-post-pages [blog-posts]
  (markdown-pages blog-posts))

(defn blog-list-page [blog-posts]
  {"/"
    (layout/layout-page
      [:ul
       (for [[path post] blog-posts]
         [:li (:published post) " " (link-to path
                                             (:title post))])])})

(defn create-pages [content]
  (stasis/merge-page-sources
    {:blog-post-pages (blog-post-pages (:blog-posts content))
     :blog-list       (blog-list-page (:blog-posts content))}))