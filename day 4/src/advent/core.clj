(ns advent.core
  (:require [clojure.java.io :as io]
            [clojure.string :as string]))

(def data-file (io/resource "input.txt"))

(defn- read-lines [file]
  (string/split 
    (string/replace (slurp file) "\n" " ") 
    #"  "))

(defn- parse-to-passports [lines]
  (map 
    #(reduce 
      (fn [out token]
        (let 
          [keyvalue (drop 1 (re-matches #"^(\S\S\S):(.*)" token))
           key      (first keyvalue)
           value    (second keyvalue)]
          (assoc out (keyword key) value)))
      {}     
      (string/split % #" ")) 
    lines))

(defn- validate [{:keys [byr iyr eyr hgt hcl ecl pid cid]}]
  (not-any? nil? [byr iyr eyr hgt hcl ecl pid])ß)

(defn- count-valid [passports]
  (count (filter #(validate %) passports)))

(defn -main []
  (println
    (-> data-file
    (read-lines)
    (parse-to-passports)
    (count-valid))))
