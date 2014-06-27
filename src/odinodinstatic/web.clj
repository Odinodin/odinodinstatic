(ns odinodinstatic.web
  (:require [stasis.core :as stasis]
            [optimus.assets :as assets]
            [optimus.export]
            [optimus.optimizations :as optimizations]
            [optimus.prime :as optimus]
            [optimus.strategies :refer [serve-live-assets]]
            [clojure.java.io :as io]
            [clojure.string :as str]
            [me.raynes.cegdown :as md]
            [odinodinstatic.highlight :as highlight]
            [odinodinstatic.layout :as layout]))

(def pegdown-options ;; https://github.com/sirthias/pegdown
  [:autolinks :fenced-code-blocks :strikethrough])

(defn render-markdown-page [page]
  (layout/layout-page (md/to-html page pegdown-options)))

;; Can fix this to avoid having to have content in /about/index.html ...
(defn partial-pages [pages]
  "Add layout to each page.
  pages is a map of {'path (i.e about)' 'page content as string'}"
  (zipmap (keys pages)
          (map layout/layout-page (vals pages))))

(defn markdown-pages [pages]
  (zipmap (map #(str/replace % #"\.md$" "") (keys pages))
          (map render-markdown-page (vals pages))))

(defn prepare-pages [pages]
  (zipmap (keys pages)
          ;; by sending a function and not a string containing HTML, stasis will only generate
          ;; pages on-demand, thus improving the development process
          (map #(fn [req] (highlight/highlight-code-blocks %)) (vals pages))))

(defn get-assets []
  (assets/load-assets "public" [#".*"]))

(defn get-raw-pages []
  ;; Merge page sources throws exception if two paths collide
  (stasis/merge-page-sources
    {:public (stasis/slurp-directory "resources/public" #".*\.(html|css|js|png)$")
     :partials (partial-pages (stasis/slurp-directory "resources/partials" #".*\.html$"))
     :markdown (markdown-pages (stasis/slurp-directory "resources/posts" #".*\.md$"))}))

(defn get-pages []
  (prepare-pages (get-raw-pages)))

(def app
  (optimus/wrap (stasis/serve-pages get-pages)
                get-assets
                optimizations/all
                serve-live-assets))

;; Export site
(def export-dir "dist")

(defn export []
  (let [assets (optimizations/all (get-assets) {})]
    (stasis/empty-directory! export-dir)
    (optimus.export/save-assets assets export-dir)
    (stasis/export-pages (get-pages) export-dir {:optimus-assets assets})))