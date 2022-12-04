(ns advent-of-code-22.day4
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [clojure.set])
  (:require [clojure.pprint :as pp]))

(defn read-lines [file-name]
  (let [res (io/resource file-name)]
    (str/split (slurp res) #"\n")))

(defn process-line-1 [line]
  (let [parts (str/split line #",|-")
        int-parts (map #(Integer/parseInt %) parts)
        first-bigger (and (<= (nth int-parts 0) (nth int-parts 2)) (>= (nth int-parts 1) (nth int-parts 3)))
        second-bigger (and (<= (nth int-parts 1) (nth int-parts 3)) (>= (nth int-parts 0) (nth int-parts 2)))]
    (if (or first-bigger second-bigger)
      1
      0)))

(defn process-input-1 [lines]
  (map process-line-1 lines))


(defn process-line-2 [line]
  (let [parts (str/split line #",|-")
        int-parts (map #(Integer/parseInt %) parts)
        first-bigger (and (>= (nth int-parts 0) (nth int-parts 3)) (<= (nth int-parts 1) (nth int-parts 2)))
        second-bigger (and (>= (nth int-parts 1) (nth int-parts 2)) (<= (nth int-parts 0) (nth int-parts 3)))]
    (if (or first-bigger second-bigger)
      1
      0)))

(defn process-input-2 [lines]
  (map process-line-2 lines))


(let [s1 (comp process-input-1 read-lines)
      s2 (comp process-input-2 read-lines)
      results-small1 (s1 "day4-small.txt")
      results-small2 (s2 "day4-small.txt")
      results1 (s1 "day4.txt")
      results2 (s2 "day4.txt")]
  (pp/pprint (reduce + results-small1))
  (pp/pprint (reduce + results-small2))
  (pp/pprint (reduce + results1))
  (pp/pprint (reduce + results2)))