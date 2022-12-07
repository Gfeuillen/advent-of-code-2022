(ns advent-of-code-22.day7
  (:gen-class)
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [clojure.set])
  (:require [clojure.pprint :as pp]))

(defn read-lines [file-name]
  (let [res (io/resource file-name)]
    (str/split (slurp res) #"\$")))

(defn process-lines [lines]
  (loop [lines lines
         dir "root"
         acc {}]
    (let [line (first lines)
          command (apply str (take 2 line))
          arg (apply str (drop 3 line))
          dir-arg  (apply str (drop-last arg))
          res (filter #(not (empty? %)) (str/split (apply str (drop 2 line)) #"\n"))]
      (if (empty? lines)
        acc
        (cond
          (= command "cd")
          (recur
           (drop 1 lines)
           (if (= dir-arg "..")
             (->> (str/split dir #"-")
                  (drop-last 1)
                  (str/join "-"))
             (str dir "-" dir-arg))
           acc)
          (= command "ls") (let [split-res (map #(into [] (str/split % #" ")) res)
                                 dirs (filter #(= (first %) "dir") split-res)
                                 files (filter #(not (= (first %) "dir")) split-res)
                                 dir-names (map #(str dir "-" (second %)) dirs)
                                 total-files-size (reduce + (map #(Integer/parseInt (first %)) files))]
                             (recur
                              (drop 1 lines)
                              dir
                              (assoc acc (keyword dir) {:children dir-names
                                                        :size total-files-size}))))))))

(defn compute-size [folders folder]
  (loop [children (:children folder)
         acc (:size folder)]
    (if (empty? children)
      acc
      (let [child (get folders (keyword (first children)))
            child-child (:children child)]
        (recur
         (concat (drop 1 children) child-child)
         (+ acc (:size child)))))))

(let [folders (->> (read-lines "day7.txt")
                   (drop 2)
                   (map #(drop 1 %))
                   (process-lines))
      folder-sizes (map #(compute-size folders %) (vals folders))]
  (->> (filter #(<= % 100000) folder-sizes)
       (reduce +)
       (pp/pprint)) ;Part 1
  (let [root-size (last (sort folder-sizes))
        free-space (- 70000000 root-size)
        needed-space (- 30000000 free-space)]
    (->> (filter #(>= % needed-space) folder-sizes)
         (sort)
         (first)
         (pp/pprint)))) ;Part 2