(ns odinodinstatic.util)

(defn map-vals [fn m]
  "Maps a function across every value in a map and returns a transformed map with the same keys.
  Example:
  (map-vals inc {:a 1 :b 2}) => {:a 2 :b 3}"
  (zipmap (keys m)
          (map fn (vals m))))

(defn map-keys [fn m]
  "Maps a function across every key in a map and returns a transformed map, with the values intact.
  Example:
  (map-keys str {1 1 2 2}) => {'1' 1 '2' 2}"
  (zipmap (map fn (keys m))
          (vals m)))