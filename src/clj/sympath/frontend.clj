(ns sympath.frontend
  (:use
    [sympath.private :only [from-host-form to-host-form]])
  (:require
    [sympath.core :as core]))

(defn ^:export parse-path
  [x]
  (to-host-form (core/parse-path x)))

(defn ^:export menu
  [form]
  (to-host-form
    [{:group "customer"
      :name "personal_info"
      :label "Personal information"
      :available true
      :mandatory true
      :visited true}
     {:group "customer"
      :name "spouse_info"
      :label "Spouse information"
      :available false
      :mandatory false
      :visited false}
     {:group "payment"
      :name "credit_card"
      :label "Credit card"
      :available true
      :mandatory false
      :visited false}]))
