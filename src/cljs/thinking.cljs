(ns thinking
  (:require [calcative.wizard :as wz]))

(wz/defmenu
  [:personal
    :main
    [:others
      :spouse]
    [:children]])
      
(def db ,,,)

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

  (fact
    (wz/field-data db "/personal/main/birthDate")
      => {:type :map, :label "Születési dátum", :keys ["year" "month" "day"]})

  (fact
    (wz/field-data db "/personal/main/birthDate/day")
      => {:type :integer, :range [1 31]}))
