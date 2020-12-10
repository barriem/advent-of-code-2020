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

(defn- i [str]
  (Integer/parseInt str))

; byr (Birth Year) - four digits; at least 1920 and at most 2002.
; iyr (Issue Year) - four digits; at least 2010 and at most 2020.
; eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
; hgt (Height) - a number followed by either cm or in:
;   If cm, the number must be at least 150 and at most 193.
;   If in, the number must be at least 59 and at most 76.
; hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
; ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
; pid (Passport ID) - a nine-digit number, including leading zeroes.
(defn- validate [{:keys [byr iyr eyr hgt hcl ecl pid cid]}]
  (and (not-any? nil? [byr iyr eyr hgt hcl ecl pid])
    (let 
      [byrValid (and (= (count byr) 4) (and (>= (i byr) 1920) (<= (i byr) 2002)))
       iyrValid (and (= (count iyr) 4) (and (>= (i iyr) 2010) (<= (i iyr) 2020)))
       eyrValid (and (= (count eyr) 4) (and (>= (i eyr) 2020) (<= (i eyr) 2030)))
       hgtRegex (re-matches #"^([0-9]*)(cm|in)$" hgt)
       hgtValid (and 
                  (not (empty? hgtRegex))
                  (let 
                    [height (nth hgtRegex 1)
                     units (nth hgtRegex 2)] 
                     (cond 
                       (= units "cm") (and (>= (i height) 150) (<= (i height) 193))
                       (= units "in") (and (>= (i height) 59) (<= (i height) 76))
                       :else false)))
      hclValid (not (empty? (re-matches #"^#[a-fA-F0-9]{6,6}$" hcl)))
      eclValid (not (empty? (re-matches #"^amb|blu|brn|gry|grn|hzl|oth$" ecl)))
      pidValid (not (empty? (re-matches #"^[0-9]{9,9}$" pid)))]
      (every? (fn [x] (= true x)) [byrValid iyrValid eyrValid hgtValid hclValid eclValid pidValid]))))

(defn- count-valid [passports]
  (count (filter #(validate %) passports)))

(defn- print-answer [count]
  (println (string/join " " ["Total number of valid passports:" count])))

(defn -main []
  (-> data-file
    (read-lines)
    (parse-to-passports)
    (count-valid)
    (print-answer)))
