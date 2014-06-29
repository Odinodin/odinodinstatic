(ns odinodinstatic.util)

(defn fmap [fn amap]
  "Maps a function across every value in a map and returns a transformed map.
  Example:
  (fmap inc {:a 1 :b 2}) => {:a 2 :b 3}"
  (zipmap (keys amap)
          (map fn (vals amap))))