(ns test.tree.core
  (:use
    [midje.sweet]
    [midje.util :only [testable-privates]]
    [tree.core :only [add parse-path parse-selector]]))

(testable-privates tree.core
  parse-primitive
  parse-dictionary-string)

(facts "About add"
  (fact (add 2 1) => 3))

(tabular
  (fact "About parse-primitive"
    (parse-primitive ?str) => ?expected)
  ?str ?expected
  "text" "text"
  "3.5" "3.5" ; float not parsed
  "true" true
  "false" false
  "" nil
  "3" 3)

(tabular
  (fact "About parse-dictionary-string"
    (parse-dictionary-string ?str) => ?expected)
  ?str ?expected
  "plain" "plain"
  "2" 2
  "num=2" {:num 2}
  "str=foo" {:str "foo"}
  "str=foo,num=2" {:str "foo" :num 2}
  "*" {})

(facts "About public parse functions"
  (fact (parse-path "foo/2/bar") => [:foo 2 :bar])
  (fact (parse-selector "foo/2/isActive=true,type=Good/bar")
    => [:foo 2 {:isActive true, :type "Good"} :bar])
  (fact (parse-selector "/root")
    => [nil :root]))
