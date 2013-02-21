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
  (let [entries (split s #",")]
    (condp = (count entries)
      0 :>> {}
      1 :>>
        (if (= "*" (first entries))
          {}
          (let [[k v] (split (first entries) #"=")]
            (if v
              (hash-map (keyword k) (parse-primitive v))
              k)))
      (reduce
        (fn [memo x]
          (let [[k v] (split x #"=")]
            (assoc memo (keyword k) (parse-primitive v))))
        {}
        entries))))

(defn ^:export add [x y]
  (+ x y))
