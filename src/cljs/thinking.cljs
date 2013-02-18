(ns thinking
  (:require [calcative.wizard :as wz]))

(wz/defmenu
  [:personal
    :main
    [:others
      :spouse]
    [:children]])
      
; utility functions
(defn- years-passed [date] ,,,)

(defn- expect [fun type]
  (fn [_ _ _ value]
    (when-not (fun value)
      (error :type type
        :args {:value value}))))

(defn- not-empty-text 
  (expect #(< 0 (count %)) :not-empty-text))

(defn validate
  [db path value]
  (let [spec-nodes (wz/select spec path)
        best-match (first spec-nodes)
        validators
          (when-let [v (:validate best-match)]
            (if (fn? v [v] v)))
        type-check (fn [value] (= (:type best-match) (type* value)))
        all-validators (conj type-check validators)]
    (mapcat (fn [f] (f spec db path value)) all-validators)))
    

; make spec
(def spec (wz/make-spec
  (setting "birthDate" :validate (fn [spec db path value]
    (let [age (years-passed value)]
      (when (< age 18)
        (error :type :age-under-18 
          :args {:age age})))))

  ; partial selector
  (setting "name/given" :validate not-empty-text)

  ; absolute selector
  (setting "/personal/main/age" :type :integer :mandatory true)

  ; selector to match all entries in a list
  (setting "/personal/main/children/*/age"
    :validate (fn [spec db path value]
      (let [parent-age (years-passed (wz/select "/personal/main/birthDate"))]
        (when (< parent-age value)
          (error :type :child-older-than-parent
            :args {:parent parent-age, :child value})))))))

  ; match-all selector
  ; It provides default key-values pairs and behavior for each node
  (setting "*"
    :type :string
    :label (fn [spec db path]
      (lookup-i18n path))

; set up db
(def db ,,,)

; facts
(facts "field-data"
  (fact
    (wz/field-data db "/personal/main/name/middle")
      => {:type :string, :label "Középső név", :value "Archibald"})

  (fact
    (wz/field-data db "/personal/main/age")
      => {:type :integer, :label "Életkor", :mandatory true})

  (fact
    (wz/field-data db "/personal/main/name/prefix")
      => {:type :string, :label "Előtag", :options ["Dr" "Jr" "Sr"]})

  (let [db2 (wz/field-data db "/personal/main/birthDate")
        day-field {:type :integer, :range [1 31]}]
    (fact "field attributes"
      (:type db2) => :map
      (:keys db2) => ["year" "month" "day"]
      (:label db2) => "Születési dátum")
    (fact "global db, full path"
      (wz/field-data db "/personal/main/birthDate/day") => day-field)
    (fact "local db, full path"
      (wz/field-data db2 "/personal/main/birthDate/day") => day-field)
    (fact "local db, relative path"
      (wz/field-data db2 "day") => day-field))

  (let [db2 (wz/field-data db "/personal/main/children")
        jim (wz/field-data db2 "0")]
    (fact
      (:count db2) => 2
      (wz/field-data db "/personal/main/children/0/name")
        => {:type :string, :label "Név", :value "Jimmy"}
      (:keys jim) => ["name" "age"]
      (wz/field-data db2 "2") => nil
      (:new db2) => {:name "", :age null, :index 2}))

(facts "validate returns a vector of error messages"
  (fact
    (wz/validate db "/personal/main/birthDate/month" 5)
      => nil)
  (fact
    (wz/validate db "/personal/main/birthDate" {:year 2013 :month 5})
      => ["The 'day' field is mandatory"
          "You have to be above 18"]))
