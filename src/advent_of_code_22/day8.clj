(ns advent-of-code-22.day8
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [clojure.set])
  (:require [clojure.pprint :as pp]))

(defn read-lines [file-name]
  (let [res (io/resource file-name)]
    (str/split (slurp res) #"\n")))

(defn read-map [lines]
  (let [generate-tree (fn [c] {:height (Integer/parseInt (str c)) :visible 0 :score 1})
        tree-line #(into [] (map generate-tree %))]
    (into [] (map tree-line lines))))

(defn rotate-game [game]
  (let [n (count (first game))]
    (map (fn [i] (into [] (reverse (map #(get % i) game)))) (range n))))

(defn compute-visibility [tree-line]
  (loop [trees tree-line
         highest -1
         acc []]
    (if (empty? trees)
      acc
      (let [tree (first trees)]
        (if (> (:height tree) highest)
          (recur (drop 1 trees) (:height tree) (conj acc (assoc tree :visible 1)))
          (recur (drop 1 trees) highest (conj acc tree)))))))

(defn compute-score [tree-line]
  (loop [trees tree-line
         acc []]
    (let [tree (first trees)
          after (rest trees)]
      (if (empty? trees)
        acc
        (let [n-after (count after)
              n (count (take-while #(< (:height %) (:height tree)) after))
              score (if (= n n-after) n (+ n 1))
              new-tree (assoc tree :score (* (:score tree) score))]
          (recur after (conj acc new-tree)))))))

(defn compute-visibility-map [map-game]
  (map compute-visibility map-game))

(defn compute-score-map [map-game]
  (map compute-score map-game))

(let [game (->> (read-lines "day8.txt")
                (read-map))
      sol1 (comp
            compute-visibility-map rotate-game
            compute-visibility-map rotate-game
            compute-visibility-map rotate-game
            compute-visibility-map)
      sol2 (comp
            compute-score-map rotate-game
            compute-score-map rotate-game
            compute-score-map rotate-game
            compute-score-map)]
  (pp/pprint (->> (sol1 game)
                  (flatten)
                  (map #(get % :visible))
                  (reduce +)))
  (pp/pprint (->> (flatten (sol2 game))
                  (map #(get % :score))
                  (sort)
                  (reverse)
                  (first))))