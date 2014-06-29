(ns odinodinstatic.pages
  (:require [stasis.core :as stasis]
            [clojure.string :as str]
            [me.raynes.cegdown :as md]
            [odinodinstatic.layout :as layout]
            [odinodinstatic.util :refer [fmap]]))


(def pegdown-options                                        ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough])

(defn render-markdown-page [page]
  (layout/layout-page (md/to-html page pegdown-options)))

;; TODO Filter out md before this stage ..
(defn markdown-pages [pages]
  (zipmap (map #(str/replace % #"\.md$" "") (keys pages))
          (map render-markdown-page (vals pages))))

;; TODO Make the markup for blog posts ...
(defn blog-post-pages [blog-posts]
  (markdown-pages (fmap :body blog-posts)))

(defn blog-list-page [blog-posts]
  {"/page"
    (layout/layout-page
      [:ul
       (for [[path post] blog-posts]
         [:li (str (:published post) " - " (:title post))]
         )])})

(defn create-pages [content]
  (stasis/merge-page-sources
    {:blog-post-pages (blog-post-pages (:blog-posts content))
     :blog-list       (blog-list-page (:blog-posts content))}))