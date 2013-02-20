(ns test.tree.core
  (:require
    [tree.core :as tree]))

(defn ^:export parse-primitive-test
  [s]
  (tree/parse-primitive s))
