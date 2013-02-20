(ns test.tree.core
  (:use
    [midje.sweet]
    [midje.util :only [testable-privates]]
    [tree.core :only [add]]))

(testable-privates tree.core
  parse-primitive)

(facts "About add"
  (fact (add 2 1) => 3))

(tabular
  (fact "About parse-primitive"
    (parse-primitive ?str) => ?expected)
  ?str ?expected
  "text" "text"
  "3.5" "3.5" ; float not parsed
  "3" 3)
