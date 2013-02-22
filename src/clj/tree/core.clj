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
  (if-let [n (parse-int s)] n s))

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
      s)))

(defn ^:export add [x y]
  (+ x y))
