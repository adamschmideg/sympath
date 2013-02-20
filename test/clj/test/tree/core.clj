(ns test.tree.core
  (:use
    [midje.sweet]
    [midje.util :only [testable-privates]]
    [tree.core :only [add]]))

(facts "About add"
  (fact (add 2 1) => 3))
