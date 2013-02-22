(ns tree.core
  (:use
    [clojure.string :only [split]]
    [common.impl :only [parse-int]]))

(defn debug
  [x]
  (println x)
  x)

(defn- parse-primitive
  [s]
  (condp = s
    "true" true
    "false" false
    "" nil
    (if-let [n (parse-int s)] n s)))

(defn- parse-dictionary-string
  [s]
  (if (re-find #"=" s)
    (->> (split s #",")
      (reduce
        (fn [memo x]
          (let [[k v] (split x #"=")]
            (assoc memo (keyword k) (parse-primitive v))))
        {}))
    (if (= "*" s)
      {}
      (parse-primitive s))))

(defn- parse-xxx
  [s parse-item]
  (map
    #(let [it (parse-item %1)]
      (if (string? it)
        (keyword it)
        it))
    (split s #"/")))

(defn parse-path
  [s]
  (parse-xxx s parse-primitive))

(defn parse-selector
  [s]
  (parse-xxx s parse-dictionary-string))

(defn ^:export add [x y]
  (+ x y))
