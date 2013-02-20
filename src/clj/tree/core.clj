(ns tree.core
  (:use
    [common.impl :only [parse-int]]))


(defn- parse-primitive
  [s]
  (if-let [n (parse-int s)] n s))

(defn ^:export add [x y]
  (+ x y))
