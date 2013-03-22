(ns sympath.demo
  (:use
    [sympath.private :only [from-host-form to-host-form]]
    [sympath.core :only [update query query-keyword]]))

(def db 
  (-> {}
    (update "/customer/name"
      {:type "string"})
    (update "/cart/*/item"
      {:type "string"})
    (update "/cart/*/amount"
      {:type "integer"
       :check
        (fn [db form path ancestors]
          (when (= 13 (first ancestors))
            {:error "unlucky", :args {:value (first ancestors)}}))})))

(defn ^:export field
  [form path]
  (to-host-form
    (query-keyword db (from-host-form form) path :value)))
