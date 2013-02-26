(ns test.tree.core
  (:require
    [sympath.core :as tree]))

(defn ^:export parse-primitive-test
  [s]
  (tree/parse-primitive s))
