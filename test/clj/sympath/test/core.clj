(ns sympath.test.core
  (:use
    [midje.sweet]
    [midje.util :only [testable-privates]]
    ;[clojure.tools.trace :only [trace trace-vars trace-ns]]
    [sympath.core :only [add parse-path parse-selector query update query-keyword]]))

(testable-privates sympath.core
  get*
  get-in*
  match-selector
  parse-primitive
  parse-dictionary-string
  self-and-ancestors
  stricter-selector)

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
    => [:root]))

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
  
(facts "About match-selector"
  (let [form test-form
        path "/friends/0/name"]
    (tabular
      (fact
        (match-selector form path ?selector) => ?expected)
      ?selector ?expected
      "name" truthy
      "age=33/name" truthy
      "*/name" truthy
      "age=22/name" FALSEY
      "age=11/name" FALSEY
       "/friends/*/name" truthy
      "/name" FALSEY
      "/friends/age=33/name" truthy
      "/friends/0/name" truthy
      "/friends/1/name" FALSEY
      "/friends/2/name" FALSEY)))

(facts "About ancestors"
  (fact
    (self-and-ancestors test-form "/friends/1/name") =>
    ["Mary"
     {:name "Mary", :age 22}
     (:friends test-form)
     test-form]))

(future-facts "More about match-selector"
  (tabular
    (fact
      (match-selector test-form "/friends/0/name" ?selector) => ?expected)
    ?selector ?expected
    "" truthy
    "/*/*/name" truthy
    "/*/*/*" truthy))

(facts "About stricter-selector"
  (tabular
    (fact
      (stricter-selector ?s1 ?s2) => ?cmp)
    ?s1 ?s2 ?cmp
    "foo" "/foo" -1
    ))

(facts "About update and query"
  (let [db (-> {}
              (update "/friends/age=33/name" "old friends")
              (update "/friends/*/age" "all friends") 
              (update "name" "name")
              (update "*" "wildcard")
              (update "age=22/name" "young"))]
    (fact "update"
      db =>
        {true
          {3
            #{["/friends/age=33/name" "old friends"]
              ["/friends/*/age" "all friends"]}}
         false
          {1
            #{["name" "name"]
              ["*" "wildcard"]}
           2
            #{["age=22/name" "young"]}}})
    (tabular
      (fact "query"
        (map second (query db test-form ?path)) => ?result)
      ?path ?result
      "/friends/0/name" ["old friends" "name"]
      "/friends/1/age" ["all friends"]
      "/friends/1/name" ["young"]
      "/fellows/0/name" ["name"]
      "/fellows/0/age" []
      ;"/fellows/1/name" []
        )))

(def db 
  (-> {}
    (update "/customer/name"
      {:type "string"})
    (update "/cart/*/item"
      {:type "string"})
    (update "/cart/*/amount"
      {:type "integer"
       :check
        (fn [args]
          (when (= 13 (:value args))
            {:error "unlucky", :args {:value (:value args)}}))})))

(def form
  {:customer
    {:name "John Doe"}
   :cart
    [{:item "Book" :amount 2}
     {:item "Bread" :amount 1}]})

(facts "About check"
  (fact "No problem" (query-keyword db form "/cart/0/amount" :check) => nil)
  (let [form (assoc-in form [:cart 0 :amount] 13)]
    (fact "Error" (query-keyword db form "/cart/0/amount" :check)
      => {:error "unlucky", :args {:value 13}})))
