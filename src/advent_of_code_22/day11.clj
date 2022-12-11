(ns advent-of-code-22.day11
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [clojure.set])
  (:require [clojure.pprint :as pp])
  (:require [clojure.math.numeric-tower :as math]))

(defn read-lines [file-name]
  (let [res (io/resource file-name)]
    (str/split (slurp res) #"\n\n")))

(defn read-monkey [monkey-lines]
  (let [lines (str/split monkey-lines #"\n")
        n (Integer/parseInt (re-find #"\d+" (first lines)))
        items (->> (re-seq #"\d+" (second lines))
                   (map #(bigint (Integer/parseInt %))))
        op (nth (nth lines 2) 23)
        right-fn (if (= "ld" (apply str (take-last 2 (nth lines 2))))
                   identity
                   (fn [_] (Integer/parseInt (str/trim (apply str (take-last 2 (nth lines 2)))))))
        op-fn (case (str op)
                "+" #(+ % (right-fn %))
                "*" #(* % (right-fn %)))
        test-value (Integer/parseInt (re-find #"\d+" (nth lines 3)))
        true-monkey (Integer/parseInt (re-find #"\d+" (nth lines 4)))
        false-monkey (Integer/parseInt (re-find #"\d+" (nth lines 5)))
        test-fn (fn [x] (if (= 0 (mod x test-value)) true-monkey false-monkey))]
    {:n n
     :items (into [] items)
     :test-v test-value
     :op op-fn
     :test test-fn
     :inspected 0}))


(defn process-worry [monkey worry monkeys]
  (let [worry1 ((:op monkey) worry)
        ;; worry2 (quot worry1 3) ; Solution for 1
        worry2 (mod worry1 9699690) ;Sol for 2, use LCM
        next-monkey ((:test monkey) worry2)
        remove-item (update-in monkeys [(:n monkey) :items] #(drop 1 %))
        add-item (update-in remove-item [next-monkey :items] #(conj % worry2))]
    add-item))

(defn play-monkey [monkeys monkey]
  (loop [worries (:items monkey)
         acc monkeys]
    (if (empty? worries)
      acc
      (recur (rest worries) (process-worry monkey (first worries) acc)))))

(defn do-a-round [monkeys]
  (loop [monkey-ns (sort (keys monkeys))
         acc monkeys]
    (if (empty? monkey-ns)
      acc
      (let [n (first monkey-ns)
            monkey (get acc n)
            updated-acc (update-in acc [n :inspected] #(+ % (count (:items monkey))))] 
        (recur (rest monkey-ns) (play-monkey updated-acc monkey))))))

(defn compute-lcm [test-values]
  (loop [lcm (first test-values)
         values (rest test-values)]
    (if (empty? values)
      lcm
      (recur (math/lcm lcm (first values)) (rest values)))))

(let [small-input "day11-small.txt"
      full-input "day11.txt"
      game (->> (read-lines full-input)
                (map read-monkey)
                (map-indexed (fn [idx itm] [idx itm]))
                (into {}))
      lcm (compute-lcm (map #(:test-v %) (vals game)))
      game-1 (apply comp (repeat 20 do-a-round))
      game-2 (apply comp (repeat 10000 do-a-round))] 
  (pp/pprint lcm)
  (->> (game-2 game)
       (vals)
       (map #(:inspected %))
       (sort)
       (take-last 2)
       (apply *)
       (pp/pprint)))