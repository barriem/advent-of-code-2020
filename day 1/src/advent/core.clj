(ns advent.core
  (:require [clojure.java.io :as io]
            [clojure.math.combinatorics :as combo]
            [clojure.string :as string]))

(def data-file (io/resource "input.txt"))

(defn- read-lines [file]
    (clojure.string/split-lines 
      (slurp file)))

(defn- get-int-array [lines]
  (map #(Integer/parseInt %) lines))

(defn- pairs [ints]
  (combo/combinations ints 2))
  
(defn- triples [ints]
  (combo/combinations ints 3))

(defn- sum-pairs [pairs]
  (map (fn [[a, b]] {:first a :second b :sum (+ a b)}) pairs))

(defn- sum-triples [triples]
  (map (fn [[a, b, c]] {:first a :second b :third c :sum (+ (+ a b) c)}) triples))
 
(defn- find-2020 [sums]
  (first (filter (fn [m] (= (:sum m) 2020)) sums)))

(defn- print-winner [{:keys [first second third]}]
  (println (string/join " " ["the winner is" first "and" second (if-not (nil? third) "with third") third "with sum" (* (* first second) (or third 1))])))

(defn- winner-for-pairs
  []
  (-> data-file
    (read-lines)
    (get-int-array)
    (pairs)
    (sum-pairs)
    (find-2020)
    (print-winner)))

(defn- winner-for-triples
 []
  (-> data-file
    (read-lines)
    (get-int-array)
    (triples)
    (sum-triples)
    (find-2020)   
    (print-winner)
    ))

(defn -main
  []
  (winner-for-pairs)
  (winner-for-triples))