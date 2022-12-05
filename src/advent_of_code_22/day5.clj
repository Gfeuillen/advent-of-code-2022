(ns advent-of-code-22.day5
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [clojure.set])
  (:require [clojure.pprint :as pp]))

(defn read-lines [file-name]
  (let [res (io/resource file-name)]
    (str/split (slurp res) #"\n\n")))

(defn read-crate-line [lines n]
  (if (= n 0)
    (filter #(not (= \space %)) (map #(nth % 1) lines))
    (filter #(not (= \space %)) (map #(nth % (+ 1 (* n 4))) lines))))

(defn read-crates [lines]
  (let [crates (str/split lines #"\n")
        ncrates (Integer/parseInt (second (re-find #"(\d+)\s$" (last crates))))
        indices (take ncrates (range))]
    (map #(read-crate-line (drop-last crates) %) indices)))

(defn read-moves [lines]
  (map (fn [line] (map #(Integer/parseInt %) (re-seq #"\d+" line))) lines))

(defn process-input [lines]
  (let [crates-lines (first lines)
        crates (read-crates crates-lines)
        moves-lines (str/split (second lines) #"\n")
        moves (read-moves moves-lines)]
    [crates moves]))

(defn list-update-in [l i x]
  (let [newlist (take i l)
        newlist (concat newlist (list x))
        newlist (concat newlist (drop (+ 1 i) l))]
    newlist))

(defn execute-move [crates move reverse?]
  (let [n (nth move 0)
        from-n (- (nth move 1) 1)
        to-n (- (nth move 2) 1)
        from (nth crates from-n)
        to (nth crates to-n)
        new-from (drop n from)
        new-to (concat (if reverse?
                         (reverse (take n from))
                         (take n from)) to)]
    (list-update-in (list-update-in crates to-n new-to) from-n new-from)))

(defn execute-moves [crates moves reverse?]
  (if (empty? moves)
    crates
    (execute-moves (execute-move crates (first moves) reverse?) (rest moves) reverse?)))

(let [lines (read-lines "day5.txt")
      [crates moves] (process-input lines)]
  (pp/pprint (apply str (map first (execute-moves crates moves true))))
  (pp/pprint (apply str (map first (execute-moves crates moves false)))))