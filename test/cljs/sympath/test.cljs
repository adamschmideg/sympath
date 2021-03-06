(ns ^{:doc    "tests for your awesome library"
      :author ""}
  sympath.test
  (:use
    [clojure.test :only [is]])
  (:require
    [test.feature1 :as f1]
    [treespec :as ts]))
    

(defn log [& more] (.log js/console more))

(def success 0)

(defn ^:export run []
  (.log js/console "Add your tests.")
  (log "add" (ts/add 2 3))
  ;(is (= (5 (ts/add 2 2))))
  (log "subtr" (ts/subtr 2 3))
  (f1/run)
  success)
