(ns advent-of-code-22.day9
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [clojure.set])
  (:require [clojure.pprint :as pp]))

(defn read-lines [file-name]
  (let [res (io/resource file-name)]
    (str/split (slurp res) #"\n")))

(defn read-input [lines]
  (let [split-lines (map #(str/split % #" ") lines)]
    (map (fn [line] (let [direction (first line)
                          quantity (second line)]
                      {:direction direction :quantity (Integer/parseInt quantity)})) split-lines)))

(defn instructions-to-steps [instructions]
  (mapcat #(repeat (:quantity %) (:direction %)) instructions))

(def actions {"U" (fn [[x y]] [x (+ y 1)])
              "D" (fn [[x y]] [x (- y 1)])
              "L" (fn [[x y]] [(- x 1) y])
              "R" (fn [[x y]] [(+ x 1) y])})

(defn move-queue [[h-x h-y] [t-x t-y]]
  (let [new-t-x (if (< t-x h-x) (+ t-x 1) (- t-x 1))
        new-t-y (if (< t-y h-y) (+ t-y 1) (- t-y 1))
        delta-x (abs (- h-x t-x))
        delta-y (abs (- h-y t-y))
        delta-total (+ delta-x delta-y)]
    (cond
      (and (= delta-x 0) (> delta-y 1)) [t-x new-t-y]
      (and (= delta-y 0) (> delta-x 1)) [new-t-x t-y]
      (> delta-total 2) [new-t-x new-t-y]
      :else [t-x t-y])))

(defn move-whole-queue [head queue]
  (loop [q queue
         acc [head]]
    (if (empty? q)
      acc
      (recur (rest q) (conj acc (move-queue (last acc) (first q)))))))

(defn compute-visited [steps starting-state]
  (loop [rest-steps steps
         state starting-state ; [[0 0] [0 0]] -- H - T
         visited #{[0,0]}]
    (if (empty? rest-steps)
      visited
      (let [new-head ((get actions (first rest-steps)) (first state))
            new-state (move-whole-queue new-head (rest state))]
        (recur (rest rest-steps) new-state (conj visited (last new-state)))))))

(let [small-input "day9-small.txt"
      full-input "day9.txt"
      steps  (->> (read-lines full-input)
                  (read-input)
                  (instructions-to-steps))]
  (pp/pprint (->> (compute-visited steps (repeat 2 [0 0]))
                  (count)))
  (pp/pprint (->> (compute-visited steps (repeat 10 [0 0]))
                  (count))))