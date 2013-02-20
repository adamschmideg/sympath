(ns common.impl)

(defn parse-int
  [s]
  (let [n (js/parseInt s)]
    ; beware: parseInt("3.5") => 3
    (if (= s (str n)) n s)))
