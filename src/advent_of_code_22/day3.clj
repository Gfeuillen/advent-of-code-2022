(ns advent-of-code-22.day3
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [clojure.set])
  (:require [clojure.pprint :as pp]))

(defn read-lines [file-name]
  (let [res (io/resource file-name)]
    (str/split (slurp res) #"\n")))

(defn find-common-bags [line]
  (let [n (count line)
        half (quot n 2)
        first-half (into #{} (take half line))
        second-half (into #{} (drop half line))
        intersection (clojure.set/intersection first-half second-half)]
    (apply str (take 1 intersection))))

(defn give-value [char]
  (let [chars "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        index (.indexOf chars char)]
    (+ index 1)))

(defn solution-1 [lines]
  (map (comp give-value find-common-bags) lines))

(defn solution-2 [lines]
  (let [parts (partition 3 lines) 
        common-item (fn [bags] (let [sets (map #(set %) bags)] (apply clojure.set/intersection sets)))
        common-items (map #(apply str (take 1 (common-item %))) parts)]
  (map give-value common-items)))

(let [s1 (comp solution-1 read-lines)
      s2 (comp solution-2 read-lines)
      results1 (s1 "day3.txt")
      results2 (s2 "day3.txt")]
  (pp/pprint (reduce + results1))
  (pp/pprint (reduce + results2)))