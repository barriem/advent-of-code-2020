(ns advent.core
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def data-file (io/resource "input.txt"))

(defn- read-lines [file]
  (clojure.string/split-lines 
    (slurp file)))

(defn- lines-to-array [lines]
  (map #(seq (char-array %)) lines))

(defn- calculate-offset [currentOffset, rowLength steps]
  (let [provisionalOffset (+ currentOffset steps)]
    (if (>= provisionalOffset rowLength) (- provisionalOffset rowLength) provisionalOffset)))

(defn- move [rows steps]
  (reduce (fn [{:keys [numberOfTrees, currentOffset]} row]
    (let 
      [offset (calculate-offset currentOffset (count row) steps)
       charAtOffset (nth row offset)
       treeAtOffset (= (str charAtOffset) "#")
       treeCount (if treeAtOffset (+ numberOfTrees 1) numberOfTrees)]
     ;;  (println (string/join " " ["offset" offset "char" charAtOffset "tree" treeAtOffset]))
      {:numberOfTrees treeCount :currentOffset offset})) 
    {:numberOfTrees 0 :currentOffset 0}
    (drop 1 rows)))

(defn- print-outcome [{:keys [numberOfTrees]}]
  (println (string/join " " ["Found" numberOfTrees "trees on path"])))

(defn- move-with-1-step
  [rows]
  (-> 
    (move rows 1)
    (print-outcome)))

(defn- move-with-3-steps 
  [rows]
  (-> 
    (move rows 3)
    (print-outcome)))

(defn- move-with-5-steps 
  [rows]
  (-> 
    (move rows 5)
    (print-outcome)))
    
(defn- move-with-7-steps 
  [rows]
  (-> 
    (move rows 7)
    (print-outcome))) 

(defn- move-with-1-step-over-two-rows
  [rows]
  (-> 
    (move (take-nth 2 rows) 1)
    (print-outcome)))

(defn -main []
  (let 
    [rows (-> data-file
              (read-lines)
              (lines-to-array))]
    (move-with-1-step rows)
    (move-with-3-steps rows)
    (move-with-5-steps rows)
    (move-with-7-steps rows)
    (move-with-1-step-over-two-rows rows)))