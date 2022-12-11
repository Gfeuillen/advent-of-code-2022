(ns advent-of-code-22.day10
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [clojure.set])
  (:require [clojure.pprint :as pp]))

(defn read-lines [file-name]
  (let [res (io/resource file-name)]
    (str/split (slurp res) #"\n")))


(defn readg-line [line]
  (if (= "no" (apply str (take 2 line)))
    identity
    (let [parts (str/split line #" ")]
      (fn [x] (+ x (Integer/parseInt (second parts)))))))

(defn read-map [lines]
  (map readg-line lines))

(defn process-game [instr]
  (loop [acc [1]
         x 1
         instructions instr]
    (if (empty? instructions)
      acc
      (let [op (first instructions)
            new-x (op x)
            is-noop (= new-x x)
            add-to-acc (if is-noop
                         [new-x]
                         [x new-x])
            new-acc (concat acc add-to-acc)]
        (recur new-acc new-x (rest instructions))))))

(let [small-input "day10-small.txt"
      full-input "day10.txt"
      game (->> (read-lines small-input)
                (read-map)
                (process-game))]
  (->>  (map-indexed (fn [i x] [(+ 1 i) x]) game) ;index starts at 0 otherwise
        (filter (fn [[i x]] (or (= i 20) (= 0 (mod (- i 20) 40)))))
        (map (fn [[i x]] (* i x)))
        (apply +)
        (pp/pprint))
  (->> (map-indexed (fn [i x] [(+ 1 i) x]) game)
       (map (fn [[i x]] (let [new-x (mod x 40)
                              new-i (mod i 40)
                              diff (- new-i new-x)
                              in-range (and (>= diff 0) (< diff 3))] 
                          (if in-range "#" "."))))
       (partition 40)
       (map #(apply str %))
       (pp/pprint)
   )
  )