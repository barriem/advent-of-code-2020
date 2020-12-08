(ns advent.core
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def data-file (io/resource "input.txt"))

(defn- read-lines [file]
    (clojure.string/split-lines 
      (slurp file)))

(defn- array-to-map [[line, min, max, char, password]]
  {:min (Integer/parseInt min) :max (Integer/parseInt max) :char char :password password})     

(defn- regex-lines [lines]
  (map 
    (fn [l]
      (array-to-map 
        (re-matches #"^([0-9]*)-([0-9]*) ([a-zA-Z]): ([\S]*)" l))) lines))

(defn- password-valid-occurences [{:keys [min, max, char, password]}]
  (let 
    [freq (frequencies password)
     charKey (first (seq (char-array char)))
     charCount (or (get freq charKey) 0)
     satisifesMin (>= charCount min)
     satisfiesMax (< charCount (+ max 1))]
     (and satisifesMin satisfiesMax)))

(defn- char-is-as-pos [chars char pos]
  (if (<= pos (count chars))
    (let 
      [c (nth chars (- pos 1))]
      (= char c)) 
    false))

(defn- password-valid-positions [{:keys [min, max, char, password]}]
  (let 
    [chars (seq (char-array char))
     charKey (first (seq (char-array char)))
     charIsAtMin (char-is-as-pos password charKey min)
     charIsAtMax (char-is-as-pos password charKey max)]
     (or 
      (and charIsAtMin (not charIsAtMax))
      (and charIsAtMax (not charIsAtMin)))))

(defn- print-total [data]
  (println (string/join " " ["Total number of valid passwords:" (count data)])))

(defn- valid-passwords-based-on-occurences
  []
  (->> data-file
    (read-lines)
    (regex-lines)
    (filter #(password-valid-occurences %))
    (print-total)))

(defn- valid-passwords-based-on-posiion
 []
 (->> data-file
    (read-lines)
    (regex-lines)
    (filter #(password-valid-positions %))
    (print-total)))

(defn -main
  []
  (valid-passwords-based-on-occurences)
  (valid-passwords-based-on-posiion))
