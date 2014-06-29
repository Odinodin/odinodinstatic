(ns odinodinstatic.pages
  (:require [stasis.core :as stasis]
            [clojure.string :as str]
            [me.raynes.cegdown :as md]
            [odinodinstatic.layout :as layout]))


(def pegdown-options ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough])

(defn render-markdown-page [page]
  (layout/layout-page (md/to-html page pegdown-options)))

(defn markdown-pages [pages]
  (zipmap (map #(str/replace % #"\.md$" "") (keys pages))
          (map render-markdown-page (vals pages))))

;; TODO Make the markup for blog posts ...
(defn blog-post-pages [blog-posts]
  (markdown-pages blog-posts))

(defn create-pages [content]
  (stasis/merge-page-sources
    {:blog-post-pages (blog-post-pages (:blog-posts content))}))