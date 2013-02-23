(ns test.tree.core
  (:use
    [midje.sweet]
    [midje.util :only [testable-privates]]
    [tree.core :only [add parse-path parse-selector]]))

(testable-privates tree.core
  get*
  get-in*
  match-selector
  parse-primitive
  parse-dictionary-string)

(def test-form
  {:friends
    [{:name "Jack", :age 33}
     {:name "Mary", :age 22}
     {:name "Dick", :age 33}]
   :fellows
    [{:name "Peter", :age 75}]})

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

(facts "About get*"
  (let [form (:friends test-form)]
    (tabular
      (fact
        (count (get* form (parse-dictionary-string ?selector))) => ?count)
      ?selector ?count
      "name=Jack" 1
      "name=Jack,age=33" 1
      "name=Jack,age=33,missing=true" 0
      "age=33" 2
      "missing=true" 0)))

(facts "About get-in*"
  (tabular
    (fact
      (get-in* ?form ?selector) => ?result)
    ?selector ?form ?result
    [] [:x :y] [[:x :y]]
    [0] [:x :y] [:x]
    [1] [:x] nil
    [:friends {:name "Jack"} :age] test-form [33]
    [:friends {:age 33} :name] test-form ["Jack" "Dick"]
    [:friends {:age 33 :dead true} :name] test-form nil
    [:friends {:age 99} :name] test-form nil
    [:friends {} :name] test-form ["Jack" "Mary" "Dick"]
    [:friends {} :age] test-form [33 22 33]
    ))
  
(future-facts "About match-selector"
  (let [form test-form
        path "/list/0/name"]
    (tabular
      (fact
        (match-selector form path ?selector) => ?expected)
      ?selector ?expected
      "/list/*/name" truthy
      "/*/*/name" truthy
      "/*/*/*" truthy
      "/name" FALSEY
      "" truthy
      "/list/0/name" truthy
      "/list/1/name" FALSEY
      "/list/2/name" FALSEY
      "/list/age=33/name" truthy
      "age=33/name" truthy
      "age=22/name" FALSEY
      "age=11/name" FALSEY
      "name" truthy
      "*/name" truthy)))
