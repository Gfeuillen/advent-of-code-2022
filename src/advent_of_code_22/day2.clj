(ns advent-of-code-22.day2
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [clojure.set])
  (:require [clojure.pprint :as pp]))

(defn read-lines [file-name]
  (let [res (io/resource file-name)]
    (str/split (slurp res) #"\n")))

(defn process-input [lines]
  (map #(str/split % #" ") lines))

(defn match-result [p1 p2]
  (let [m1 {:A "rock" :B "paper" :C "scissor"}
        m2 {:X "rock" :Y "paper" :Z "scissor"}
        values {:X 1 :Y 2 :Z 3}
        r1 (get m1 (keyword p1))
        r2 (get m2 (keyword p2))
        value (get values (keyword p2))]
    (cond
      (= r1 r2) (+ 3 value)
      (and (= r1 "rock") (= r2 "scissor")) (+ 0 value)
      (and (= r1 "rock") (= r2 "paper")) (+ 6 value)
      (and (= r1 "paper") (= r2 "rock")) (+ 0 value)
      (and (= r1 "paper") (= r2 "scissor")) (+ 6 value)
      (and (= r1 "scissor") (= r2 "rock")) (+ 6 value)
      (and (= r1 "scissor") (= r2 "paper")) (+ 0 value))))

(defn match-results [games]
  (map #(apply match-result %) games))

(defn translate-pair [p1 p2]
  (let [draw {:A "X" :B "Y" :C "Z"}
        win {:A "Y" :B "Z" :C "X" }
        loose {:A "Z" :B "X" :C "Y"}]
    (cond
      (= p2 "X") [p1 (get loose (keyword p1))]
      (= p2 "Y") [p1 (get draw (keyword p1))]
      (= p2 "Z") [p1 (get win (keyword p1))])))

(defn translate-pairs [games]
  (map #(apply translate-pair %) games))

(let [s1 (comp match-results process-input read-lines)
      s2 (comp match-results translate-pairs process-input read-lines)
      results1 (s1 "day2.txt")
      results2 (s2 "day2.txt")]
  (pp/pprint (reduce + results1))
  (pp/pprint (reduce + results2)))