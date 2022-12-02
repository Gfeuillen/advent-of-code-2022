(ns advent-of-code-22.day1
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [clojure.set])
  (:require [clojure.pprint :as pp]))

(defn read-elves [file-name]
  (let [res (io/resource file-name)]
    (str/split (slurp res) #"\n\n")))

(defn sum-elf [elf]
  (let [weights-str (str/split elf #"\n")
        weights (map #(Integer/parseInt %) weights-str)]
    (reduce + weights)))

(let [weights (->> (read-elves "day1.txt")
                   (map sum-elf))]
  (pp/pprint (apply max weights))
  (->> (sort weights)
       (take-last 3)
       (reduce +)
       pp/pprint))


